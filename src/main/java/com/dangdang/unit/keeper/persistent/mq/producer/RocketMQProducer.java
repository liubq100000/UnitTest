
package com.dangdang.unit.keeper.persistent.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.dangdang.unit.keeper.persistent.mq.vo.ConnVO;
import com.dangdang.unit.keeper.persistent.mq.vo.TopicVO;
import com.dangdang.unit.keeper.util.UkStringUtils;

/**
 * 生产者
 * 
 * @author liubq
 */
public class RocketMQProducer {
	//日志
	private static final Logger logger = LoggerFactory.getLogger(RocketMQProducer.class);

	//连接信息
	private ConnVO conn;

	//主题信息
	private TopicVO tvo;

	//生产者对象
	private DefaultMQProducer producer;

	/**
	 * 构造
	 * 
	 * @param conn
	 * @param tvo
	 */
	public RocketMQProducer(ConnVO conn, TopicVO tvo) {
		this.conn = conn;
		this.tvo = tvo;
	}

	/**
	 * 启动
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		// 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br> 
		// 注意：ProducerGroupName需要由应用来保证唯一<br> 
		// ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键， 
		// 因为服务器会回查这个Group下的任意一个Producer 
		producer = new DefaultMQProducer(tvo.getGroup());
		producer.setNamesrvAddr(conn.getUrl());
		producer.setInstanceName(tvo.getInstanceName());
		producer.setDefaultTopicQueueNums(4);
		// Producer对象在使用之前必须要调用start初始化，初始化一次即可
		//注意：切记不可以在每次发送消息时，都调用start方法 
		producer.start();
	}

	/**
	 * 关闭
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {

		// 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己 
		// 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法 
		producer.shutdown();
		// Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		// public void run() {
		// 	producer.shutdown();
		// }
		// }));

	}

	public void send(String key, String value) throws Exception {

		// 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。 
		// 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态，<br> 
		// 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，<br> 
		// 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。 
		Message msg = null;
		if (UkStringUtils.isEmpty(key)) {
			msg = new Message(tvo.getTopic(), tvo.getTags(), key, value.getBytes());
		}
		else {
			msg = new Message(tvo.getTopic(), tvo.getTags(), value.getBytes());
		}
		SendResult sendResult = producer.send(msg);
		logger.info("发送:[{}]结果:[{}]", value, sendResult);
	}
}
