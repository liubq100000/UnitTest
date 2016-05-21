
package com.dangdang.unit.keeper;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.unit.keeper.action.IAction;
import com.dangdang.unit.keeper.dto.UnitAction;
import com.dangdang.unit.keeper.dto.UnitPrepareData;
import com.dangdang.unit.keeper.dto.UnitResultData;
import com.dangdang.unit.keeper.dto.UnitTestCase;
import com.dangdang.unit.keeper.dto.UnitTestFun;
import com.dangdang.unit.keeper.persistent.IDataSetOperator;
import com.dangdang.unit.keeper.tool.UkXMLParser;
import com.dangdang.unit.keeper.util.UkListUtils;

/**
 * 库存测试基础类
 * 
 * @author liubq
 */
public abstract class AbstractTestCase {

	//日志
	private static final Logger logger = LoggerFactory.getLogger(AbstractTestCase.class);

	//测试结束是否需要清理数据
	public static boolean clearAfterTestCaseEnd = true;
	//测试开始是否需要清理数据
	public static boolean clearBeforeTestCaseBegin = true;
	//连接池工厂
	private IDBConnPool connfactory;

	@Before
	public void setup() throws Exception {
		connfactory = getDBConnFactory();
	}

	@After
	public void teardown() throws Exception {
		if (connfactory != null) {
			connfactory.close();
		}
	}

	/**
	 * 连接池工厂
	 * 
	 * @return
	 */
	protected abstract IDBConnPool getDBConnFactory();

	/**
	 * 取得连接
	 * 
	 * @param dbName
	 * @return
	 * @throws Exception
	 */
	private IDataSetOperator getConnect(String dbName) throws Exception {
		IDataSetOperator conn = connfactory.getConnect(dbName);
		if (conn == null) {
			throw new Exception(dbName + "没有配置连接信息");
		}
		return conn;
	}

	/**
	 * 根据文件名进行测试
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void testByFile(String file) throws Exception {
		testByFile(new File(file));
	}

	/**
	 * 根据文件进行测试
	 * 
	 * @param file
	 * @param caseNames
	 * @throws Exception
	 */
	public void testByFile(File file, String... caseNames) throws Exception {
		UnitTestFun fun = UkXMLParser.getInstance().parse(file);
		Set<String> set = null;
		if (caseNames != null && caseNames.length > 0) {
			set = new HashSet<String>();
			set.addAll(Arrays.asList(caseNames));
		}
		if (fun != null && fun.getTestcases() != null) {
			for (UnitTestCase testCase : fun.getTestcases()) {
				if (set != null && !set.contains(testCase.getName())) {
					continue;
				}
				testByCase(buildCaseContext(fun, testCase), testCase);
			}
		}
	}

	/**
	 * 创建公共区域
	 * 
	 * @param fun
	 * @param testCase
	 * @return
	 */
	private UnitCaseContext buildCaseContext(UnitTestFun fun, UnitTestCase testCase) {
		UnitCaseContext context = new UnitCaseContext();
		Map<String, UnitAction> actionMap = new HashMap<String, UnitAction>();
		if (UkListUtils.isNotEmpty(fun.getActions())) {
			for (UnitAction action : fun.getActions()) {
				actionMap.put(action.getName(), action);
			}
		}
		context.setActionMap(actionMap);
		context.initCase(testCase);
		return context;
	}

	/**
	 * 测试每个case
	 * 
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void testByCase(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		logger.debug("{} 准备开始测试",stockTestCase.getName());
		//清理环境
		if (clearBeforeTestCaseBegin) {
			clearEnvironment(context, stockTestCase);
			logger.info("{} 开始前的清理数据完成",stockTestCase.getName());
		}
		try {
			//测试启动
			start(context, stockTestCase);
			sleep(100);
			logger.debug("{} 测试用例启动",stockTestCase.getName());
			//准备数据
			prepare(context, stockTestCase);
			sleep(100);
			logger.info("{} 数据准备完成",stockTestCase.getName());
			//操作执行
			logger.debug("{} 动作执行开始",stockTestCase.getName());
			doAction(context, stockTestCase);
			sleep(100);
			logger.info("{} 动作执行完成",stockTestCase.getName());
			//对比数据
			diffData(context, stockTestCase);
			logger.info("{} 对比工作完成",stockTestCase.getName());
		}
		finally {
			if (clearAfterTestCaseEnd) {
				//清理环境
				clearEnvironment(context, stockTestCase);
				logger.info("{} 结束后的清理数据完成",stockTestCase.getName());
			}
			//关闭环境
			try {
				endEnvironment(context, stockTestCase);
			}
			catch (Exception ex) {
				logger.error("测试用例结束异常", ex);
			}

		}
		logger.info("{} 成功结束测试",stockTestCase.getName());
	}

	/**
	 * 休眠一定时间
	 * 
	 * @param time
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(100);
		}
		catch (Exception ex) {

		}
	}

	/**
	 * 清理环境
	 * 
	 * @param context
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void clearEnvironment(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		//空判断
		Assert.assertNotNull(stockTestCase);
		//清理数据
		IDataSetOperator conn;
		if (UkListUtils.isNotEmpty(stockTestCase.getPrepareDataSet())) {
			for (UnitPrepareData data : stockTestCase.getPrepareDataSet()) {
				conn = getConnect(data.getStoreKey());
				conn.clear(context, data);
			}
		}
		if (UkListUtils.isNotEmpty(stockTestCase.getResultDataSet())) {
			for (UnitResultData data : stockTestCase.getResultDataSet()) {
				conn = getConnect(data.getStoreKey());
				conn.clear(context, data);
			}
		}
	}

	/**
	 * 关闭
	 * 
	 * @param context
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void endEnvironment(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		//空判断
		Assert.assertNotNull(stockTestCase);
		//清理数据
		IDataSetOperator conn;
		if (UkListUtils.isNotEmpty(stockTestCase.getPrepareDataSet())) {
			for (UnitPrepareData data : stockTestCase.getPrepareDataSet()) {
				conn = getConnect(data.getStoreKey());
				conn.end(context, data);
			}
		}
		if (UkListUtils.isNotEmpty(stockTestCase.getResultDataSet())) {
			for (UnitResultData data : stockTestCase.getResultDataSet()) {
				conn = getConnect(data.getStoreKey());
				conn.end(context, data);
			}
		}
	}

	/**
	 * 执行指定动作
	 * 
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void doAction(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		UnitAction unitAction = context.getActionMap().get(stockTestCase.getAction());
		if (unitAction.getClassName() != null && unitAction.getClassName().length() > 0) {
			Object obj = Class.forName(unitAction.getClassName()).newInstance();
			IAction iAction = (IAction) obj;
			iAction.action(unitAction);
		}
	}

	/**
	 * 文件和数据的对比
	 * 
	 * 
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void diffData(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		List<UnitResultData> resultDatas = stockTestCase.getResultDataSet();
		if (UkListUtils.isEmpty(resultDatas)) {
			return;
		}
		IDataSetOperator conn;
		for (UnitResultData resultData : resultDatas) {
			conn = getConnect(resultData.getStoreKey());
			conn.diff(context, resultData);
		}
	}

	/**
	 * 测试前处理
	 * 
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void start(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		List<UnitPrepareData> prepareDatas = stockTestCase.getPrepareDataSet();
		if (UkListUtils.isNotEmpty(prepareDatas)) {
			IDataSetOperator conn;
			for (UnitPrepareData prepareData : prepareDatas) {
				conn = getConnect(prepareData.getStoreKey());
				conn.start(context, prepareData);
			}
		}
		List<UnitResultData> resultDatas = stockTestCase.getResultDataSet();
		if (UkListUtils.isNotEmpty(resultDatas)) {
			IDataSetOperator conn;
			for (UnitResultData resultData : resultDatas) {
				conn = getConnect(resultData.getStoreKey());
				conn.start(context, resultData);
			}
		}
	}

	/**
	 * 测试前处理
	 * 
	 * @param stockTestCase
	 * @throws Exception
	 */
	private void prepare(UnitCaseContext context, UnitTestCase stockTestCase) throws Exception {
		List<UnitPrepareData> prepareDatas = stockTestCase.getPrepareDataSet();
		if (UkListUtils.isNotEmpty(prepareDatas)) {
			IDataSetOperator conn;
			for (UnitPrepareData prepareData : prepareDatas) {
				conn = getConnect(prepareData.getStoreKey());
				conn.prepare(context, prepareData);
			}
		}
	}
}
