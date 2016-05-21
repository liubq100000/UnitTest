
package com.dangdang.unit.keeper.persistent.redis;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.UnitCaseContext;
import com.dangdang.unit.keeper.dto.UnitData;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.persistent.AbstractDataSetOperator;
import com.dangdang.unit.keeper.persistent.redis.type.IRedisType;
import com.dangdang.unit.keeper.persistent.redis.type.RedisTypeFactory;
import com.dangdang.unit.keeper.util.UkListUtils;
import com.dangdang.unit.keeper.util.UkMapList;

/**
 * Redis操作的相关功能
 * 
 * @author liubq
 */
public class RedisDataSetOperator extends AbstractDataSetOperator {

	//Jedis对象
	private Jedis jedis;

	/**
	 * 初始化
	 * 
	 * @param inJedis
	 */
	public RedisDataSetOperator(Jedis inJedis) {
		jedis = inJedis;
	}

	@Override
	public void prepare(UnitCaseContext context, UnitData dataSet) throws Exception {
		List<UnitTable> tables = dataSet.getDataSet();
		if (UkListUtils.isEmpty(tables)) {
			return;
		}
		UkMapList<String, UnitTable> mapList = new UkMapList<String, UnitTable>();
		for (UnitTable table : tables) {
			mapList.put(table.getTableName(), table);
		}
		IRedisType redisType;
		for (Map.Entry<String, List<UnitTable>> entry : mapList.entrySet()) {
			redisType = RedisTypeFactory.getProcessor(entry.getKey());
			if (redisType != null) {
				redisType.save(jedis, entry.getValue());
			}
		}

	}

	@Override
	public void clear(UnitCaseContext context, UnitData dataSet) throws Exception {
		List<UnitTable> tables = dataSet.getDataSet();
		if (UkListUtils.isEmpty(tables)) {
			return;
		}
		UkMapList<String, UnitTable> mapList = new UkMapList<String, UnitTable>();
		for (UnitTable table : tables) {
			mapList.put(table.getTableName(), table);
		}
		IRedisType redisType;
		for (Map.Entry<String, List<UnitTable>> entry : mapList.entrySet()) {
			redisType = RedisTypeFactory.getProcessor(entry.getKey());
			if (redisType != null) {
				redisType.clear(jedis, entry.getValue());
			}
		}

	}

	@Override
	public void diff(UnitCaseContext context, UnitData dataSet) throws Exception {
		List<UnitTable> tables = dataSet.getDataSet();
		if (UkListUtils.isEmpty(tables)) {
			return;
		}
		UkMapList<String, UnitTable> mapList = new UkMapList<String, UnitTable>();
		for (UnitTable table : tables) {
			mapList.put(table.getTableName(), table);
		}
		IRedisType redisType;
		for (Map.Entry<String, List<UnitTable>> entry : mapList.entrySet()) {
			redisType = RedisTypeFactory.getProcessor(entry.getKey());
			if (redisType != null) {
				redisType.diff(jedis, entry.getValue());
			}
		}
	}

}
