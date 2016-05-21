
package com.dangdang.ztest.common;

import com.dangdang.unit.keeper.IDBConnPool;
import com.dangdang.unit.keeper.persistent.IDataSetOperator;
import com.dangdang.unit.keeper.persistent.mq.RocketMQDataSetOperator;
import com.dangdang.unit.keeper.persistent.mq.vo.ConnVO;

public class RocketMQConnPool implements IDBConnPool {

	public IDataSetOperator getConnect(String name) throws Exception {
		if ("rocketMQ".equals(name)) {
			return this.rocketMQ(name);
		}
		return null;
	}

	public synchronized IDataSetOperator rocketMQ(String key) throws Exception {
		ConnVO conn = new ConnVO("192.168.134.107", "9876");
		return new RocketMQDataSetOperator(conn);
	}

	@Override
	public void close() throws Exception {
		
	}
}
