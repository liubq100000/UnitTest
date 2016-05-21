
package com.dangdang.unit.keeper.function;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uk函数过来替换类
 * 
 * @author liubq
 */
public class UkFunction {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(UkFunction.class);

	//检查类
	private static UkFunPreProcessor checker = new UkFunPreProcessor();

	//所有使用的函数列表
	private static List<IUkFunction> funList = new ArrayList<IUkFunction>();

	//初始化函数列表
	static {
		funList.add(new NULLUkFun());
		funList.add(new NOWUkFun());
		funList.add(new IPUkFun());
	}

	/**
	 * 依次执行所有的公式，符合条件就替换掉旧值
	 * 
	 * @param context 上下文
	 * @param data 输入数据
	 * @return 结果数据
	 * @throws Exception
	 */
	public static Object replace(UkFunctionContext context, Object data) throws Exception {
		Object newValue = data;
		if (funList != null && funList.size() > 0) {
			FunResult funRes = checker.process(data);
			//不需要函数处理，则返回旧值
			if (!funRes.isContinue()) {
				return data;
			}
			newValue = funRes.getValue();
			logger.info("***^_^*** value:{},is function", data);
			for (IUkFunction fun : funList) {
				logger.info("fun:{},value:{}", fun.getClass(), newValue);
				try {
					funRes = fun.replace(context, newValue.toString());
				}
				catch (Exception ex) {
					logger.error("函数执行错误", ex);
					throw ex;
				}
				if (!funRes.isContinue()) {
					return funRes.getValue();
				}
				newValue = funRes.getValue();
			}

		}
		return newValue;
	}

	/**
	 * 依次执行输入的的公式，符合条件就替换掉旧值
	 * 
	 * @param context 上下文
	 * @param data 输入数据
	 * @param runFuns 执行的函数列表
	 * @return 结果数据
	 * @return
	 * @throws Exception
	 */
	public static Object replace(UkFunctionContext context, Object data, IUkFunction... runFuns) throws Exception {
		Object newValue = data;
		if (runFuns != null && runFuns.length > 0) {
			FunResult funRes = checker.process(data);
			if (!funRes.isContinue()) {
				return funRes.getValue();
			}
			newValue = funRes.getValue();
			logger.info("***^_^*** value:{},is function", data);
			for (IUkFunction fun : funList) {
				logger.info("fun:{},value:{}", fun.getClass(), newValue);
				try {
					funRes = fun.replace(context, newValue.toString());
				}
				catch (Exception ex) {
					logger.error("函数执行错误", ex);
					throw ex;
				}

				if (!funRes.isContinue()) {
					return funRes.getValue();
				}
				newValue = funRes.getValue();
			}

		}
		return newValue;
	}
}
