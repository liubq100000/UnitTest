
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.List;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.persistent.redis.dto.RedisUtils;
import com.dangdang.unit.keeper.util.UkListUtils;

/**
 * 默认实现
 * 
 * @author liubq
 */
public abstract class RedisAbstractType implements IRedisType {

	/**
	 * 清理
	 * 
	 * @param jedis
	 * @param unitTables
	 */
	public void clear(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		if (UkListUtils.isEmpty(unitTables)) {
			return;
		}
		String key;
		for (UnitTable uTable : unitTables) {
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			for (UnitRow row : uTable.getRows()) {
				key = RedisUtils.getKey(row);
				jedis.del(key);
			}
		}

	}
}
