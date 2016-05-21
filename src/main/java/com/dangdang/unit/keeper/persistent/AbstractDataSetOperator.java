
package com.dangdang.unit.keeper.persistent;

import com.dangdang.unit.keeper.UnitCaseContext;
import com.dangdang.unit.keeper.dto.UnitData;

/**
 * 默认实现
 * 
 * @author liubq
 */
public abstract class AbstractDataSetOperator implements IDataSetOperator {
	/**
	 * 独立的测试用例清理环境
	 * 
	 * @param context
	 * @param data
	 * @throws Exception
	 */
	public void clear(UnitCaseContext context, UnitData data) throws Exception {

	}

	/**
	 * 独立的测试用例准备数据
	 * 
	 * @param context
	 * @param data
	 * @throws Exception
	 */
	public void prepare(UnitCaseContext context, UnitData data) throws Exception {

	}

	/**
	 * 独立的测试用例比较数据
	 * @param context
	 * @param dataSet
	 * @throws Exception
	 */
	public void diff(UnitCaseContext context, UnitData dataSet) throws Exception {

	}

	/**
	 * 独立的测试用例开始
	 * 
	 * @param context
	 * @param dataSet
	 * @throws Exception
	 */
	public void start(UnitCaseContext context, UnitData dataSet) throws Exception {

	}

	/**
	 * 独立的测试用例结束
	 * 
	 * @param context
	 * @param dataSet
	 * @throws Exception
	 */
	public void end(UnitCaseContext context, UnitData dataSet) throws Exception {

	}

	/**
	 * 测试初始化
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {

	}

	/**
	 * 测试销毁
	 * 
	 * @throws Exception
	 */
	public void destroy() throws Exception {

	}

}
