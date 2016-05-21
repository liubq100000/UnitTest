
package com.dangdang.unit.keeper.persistent.mq;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.dangdang.unit.keeper.UnitCaseContext;
import com.dangdang.unit.keeper.dto.UnitData;
import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.persistent.AbstractDataSetOperator;
import com.dangdang.unit.keeper.persistent.mq.consumer.RocketMQPullConsumer;
import com.dangdang.unit.keeper.persistent.mq.producer.RocketMQProducer;
import com.dangdang.unit.keeper.persistent.mq.tool.RockMQConst;
import com.dangdang.unit.keeper.persistent.mq.tool.RocketMQAsserts;
import com.dangdang.unit.keeper.persistent.mq.vo.ConnVO;
import com.dangdang.unit.keeper.persistent.mq.vo.DataVO;
import com.dangdang.unit.keeper.util.UkListUtils;
import com.dangdang.unit.keeper.util.UkMapList;
import com.dangdang.unit.keeper.util.UkStringUtils;

/**
 * RocketMQ的实现方式
 * 
 * 目前只是实现了接收MQ消息的功能
 * 
 * @author liubq
 */
public class RocketMQDataSetOperator extends AbstractDataSetOperator {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(RocketMQDataSetOperator.class);

	//连接
	private ConnVO conn;

	/**
	 * 构造
	 * 
	 * @param conn
	 */
	public RocketMQDataSetOperator(ConnVO conn) {
		super();
		this.conn = conn;
	}

	//消费者集合
	private static Map<String, RocketMQPullConsumer> cMap = new HashMap<String, RocketMQPullConsumer>();

	@Override
	public void start(UnitCaseContext context, UnitData dataSet) throws Exception {
		List<UnitTable> list = dataSet.getDataSet();
		if (UkListUtils.isEmpty(list)) {
			return;
		}
		RocketMQPullConsumer consumer;
		for (UnitTable uTable : list) {
			//<row group="1" instanceName="测试1" topic="31" tags="" key=""></row>
			//只有消费者需要开始就启动监听
			if (!RockMQConst.CONSUMER_TABLE.equals(uTable.getTableName())) {
				continue;
			}
			//没有数据
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			for (UnitRow uRow : uTable.getRows()) {
				//取得配置数据
				DataVO data = DataVO.assembleTopicVO(uRow);
				RocketMQAsserts.assertTopic(data);
				if (cMap.containsKey(data.getTopicKey())) {
					continue;
				}
				//启动一个消费者
				consumer = new RocketMQPullConsumer(conn, data);
				try {
					consumer.start();
				}
				catch (Exception ex) {
					try {
						consumer.close();
					}
					catch (Exception e1) {
					}
					throw ex;
				}

				//清理数据
				consumer.clear();
				//按照key，value方式缓存
				cMap.put(data.getTopicKey(), consumer);
			}
		}
	}

	@Override
	public void end(UnitCaseContext context, UnitData dataSet) throws Exception {
		for (RocketMQPullConsumer consumer : cMap.values()) {
			consumer.close();
		}
		cMap.clear();
	}

	public void prepare(UnitCaseContext context, UnitData dataSet) throws Exception {
		List<UnitTable> list = dataSet.getDataSet();
		if (UkListUtils.isEmpty(list)) {
			return;
		}
		RocketMQProducer producer;
		Map<String, RocketMQProducer> pMap = new HashMap<String, RocketMQProducer>();
		try {
			for (UnitTable uTable : list) {
				//<row group="1" instanceName="测试1" topic="31" tags="" key="">123</row>
				//只有生成者数据需要发送
				if (!RockMQConst.PRODUCER_TABLE.equals(uTable.getTableName())) {
					continue;
				}
				//没有数据
				if (UkListUtils.isEmpty(uTable.getRows())) {
					continue;
				}
				for (UnitRow uRow : uTable.getRows()) {
					//取得配置数据
					DataVO data = DataVO.assembleTopicVO(uRow);
					RocketMQAsserts.assertTopic(data);
					producer = pMap.get(data.getTopicKey());
					if (producer == null) {
						producer = new RocketMQProducer(conn, data);
						//按照key，value方式缓存
						pMap.put(data.getTopicKey(), producer);
						//启动一个消费者
						producer.start();

					}
					//发送数据
					producer.send(data.getKey(), data.getValue());
					//休眠一会，因为RocketMQ不保证顺序，如果不休眠，后面发的有可能先被接受，保证不了顺序了
					Thread.sleep(800);
				}
			}
		}
		finally {
			for (RocketMQProducer p : pMap.values()) {
				try {
					p.close();
				}
				catch (Exception ex) {
					logger.error("关闭生产者异常", ex);
				}

			}
		}
	}

	@Override
	public void diff(UnitCaseContext context, UnitData dataSet) throws Exception {
		List<UnitTable> list = dataSet.getDataSet();
		if (UkListUtils.isEmpty(list)) {
			return;
		}
		for (UnitTable uTable : list) {
			//<row group="1" instanceName="测试1" topic="31" tags="" key=""></row>
			//消费者需要比较
			if (!RockMQConst.CONSUMER_TABLE.equals(uTable.getTableName())) {
				continue;
			}
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			UkMapList<String, String> actMap = new UkMapList<String, String>();
			UkMapList<String, String> expMap = new UkMapList<String, String>();
			for (UnitRow uRow : uTable.getRows()) {
				//取得预期监听到的数据
				DataVO expData = DataVO.assembleTopicVO(uRow);
				RocketMQAsserts.assertTopic(expData);
				if (!UkStringUtils.isEmpty(expData.getValue())) {
					expMap.put(toKey(expData.getTopicKey(), expData.getKey()), expData.getValue());
				}
			}
			//取得实际监听到的数据
			for (Map.Entry<String, RocketMQPullConsumer> entry : cMap.entrySet()) {
				List<MessageExt> eList = entry.getValue().getDatas();
				//排序，升序
				sort(eList);
				for (MessageExt ext : eList) {
					String value = new String(ext.getBody(), RockMQConst.DEFAULT_CHARSET);
					if (!UkStringUtils.isEmpty(value)) {
						actMap.put(toKey(entry.getKey(), ext.getKeys()), value);
					}
				}
			}
			//判断是否相等
			RocketMQAsserts.assertEqual("RocketMQ,consumer ", expMap, actMap);

		}
	}

	/**
	 * 升序排序
	 */
	private void sort(List<MessageExt> eList) {
		Collections.sort(eList, new Comparator<MessageExt>() {
			@Override
			public int compare(MessageExt o1, MessageExt o2) {
				return o1.getBornTimestamp() - o2.getBornTimestamp() > 0 ? 1 : -1;
			}
		});
	}

	/**
	 * 主键
	 * 
	 * @param keys
	 * @return
	 */
	private String toKey(String... keys) {
		StringBuilder str = new StringBuilder();
		for (String key : keys) {
			if (UkStringUtils.isEmpty(key)) {
				str.append("NULL");
			}
			else {
				str.append(key.toUpperCase());
			}
		}
		return str.toString();
	}

}
