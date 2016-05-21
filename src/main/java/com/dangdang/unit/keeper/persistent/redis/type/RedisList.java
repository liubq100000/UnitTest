
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.ArrayList;
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

public class RedisList extends RedisAbstractType {

	public void save(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		if (UkListUtils.isEmpty(unitTables)) {
			return;
		}
		for (UnitTable uTable : unitTables) {
			if (UkListUtils.isEmpty(uTable.getRows())) {
				continue;
			}
			RedisRow rRow;
			UkMapList<String, RedisRow> mapList = new UkMapList<String, RedisRow>();
			for (UnitRow row : uTable.getRows()) {
				rRow = RedisUtils.getRow(row);
				if (rRow != null && !UkStringUtils.isEmpty(rRow.getKey()) && !UkStringUtils.isEmpty(rRow.getValue())) {
					mapList.put(rRow.getKey(), rRow);
				}
			}
			for (Map.Entry<String, List<RedisRow>> mapEntry : mapList.entrySet()) {
				if (UkListUtils.isEmpty(mapEntry.getValue())) {
					continue;
				}
				List<String> datas = new ArrayList<String>();
				for (RedisRow row : mapEntry.getValue()) {
					datas.add(row.getValue());
				}
				jedis.lpush(mapEntry.getKey(), datas.toArray(new String[0]));
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
			List<String> datas;
			for (String key : expectResult.keySet()) {
				datas = jedis.lrange(key, 0, -1);
				if (datas == null || datas.size() == 0) {
					actResult.put(key, null);
				}
				else {
					for (String data : datas) {
						actResult.put(key, data);
					}
				}

			}
			//对比
			RedisAsserts.assertEqualIgnoreSort("类型" + type(), expectResult, actResult);
		}
	}

	@Override
	public String type() {
		return "List";
	}
}
