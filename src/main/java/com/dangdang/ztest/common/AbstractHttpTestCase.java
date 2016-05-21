
package com.dangdang.ztest.common;

import com.dangdang.unit.keeper.AbstractTestCase;
import com.dangdang.unit.keeper.IDBConnPool;
import com.dangdang.unit.keeper.persistent.IDataSetOperator;

/**
 * 库存测试基础类
 * 
 * @author liubq
 */
public abstract class AbstractHttpTestCase extends AbstractTestCase {

	/**
	 * 连接池工厂
	 */
	public IDBConnPool getDBConnFactory() {
		return new IDBConnPool(){

			@Override
			public IDataSetOperator getConnect(String name) throws Exception {
				return null;
			}

			@Override
			public void close() throws Exception {
				
			}};
	}

}