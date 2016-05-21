
package com.dangdang.unit.keeper.util;

import java.util.Collection;

public class UkListUtils {
	public static <T> boolean isEmpty(Collection<T> collection) {
		if (collection == null || collection.size() == 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isNotEmpty(Collection<T> collection) {
		return !isEmpty(collection);
	}
}
