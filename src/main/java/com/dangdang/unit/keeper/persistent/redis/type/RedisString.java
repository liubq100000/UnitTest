
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.dto.UnitRow;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.persistent.redis.dto.RedisRow;
import com.dangdang.unit.keeper.persistent.redis.dto.RedisUtils;
import com.dangdang.unit.keeper.util.UkListUtils;
import com.dangdang.unit.keeper.util.UkMapList;
import com.dangdang.unit.keeper.util.UkStringUtils;

public class RedisString extends RedisAbstractType {

	public void save(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		if (UkListUtils.isEmpty(unitTables)) {
			return;
		}
		for (UnitTable uTable : unitTables) {
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			RedisRow rRow;
			Map<String, RedisRow> map = new HashMap<String, RedisRow>();
			for (UnitRow row : uTable.getRows()) {
				rRow = RedisUtils.getRow(row);
				if (rRow != null && !UkStringUtils.isEmpty(rRow.getKey()) && !UkStringUtils.isEmpty(rRow.getValue())) {
					map.put(rRow.getKey(), rRow);
				}
			}
			for (Map.Entry<String, RedisRow> mapEntry : map.entrySet()) {
				jedis.set(mapEntry.getKey(), mapEntry.getValue().getValue());
			}
		}
	}

	public void diff(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		if (UkListUtils.isEmpty(unitTables)) {
			return;
		}
		for (UnitTable uTable : unitTables) {
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			RedisRow rRow;
			UkMapList<String, String> expectResult = new UkMapList<String, String>();
			for (UnitRow row : uTable.getRows()) {
				rRow = RedisUtils.getRow(row);
				if (rRow != null && !UkStringUtils.isEmpty(rRow.getKey())) {
					expectResult.put(rRow.getKey(), rRow.getValue());
				}
			}
			UkMapList<String, String> actResult = new UkMapList<String, String>();
			String data;
			for (String key : expectResult.keySet()) {
				data = jedis.get(key);
				actResult.put(key, data);
			}
			//对比
			RedisAsserts.assertEqualIgnoreSort("类型" + type(), expectResult, actResult);
		}
	}

	@Override
	public String type() {
		return "String";
	}
}
