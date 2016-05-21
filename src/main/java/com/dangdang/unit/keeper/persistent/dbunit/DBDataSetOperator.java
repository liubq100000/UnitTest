
package com.dangdang.unit.keeper.persistent.dbunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.ComparisonFailure;

import org.dbunit.Assertion;
import org.dbunit.assertion.DefaultFailureHandler;
import org.dbunit.assertion.FailureFactory;
import org.dbunit.assertion.FailureHandler;
import org.dbunit.assertion.JUnitFailureFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;

import com.dangdang.unit.keeper.UnitCaseContext;
import com.dangdang.unit.keeper.dto.UnitData;
import com.dangdang.unit.keeper.dto.UnitTable;
import com.dangdang.unit.keeper.persistent.AbstractDataSetOperator;
import com.dangdang.unit.keeper.tool.UkDataSetBuilder;
import com.dangdang.unit.keeper.util.UkListUtils;

/**
 * 数据库操作的相关功能
 * 
 * @author liubq
 */
@SuppressWarnings("unused")
public class DBDataSetOperator extends AbstractDataSetOperator {

	//连接
	private DatabaseConnection conn;

	/**
	 * 构造
	 * 
	 * @param inConn
	 */
	public DBDataSetOperator(DatabaseConnection inConn) {
		conn = inConn;
	}

	//标准文件目录
	public static final String ROOT_URL = System.getProperty("user.dir") + "/src/main/resources/";

	//标准临时目录
	public static final String TMP_ROOT_URL = System.getProperty("user.dir") + "/target/UnitKeeper/";

	@Override
	public void prepare(UnitCaseContext context, UnitData data) throws Exception {
		//基本检查
		if (data == null) {
			return;
		}
		List<UnitTable> tables = data.getDataSet();
		if (UkListUtils.isEmpty(tables)) {
			return;
		}
		IDataSet xmlDestDataSet = getXmlDataSet(UkDataSetBuilder.buildDatasetStream(data));
		IDataSet replacementDataSet = createReplacementDataSet(context, xmlDestDataSet);
		this.write(replacementDataSet);
	}

	@Override
	public void clear(UnitCaseContext context, UnitData data) throws Exception {
		//基本检查
		if (data == null) {
			return;
		}
		List<UnitTable> tables = data.getDataSet();
		if (UkListUtils.isEmpty(tables)) {
			return;
		}
		for (UnitTable table : tables) {
			this.clearTable(table.getTableName());
		}

	}

	@Override
	public void diff(UnitCaseContext context, UnitData data) throws Exception {
		//基本检查
		if (data == null) {
			return;
		}
		List<UnitTable> tables = data.getDataSet();
		if (UkListUtils.isEmpty(tables)) {
			return;
		}
		IDataSet xmlDestDataSet = getXmlDataSet(UkDataSetBuilder.buildDatasetStream(data));
		for (UnitTable table : tables) {
			ITable dbTable = getDBTable(table);
			// handle null value, replace "[null]" strings with null
			IDataSet replacementDataSet = createReplacementDataSet(context, xmlDestDataSet);
			ITable xmlTable = replacementDataSet.getTable(table.getTableName());
			if (UkListUtils.isNotEmpty(table.getExcludedColumns())) {
				//exclude some columns which don't want to compare result
				String[] columns = table.getExcludedColumns().toArray(new String[0]);
				xmlTable = DefaultColumnFilter.excludedColumnsTable(xmlTable, columns);
				dbTable = DefaultColumnFilter.excludedColumnsTable(dbTable, columns);
			}
			//前缀
			String preMsg = "*^_^*(" + context.getTestCase().getName() + ")*^_^*测试不通过,请检查";
			Assertion.assertEquals(xmlTable, dbTable, getFailureHandler(preMsg));
		}
	}

	/**
	 * 失败处理类
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private FailureHandler getFailureHandler(String msg) throws Exception {
		DefaultFailureHandler failureHandler = new DefaultFailureHandler((Column[]) null);
		failureHandler.setFailureFactory(getJUnitFailureFactory(msg));
		return failureHandler;
	}

	/**
	 * @return the JUnitFailureFactory if JUnit is on the classpath or <code>null</code> if
	 * JUnit is not on the classpath.
	 * @throws  Exception 
	 */
	private FailureFactory getJUnitFailureFactory(String msg) throws Exception {
		Class.forName("junit.framework.Assert");
		// JUnit available
		return new UkJUnitFailureFactory(msg);
	}

	public class UkJUnitFailureFactory extends JUnitFailureFactory {
		private String preMessage = null;

		public UkJUnitFailureFactory(String inPreMessage) {
			preMessage = inPreMessage;
		}

		public Error createFailure(String message, String expected, String actual) {
			// Return the junit.framework.ComparisonFailure object
			return new ComparisonFailure(preMessage + "---->" + message, expected, actual);
		}

		public Error createFailure(String message) {
			// Return the junit.framework.AssertionFailedError object
			return new AssertionFailedError(preMessage + ": " + message);
		}
	}

	/**
	 * 读取XML文件内容
	 * 
	 * @param input
	 * @return
	 * @throws DataSetException
	 * @throws IOException
	 */
	private IDataSet getXmlDataSet(InputStream input) throws Exception {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		return builder.build(input);
	}

	/**
	 * 读取XML文件内容
	 * 
	 * @param file
	 * @return
	 * @throws DataSetException
	 * @throws IOException
	 */
	private IDataSet getXmlDataSet(File file) throws Exception {
		if (!file.exists()) {
			return null;
		}
		return getXmlDataSet(new FileInputStream(file));
	}

	/**
	 * 读取EXCEL文件内容
	 * 
	 * @param file
	 * @return
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws IOException
	 */
	private XlsDataSet getXlsDataSet(File file) throws Exception {
		if (!file.exists()) {
			return null;
		}
		InputStream is = new FileInputStream(file);
		return new XlsDataSet(is);
	}

	/**
	 * 取得数据
	 * 
	 * @param conn
	 * @param tableNames
	 * @return
	 * @throws SQLException
	 * @throws DataSetException
	 */
	private IDataSet getDBDataSet(String... tableNames) throws Exception {
		return conn.createDataSet(tableNames);
	}

	/**
	 * 取得数据的表数据
	 * 
	 * @param conn
	 * @param tableNames
	 * @return
	 * @throws SQLException
	 * @throws DataSetException
	 */
	private ITable getDBTable(UnitTable table) throws Exception {
		if (UkListUtils.isEmpty(table.getRows())) {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT * ");
			sql.append(" FROM ").append(table.getTableName());
			return conn.createQueryTable(table.getTableName() + System.currentTimeMillis(), sql.toString());
		}
		else {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT ");
			int len = table.getIncludedColumns().size();
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					sql.append(",");
				}
				sql.append(table.getIncludedColumns().get(i));
			}
			sql.append(" FROM ").append(table.getTableName());
			if (table.getOrderBy() != null && table.getOrderBy().length() > 0) {
				sql.append(" order by ").append(table.getOrderBy()).append(" ");
			}
			else {
				sql.append(" order by ").append(table.getIncludedColumns().get(0)).append(" asc ");
			}

			return conn.createQueryTable(table.getTableName() + System.currentTimeMillis(), sql.toString());
		}

	}

	/**
	 * 备份整个DB
	 * 
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private File readAll(DatabaseConnection conn) throws Exception {
		// create DataSet from database.
		IDataSet ds = conn.createDataSet();

		// create temp file
		File tempFile = createTempFile();

		// write the content of database to temp file
		FlatXmlDataSet.write(ds, new FileWriter(tempFile), "UTF-8");
		return tempFile;
	}

	/**
	 * 备份表的数据
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private File read(String... tableName) throws Exception {
		// back up specific files
		QueryDataSet qds = new QueryDataSet(conn);
		for (String str : tableName) {

			qds.addTable(str);
		}
		File tempFile = createTempFile();
		FlatXmlDataSet.write(qds, new FileWriter(tempFile), "UTF-8");

		return tempFile;

	}

	/**
	 * 保存数据
	 * 
	 * @param conn
	 * @param dataFile
	 * @throws Exception
	 */
	private void write(File dataFile) throws Exception {
		// recover database
		write(new FileInputStream(dataFile));
	}

	/**
	 * 回滚表的数据
	 * 
	 * @param conn
	 * @param dataFile
	 * @throws Exception
	 */
	private void write(InputStream inputStream) throws Exception {

		// get the temp file
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		IDataSet ds = builder.build(inputStream);

		// recover database
		write(ds);
	}

	/**
	 * 导入数据
	 * 
	 * @param conn
	 * @param dataSet
	 * @throws Exception
	 */
	private void write(IDataSet dataSet) throws Exception {
		// recover database
		DatabaseOperation.INSERT.execute(conn, dataSet);
	}

	/**
	 * 清理表的数据
	 * 
	 * @param conn
	 * @param tableName
	 * @throws Exception
	 */
	private void clearTable(String tableName) throws Exception {
		DefaultDataSet dataset = new DefaultDataSet();
		dataset.addTable(new DefaultTable(tableName));
		DatabaseOperation.DELETE_ALL.execute(conn, dataset);
	}

	/**
	 * 验证表是否为空
	 * 
	 * @param conn
	 * @param tableName
	 * @throws DataSetException
	 * @throws SQLException
	 */
	private void verifyTableEmpty(String tableName) throws DataSetException, SQLException {
		Assert.assertEquals(0, conn.createDataSet().getTable(tableName).getRowCount());
	}

	/**
	 * 验证表是否非空
	 * 
	 * @param conn
	 * @param tableName
	 * @throws DataSetException
	 * @throws SQLException
	 */
	private void verifyTableNotEmpty(String tableName) throws DataSetException, SQLException {
		Assert.assertNotEquals(0, conn.createDataSet().getTable(tableName).getRowCount());
	}

	/**
	 * 清空数据内容
	 * 
	 * @param dataSet
	 * @return
	 */
	private UkReplacementDataSet createReplacementDataSet(UnitCaseContext context, IDataSet dataSet) {
		return new UkReplacementDataSet(context, dataSet);
	}

	/**
	 * 临时文件目录
	 * 
	 * @return
	 */
	private File createTempFile() {
		File tempFile = new File(TMP_ROOT_URL + System.currentTimeMillis() + ".xml");
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		return tempFile;
	}
}
