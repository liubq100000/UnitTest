
package com.dangdang.unit.keeper.persistent.dbunit;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.ReplacementTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.unit.keeper.UnitCaseContext;
import com.dangdang.unit.keeper.function.UkFunction;
import com.dangdang.unit.keeper.function.UkFunctionContext;

/**
 * Decorator that replaces configured values from the decorated table
 * with replacement values.
 * 
 * @author liubq
 */
public class UkReplacementTable implements ITable {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ReplacementTable.class);

	//表
	private final ITable _table;

	//环境
	private final UnitCaseContext context;

	/**
	 * Create a new ReplacementTable object that decorates the specified table.
	 *
	 * @param inContext
	 * @param table
	 * @param inFunList
	 */
	public UkReplacementTable(UnitCaseContext inContext, ITable table) {
		_table = table;
		context = inContext;
	}

	////////////////////////////////////////////////////////////////////////
	// ITable interface

	public ITableMetaData getTableMetaData() {
		return _table.getTableMetaData();
	}

	public int getRowCount() {
		return _table.getRowCount();
	}

	public Object getValue(int row, String column) throws DataSetException {
		if (logger.isDebugEnabled())
			logger.debug("getValue(row={}, columnName={}) - start", Integer.toString(row), column);

		Object oldValue = _table.getValue(row, column);
		//替换
		Object newValue = replace(oldValue);
		logger.info("oldValue:{},newValue:{}", oldValue, newValue);
		return newValue;
	}

	/**
	 * 替换
	 * 
	 * @param value
	 * @return
	 * @throws DataSetException
	 */
	private Object replace(Object value) throws DataSetException {
		UkFunctionContext fContext = new UkFunctionContext(context.getBeginDate());
		try {
			return UkFunction.replace(fContext, value);
		}
		catch (Exception ex) {
			logger.error("异常", ex);
			throw new DataSetException(ex);
		}

	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append("[");
		sb.append(", _table=").append(_table);
		sb.append("]");
		return sb.toString();
	}
}