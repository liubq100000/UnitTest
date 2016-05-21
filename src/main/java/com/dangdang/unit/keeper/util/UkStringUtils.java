
package com.dangdang.unit.keeper.util;

import java.util.Collection;

public class UkStringUtils {

	/**
	 * 判断是否为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if (value == null || value.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 变成字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String join(Collection<String> set) {
		if (set == null || set.size() == 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for (String key : set) {
			str.append(key).append(",");
		}
		if (str.length() > 0) {
			str.deleteCharAt(str.length() - 1);
		}
		return str.toString();
	}

}
