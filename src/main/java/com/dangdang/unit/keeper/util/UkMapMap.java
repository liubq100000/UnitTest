
package com.dangdang.unit.keeper.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UkMapMap<K, F, V> {

	//内部对象
	private Map<K, Map<F, V>> mapList = new HashMap<K, Map<F, V>>();

	public void put(K k, F f, V v) {
		if (k != null && f != null) {
			Map<F, V> map = mapList.get(k);
			if (map == null) {
				map = new HashMap<F, V>();

			}
			map.put(f, v);
			mapList.put(k, map);
		}
	}

	public Map<F, V> get(K k) {
		return mapList.get(k);
	}

	public Set<Map.Entry<K, Map<F, V>>> entrySet() {
		return mapList.entrySet();
	}

	public Set<K> keySet() {
		return mapList.keySet();
	}

	public boolean containsKey(String key) {
		return mapList.containsKey(key);
	}

	public int size() {
		return mapList.size();
	}
}
