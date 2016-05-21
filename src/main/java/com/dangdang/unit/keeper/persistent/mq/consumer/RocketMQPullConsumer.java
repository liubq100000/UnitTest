
package com.dangdang.unit.keeper.persistent.mq.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.consumer.PullStatus;
import com.alibaba.rocketmq.client.consumer.store.OffsetStore;
import com.alibaba.rocketmq.client.consumer.store.ReadOffsetType;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.dangdang.unit.keeper.persistent.mq.tool.RockMQConst;
import com.dangdang.unit.keeper.persistent.mq.vo.ConnVO;
import com.dangdang.unit.keeper.persistent.mq.vo.TopicVO;

/**
 * 消费者
 * @author liubq
 */
public class RocketMQPullConsumer {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(RocketMQPullConsumer.class);

	//连接信息
	private final ConnVO conn;

	//主题信息
	private final TopicVO tvo;

	//生产者对象
	private DefaultMQPullConsumer consumer;

	//mq中的数据
	private List<MessageExt> datas = new ArrayList<MessageExt>();

	/**
	 * 清空数据
	 */
	public void clear() {
		datas.clear();
		if (consumer != null) {
			resetOffset();
		}

	}

	/**
	 * 取得监听到的数据
	 * 
	 * @return
	 */
	public List<MessageExt> getDatas() {
		return datas;
	}

	/**
	 * 构造
	 * 
	 * @param conn
	 * @param tvo
	 */
	public RocketMQPullConsumer(ConnVO conn, TopicVO tvo) {
		this.conn = conn;
		this.tvo = tvo;
	}

	/**
	 * 启动
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		/** 
		 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br> 
		 * 注意：ProducerGroupName需要由应用来保证唯一<br> 
		 * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键， 
		 * 因为服务器会回查这个Group下的任意一个Producer 
		 */
		consumer = new DefaultMQPullConsumer(tvo.getGroup());
		consumer.setNamesrvAddr(conn.getUrl());
		consumer.setInstanceName(tvo.getInstanceName());
		/** 
		 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br> 
		 * 注意：切记不可以在每次发送消息时，都调用start方法 
		 */
		consumer.start();
		//初始化
		initJob();
	}

	//是否已经关闭
	private boolean bClosed = false;

	/**
	 * 关闭
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
		consumer.shutdown();
		bClosed = true;
	}

	/**
	 * 初始化作业
	 */
	private void initJob() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					while (!bClosed) {
						pullData();
						Thread.sleep(100);
					}
				}
				catch (Exception e) {
					logger.error("pullData", e);
				}
			}
		};
		t.start();
	}

	/**
	 * 重置offset
	 */
	private void resetOffset() {
		String topic = tvo.getTopic();
		try {
			//获得所订阅主题的mq
			Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(topic);
			if (mqs == null) {
				return;
			}
			for (MessageQueue mq : mqs) {
				//更新队列偏移量
				OffsetStore offsetStore = consumer.getDefaultMQPullConsumerImpl().getOffsetStore();
				offsetStore.updateOffset(mq, consumer.maxOffset(mq), true);
				offsetStore.persist(mq);
			}
		}
		catch (Exception e) {
			logger.error("resetOffset", e);
			return;
		}
	}

	/**
	 * 拉取数据
	 * @throws  Exception 
	 */
	private void pullData() throws Exception {
		//获得所订阅主题的mq
		Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues(tvo.getTopic());
		if (mqs == null || mqs.size() == 0) {
			return;
		}
		for (MessageQueue mq : mqs) {
			//偏移量仓库
			OffsetStore offsetStore = consumer.getDefaultMQPullConsumerImpl().getOffsetStore();
			//获取消费偏移量
			long offset = offsetStore.readOffset(mq, ReadOffsetType.MEMORY_FIRST_THEN_STORE);
			//拉取消息
			PullResult pullResult = consumer.pull(mq, tvo.getTags(), offset, 10000);
			//没有找到消息
			if (pullResult.getPullStatus() != PullStatus.FOUND) {
				continue;
			}
			List<MessageExt> msgs = pullResult.getMsgFoundList();
			//当前队列没有消息可消费，取下一队列
			if (msgs == null || msgs.isEmpty()) {
				continue;
			}
			if (msgs != null) {
				for (MessageExt msg : msgs) {
					if (msg == null) {
						continue;
					}
					datas.add(msg);
					logger.info(new String(msg.getBody(), RockMQConst.DEFAULT_CHARSET));
					offset = msg.getQueueOffset() + 1;
				}
			}
			//更新队列偏移量
			offsetStore.updateOffset(mq, offset, true);
			offsetStore.persist(mq);
		}

	}
}
