
package com.dangdang.unit.keeper.function;

import java.util.Date;

/**
 * 函数执行上下文
 * 
 * @author liubq
 */
public class UkFunctionContext {

	//开始执行测试用例的时间
	private static Date nowDate = new Date();

	/**
	 * 构造
	 * 
	 * @param now
	 */
	public UkFunctionContext(Date now) {
		nowDate = now;
	}

	/**
	 * 执行这个函数的时间
	 * 
	 * @return
	 */
	public Date now() {
		return nowDate;
	}

}
