
package com.dangdang.unit.keeper.function;


public abstract class AbstractUkFunction implements IUkFunction {

	@Override
	public final FunResult replace(UkFunctionContext context, String value) {
		return doAction(context, value);

	}

	public abstract FunResult doAction(UkFunctionContext context, String value);

}
