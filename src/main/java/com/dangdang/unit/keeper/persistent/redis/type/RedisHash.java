
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.persistent.redis.dto.RedisHashRow;
import com.dangdang.unit.keeper.persistent.redis.dto.RedisUtils;
import com.dangdang.unit.keeper.util.UkListUtils;
import com.dangdang.unit.keeper.util.UkMapList;
import com.dangdang.unit.keeper.util.UkMapMap;
import com.dangdang.unit.keeper.util.UkStringUtils;

public class RedisHash extends RedisAbstractType {

	@Override
	public void save(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		if (UkListUtils.isEmpty(unitTables)) {
			return;
		}
		for (UnitTable uTable : unitTables) {
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			RedisHashRow hRow;
			UkMapList<String, RedisHashRow> mapList = new UkMapList<String, RedisHashRow>();
			for (UnitRow row : uTable.getRows()) {
				hRow = RedisUtils.getHashRow(row);
				if (hRow != null && !UkStringUtils.isEmpty(hRow.getKey()) && !UkStringUtils.isEmpty(hRow.getValue())) {
					mapList.put(hRow.getKey(), hRow);
				}
			}
			for (Map.Entry<String, List<RedisHashRow>> mapEntry : mapList.entrySet()) {
				if (UkListUtils.isEmpty(mapEntry.getValue())) {
					continue;
				}
				for (RedisHashRow item : mapEntry.getValue()) {
					if (UkStringUtils.isEmpty(item.getValue())) {
						continue;
					}
					jedis.hset(item.getKey(), item.getField(), item.getValue());
				}
			}
		}
	}

	@Override
	public void diff(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		if (UkListUtils.isEmpty(unitTables)) {
			return;
		}
		for (UnitTable uTable : unitTables) {
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			RedisHashRow expectRow;
			UkMapMap<String, String, String> expectResult = new UkMapMap<String, String, String>();
			for (UnitRow row : uTable.getRows()) {
				expectRow = RedisUtils.getHashRow(row);
				if (expectRow != null && !UkStringUtils.isEmpty(expectRow.getKey())) {
					expectResult.put(expectRow.getKey(), expectRow.getField(), expectRow.getValue());
				}
			}
			UkMapMap<String, String, String> actResult = new UkMapMap<String, String, String>();
			Map<String, String> fieldMap;
			for (String key : expectResult.keySet()) {
				fieldMap = jedis.hgetAll(key);
				if (fieldMap == null) {
					continue;
				}
				for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
					actResult.put(key, entry.getKey(), entry.getValue());
				}
			}
			//对比
			RedisAsserts.assertEqual("类型" + type(), expectResult, actResult);
		}

	}

	@Override
	public String type() {
		return "Hash";
	}
}
