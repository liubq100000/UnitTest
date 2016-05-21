
package com.dangdang.unit.keeper.persistent.mq.consumer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.dangdang.unit.keeper.persistent.mq.tool.RockMQConst;
import com.dangdang.unit.keeper.persistent.mq.vo.ConnVO;
import com.dangdang.unit.keeper.persistent.mq.vo.TopicVO;

/**
 * 消费者
 * @author liubq
 */
public class RocketMQPushConsumer {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(RocketMQPushConsumer.class);

	//连接信息
	private ConnVO conn;

	//主题信息
	private TopicVO tvo;

	//生产者对象
	private DefaultMQPushConsumer consumer;

	//mq中的数据
	private List<MessageExt> datas = new ArrayList<MessageExt>();

	/**
	 * 清空数据
	 */
	public void clear() {
		datas.clear();
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
	public RocketMQPushConsumer(ConnVO conn, TopicVO tvo) {
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
		consumer = new DefaultMQPushConsumer(tvo.getGroup());
		consumer.setNamesrvAddr(conn.getUrl());
		consumer.setInstanceName(tvo.getInstanceName());
		//添加监听者
		addListener();
		/** 
		 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br> 
		 * 注意：切记不可以在每次发送消息时，都调用start方法 
		 */
		consumer.start();
	}

	/**
	 * 关闭
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {

		/** 
		 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己 
		 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法 
		 */
		consumer.shutdown();
		//		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		//			public void run() {
		//				consumer.shutdown();
		//			}
		//		}));

	}

	/**
	 * 添加监听者
	 * 
	 * @throws Exception
	 */
	private void addListener() throws Exception {
		// 订阅指定topic下tags
		consumer.subscribe(tvo.getTopic(), tvo.getTags());

		//添加监听
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				if (msgs != null) {
					for (MessageExt msg : msgs) {
						if (msg == null) {
							continue;
						}
						datas.add(msg);
						try {
							logger.info(new String(msg.getBody(), RockMQConst.DEFAULT_CHARSET));
						}
						catch (Exception e) {
							logger.error("addListener", e);
						}
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

			}
		});
	}
}
