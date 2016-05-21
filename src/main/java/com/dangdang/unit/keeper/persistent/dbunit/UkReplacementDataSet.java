
package com.dangdang.unit.keeper.persistent.dbunit;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.ReplacementDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.unit.keeper.UnitCaseContext;

/**
 * Decorator that replace configured values from the decorated dataset
 * with replacement values.
 *
 * 
 * @author liubq
 */
public class UkReplacementDataSet extends AbstractDataSet {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(ReplacementDataSet.class);

	//环境上下文
	private final UnitCaseContext context;

	//数据集合
	private final IDataSet _dataSet;

	/**
	 * Create a new ReplacementDataSet object that decorates the specified dataset.
	 *
	 * @param inContext
	 * @param dataSet
	 */
	public UkReplacementDataSet(UnitCaseContext inContext, IDataSet dataSet) {
		_dataSet = dataSet;
		context = inContext;
	}

	/**
	 * 创建替代表
	 * 
	 * @param table
	 * @return
	 */
	private UkReplacementTable createReplacementTable(ITable table) {
		logger.debug("createReplacementTable(table={}) - start", table);
		return new UkReplacementTable(context, table);
	}

	////////////////////////////////////////////////////////////////////////////
	// AbstractDataSet class

	protected ITableIterator createIterator(boolean reversed) throws DataSetException {
		if (logger.isDebugEnabled())
			logger.debug("createIterator(reversed={}) - start", String.valueOf(reversed));

		return new ReplacementIterator(reversed ? _dataSet.reverseIterator() : _dataSet.iterator());
	}

	////////////////////////////////////////////////////////////////////////////
	// IDataSet interface

	public String[] getTableNames() throws DataSetException {
		logger.debug("getTableNames() - start");

		return _dataSet.getTableNames();
	}

	public ITableMetaData getTableMetaData(String tableName) throws DataSetException {
		logger.debug("getTableMetaData(tableName={}) - start", tableName);

		return _dataSet.getTableMetaData(tableName);
	}

	public ITable getTable(String tableName) throws DataSetException {
		logger.debug("getTable(tableName={}) - start", tableName);

		return createReplacementTable(_dataSet.getTable(tableName));
	}

	////////////////////////////////////////////////////////////////////////////
	// ReplacementIterator class

	private class ReplacementIterator implements ITableIterator {

		/**
		 * Logger for this class
		 */
		private final Logger logger = LoggerFactory.getLogger(ReplacementIterator.class);

		private final ITableIterator _iterator;

		public ReplacementIterator(ITableIterator iterator) {
			_iterator = iterator;
		}

		////////////////////////////////////////////////////////////////////////
		// ITableIterator interface

		public boolean next() throws DataSetException {
			logger.debug("next() - start");

			return _iterator.next();
		}

		public ITableMetaData getTableMetaData() throws DataSetException {
			logger.debug("getTableMetaData() - start");

			return _iterator.getTableMetaData();
		}

		public ITable getTable() throws DataSetException {
			logger.debug("getTable() - start");

			return createReplacementTable(_iterator.getTable());
		}
	}
}
