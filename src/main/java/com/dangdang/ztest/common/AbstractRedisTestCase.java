
package com.dangdang.ztest.common;

import com.dangdang.unit.keeper.AbstractTestCase;
import com.dangdang.unit.keeper.IDBConnPool;

/**
 * 库存测试基础类
 * 
 * @author liubq
 */
public abstract class AbstractRedisTestCase extends AbstractTestCase {

	/**
	 * 连接池工厂
	 */
	public IDBConnPool getDBConnFactory() {
		return new RedisConnPool();
	}

}