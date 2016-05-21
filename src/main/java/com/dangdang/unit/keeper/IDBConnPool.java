
package com.dangdang.unit.keeper;

import com.dangdang.unit.keeper.persistent.IDataSetOperator;

/**
 * 数据连接工厂
 * 
 * @author liubq
 */
public interface IDBConnPool {
	/**
	 * 取得数据库连接
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public IDataSetOperator getConnect(String name) throws Exception;

	/**
	 * 关闭数据看连接
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;
}
