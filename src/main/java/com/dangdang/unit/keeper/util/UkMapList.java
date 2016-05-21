
package com.dangdang.unit.keeper.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UkMapList<K, V> {

	//内部对象
	private Map<K, List<V>> mapList = new HashMap<K, List<V>>();

	public void put(K k, V v) {
		if (k != null) {
			List<V> list = mapList.get(k);
			if (list == null) {
				list = new ArrayList<V>();

			}
			list.add(v);
			mapList.put(k, list);
		}
	}

	public List<V> get(K k) {
		return mapList.get(k);
	}

	public Set<Map.Entry<K, List<V>>> entrySet() {
		return mapList.entrySet();
	}

	public int size() {
		return mapList.size();
	}

	public Set<K> keySet() {
		return mapList.keySet();
	}

	public boolean containsKey(String key) {
		return mapList.containsKey(key);
	}

}
