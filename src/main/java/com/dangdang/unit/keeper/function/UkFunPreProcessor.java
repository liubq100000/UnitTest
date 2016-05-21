
package com.dangdang.unit.keeper.function;

/**
 * 是否是公式的检查类
 * 
 * 
 * @author liubq
 */
public class UkFunPreProcessor {

	/**
	 * 检查是否是公式
	 * 
	 * @param value
	 * @return
	 */
	public FunResult process(Object value) {
		if (value == null || value.toString().trim().length() == 0) {
			return new FunResult(null, false);
		}
		if (!(value instanceof String)) {
			return new FunResult(value, false);
		}
		String valueStr = value.toString().trim().toUpperCase();
		if (valueStr.startsWith("[") && valueStr.endsWith("]")) {
			valueStr = valueStr.substring(1, valueStr.length() - 1);
			if (valueStr.length() > 0) {
				return new FunResult(valueStr, true);
			}
		}
		return new FunResult(valueStr, false);
	}

}
