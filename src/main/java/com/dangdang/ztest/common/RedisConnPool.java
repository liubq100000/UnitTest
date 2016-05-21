
package com.dangdang.ztest.common;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.IDBConnPool;
import com.dangdang.unit.keeper.persistent.IDataSetOperator;
import com.dangdang.unit.keeper.persistent.redis.RedisDataSetOperator;

public class RedisConnPool implements IDBConnPool {

	public IDataSetOperator getConnect(String name) throws Exception {

		if ("redisAAA".equals(name)) {
			return this.redisAAA(name);
		}
		return null;
	}

	public synchronized void close() {
	}

	public synchronized IDataSetOperator redisAAA(String key) throws Exception {
		Jedis jedis = new Jedis("127.0.0.1");
		RedisDataSetOperator op = new RedisDataSetOperator(jedis);
		return op;
	}
}
