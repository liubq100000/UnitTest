
package com.dangdang.unit.keeper.function;

/**
 * 函数处理结果
 * 
 * @author liubq
 */
public class FunResult {
	//值
	private Object value;

	//是否继续执行
	private boolean isContinue;

	public FunResult(Object value, boolean isContinue) {
		this.value = value;
		this.isContinue = isContinue;
	}

	public Object getValue() {
		return value;
	}

	public boolean isContinue() {
		return isContinue;
	}
}
