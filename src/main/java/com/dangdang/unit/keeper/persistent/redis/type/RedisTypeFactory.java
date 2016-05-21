
package com.dangdang.unit.keeper.persistent.redis.type;

import java.util.HashMap;
import java.util.Map;

public class RedisTypeFactory {
	public static IRedisType getProcessor(String type) {
		return map.get(type);
	}

	private static Map<String, IRedisType> map = new HashMap<String, IRedisType>();

	private static void addMap(IRedisType rType) {
		map.put(rType.type(), rType);
	}

	static {
		addMap(new RedisHash());
		addMap(new RedisList());
		addMap(new RedisSet());
		addMap(new RedisSortedSet());
		addMap(new RedisString());
	}
}
