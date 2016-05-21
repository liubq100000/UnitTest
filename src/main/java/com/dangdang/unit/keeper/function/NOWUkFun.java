
package com.dangdang.unit.keeper.function;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NOWUkFun extends AbstractUkFunction {

	public static final String FUN_NAME = "NOW()";

	public static final String FUN_NAME1 = "NOW";

	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public FunResult doAction(UkFunctionContext context, String value) {
		String valueStr = value.trim().toUpperCase();
		valueStr = valueStr.replaceAll(" ", "");
		Object newValue;
		if (FUN_NAME.equalsIgnoreCase(valueStr) || FUN_NAME1.equalsIgnoreCase(valueStr)) {
			newValue = format.format(context.now());
			return new FunResult(newValue, false);
		}
		else if (valueStr.indexOf(FUN_NAME) >= 0) {
			newValue = comput(context, valueStr);
			return new FunResult(newValue, false);
		}
		//没有匹配上继续下面测处理
		return new FunResult(value, true);
	}

	private Object comput(UkFunctionContext context, String valueStr) {
		if (!valueStr.startsWith(FUN_NAME)) {
			throw new RuntimeException("simple ([NOW()-100]) ,expression format is wrong :" + valueStr);
		}
		String op = valueStr.substring(FUN_NAME.length(), FUN_NAME.length() + 1);
		if (!"-".equals(op) && !"+".equals(op)) {
			throw new RuntimeException("expression format is wrong :" + valueStr);
		}
		long time = 0;
		try {
			time = Long.valueOf(valueStr.substring(FUN_NAME.length() + 1));
			time = time * 1000;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		long now = context.now().getTime();
		String result;
		if ("-".equals(op)) {
			result = format.format(new Date(now - time));
		}
		else {
			result = format.format(new Date(now + time));
		}
		return result;
	}
}
