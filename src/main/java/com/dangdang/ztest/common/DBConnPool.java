
package com.dangdang.ztest.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;

import com.dangdang.unit.keeper.IDBConnPool;
import com.dangdang.unit.keeper.persistent.IDataSetOperator;
import com.dangdang.unit.keeper.persistent.dbunit.DBDataSetOperator;

public class DBConnPool implements IDBConnPool {

	public IDataSetOperator getConnect(String name) throws Exception {

		if ("testdb".equals(name)) {
			return this.testdb(name);
		}
		return null;
	}

	public synchronized void close() {
		for (Map.Entry<String, DatabaseConnection> entry : cache.entrySet()) {
			if (entry.getValue() != null) {
				try {
					entry.getValue().close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		cache.clear();
	}

	private Map<String, DatabaseConnection> cache = new HashMap<String, DatabaseConnection>();

	public synchronized IDataSetOperator testdb(String key) throws Exception {
		DatabaseConnection connection = cache.get(key);
		if (connection == null || connection.getConnection() == null || connection.getConnection().isClosed()) {
			// get DataBaseSourceConnection
			Class.forName("com.mysql.jdbc.Driver");
			String dburl = "jdbc:mysql://10.255.209.59:3306/testdb?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true";
			Connection jdbcConnection = DriverManager.getConnection(dburl, "root", "root123");

			//get DataBaseSourceConnection
			connection = new DatabaseConnection(jdbcConnection);
			cache.put(key, connection);
			//config database as MySql
			DatabaseConfig dbConfig = connection.getConfig();
			dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

		}
		return new DBDataSetOperator(connection);
	}
}
