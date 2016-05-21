
package com.dangdang.unit.keeper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dangdang.unit.keeper.dto.UnitAction;
import com.dangdang.unit.keeper.dto.UnitTestCase;

/**
 * 测试上下文
 * 
 * @author liubq
 */
public class UnitCaseContext {

	//《动作名称，动作》
	private Map<String, UnitAction> actionMap = new HashMap<String, UnitAction>();

	/**
	 * 动作集合
	 * 
	 * @return
	 */
	public Map<String, UnitAction> getActionMap() {
		return actionMap;
	}

	/**
	 * 动作集合
	 * 
	 * @param actionMap
	 */
	public void setActionMap(Map<String, UnitAction> actionMap) {
		this.actionMap = actionMap;
	}

	/**
	 * 初始化
	 * @param testCase
	 */
	public void initCase(UnitTestCase testCase) {
		this.testCase = testCase;
		beginDate = new Date();
	}

	//正在处理的测试用例
	private UnitTestCase testCase;

	//开始执行测试用例的时间
	private static Date beginDate = null;

	/**
	 * 开始执行的时间
	 * 
	 * @return
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 正在处理的测试用例
	 * 
	 * @return
	 */
	public UnitTestCase getTestCase() {
		return testCase;
	}

}
