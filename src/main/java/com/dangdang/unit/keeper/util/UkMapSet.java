
package com.dangdang.unit.keeper.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UkMapSet<K, V> {

	//内部对象
	private Map<K, Set<V>> mapSet = new HashMap<K, Set<V>>();

	public void put(K k, V v) {
		if (k != null) {
			Set<V> map = mapSet.get(k);
			if (map == null) {
				map = new HashSet<V>();

			}
			map.add(v);
			mapSet.put(k, map);
		}
	}

	public Set<V> get(K k) {
		return mapSet.get(k);
	}

	public Set<Map.Entry<K, Set<V>>> entrySet() {
		return mapSet.entrySet();
	}

	public Set<K> keySet() {
		return mapSet.keySet();
	}

	public boolean containsKey(String key) {
		return mapSet.containsKey(key);
	}

	public int size() {
		return mapSet.size();
	}
}
