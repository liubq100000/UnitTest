
package com.dangdang.unit.keeper.function;


public class NULLUkFun extends AbstractUkFunction {

	@Override
	public FunResult doAction(UkFunctionContext context, String value) {
		String valueStr = value.trim();
		if ("NULL".equalsIgnoreCase(valueStr)) {
			return new FunResult(null, false);
		}
		else if (valueStr.trim().length() == 0) {
			return new FunResult(null, false);
		}
		//没有匹配上继续下面测处理
		return new FunResult(value, true);
	}

}
