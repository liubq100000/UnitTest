
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.List;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.dto.UnitTable;

/**
 * Redis类型
 * 
 * @author liubq
 */
public interface IRedisType {

	/**
	 * 清理
	 * 
	 * @param jedis
	 * @param unitTables
	 * @throws Exception
	 */
	public void clear(Jedis jedis, List<UnitTable> unitTables) throws Exception;

	/**
	 * 保存
	 * 
	 * @param jedis
	 * @param unitTables
	 * @throws Exception
	 */
	public void save(Jedis jedis, List<UnitTable> unitTables) throws Exception;

	/**
	 * 对比
	 * 
	 * @param jedis
	 * @param unitTables
	 * @throws Exception
	 */
	public void diff(Jedis jedis, List<UnitTable> unitTables) throws Exception;

	/**
	 * 处理的类型
	 */
	public String type();
}
