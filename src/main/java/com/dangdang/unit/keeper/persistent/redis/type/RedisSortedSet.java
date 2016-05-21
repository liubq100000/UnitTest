
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.List;

import redis.clients.jedis.Jedis;

import com.dangdang.unit.keeper.dto.UnitTable;

public class RedisSortedSet extends RedisAbstractType {

	public void save(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		throw new Exception("未实现");
	}

	public void diff(Jedis jedis, List<UnitTable> unitTables) throws Exception {
		throw new Exception("未实现");
	}

	@Override
	public String type() {
		return "SortedSet";
	}

}
