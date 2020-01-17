package com.thtf.generate.server.dao.impl;

import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.generate.api.model.*;
import com.thtf.generate.server.dao.IDatabaseDAO;
import com.thtf.generate.server.dao.IMetaDataConverter;
import com.thtf.generate.server.enums.GenerateCode;
import com.thtf.generate.server.utils.StringUtils;

import java.sql.*;
import java.util.*;

/**
 * ========================
 * 抽象数据库元信息查询类
 * Author：pyy
 * Date：2019-9-25 16:29:13
 * Version: v1.0
 * ========================
 */
public abstract class AbstractDatabasetDAOImpl implements IDatabaseDAO {

	protected IMetaDataConverter converter;
	
	protected DataSource dataSource;
	private Connection connection;

	protected static final String DRIVER = "driver";
	protected static final String URL = "url";
	protected static final String QUERY_TABLE = "query_table";
	protected static final String QUERY_COLUMN = "query_column";
	protected static final String QUERY_INDEX = "query_index";
	protected static final String QUERY_PRIMARY_KEY = "query_primary_key";
	protected static final String QUERY_FOREIGN_KEY = "query_foreign_key";
	protected static final String QUERY_TRIGGER = "query_trigger";

	public AbstractDatabasetDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<Map<String, String>> query(String sql, String[] params) {
		if (sql == null) {
			ExceptionCast.cast(GenerateCode.GENERATE_QUERY_SQL_ISNULL);
		}
		List<Map<String, String>> result = new LinkedList<Map<String, String>>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sql);
			if (params != null) {
				for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
					pstmt.setString(paramIndex + 1, params[paramIndex]);
				}
			}
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnSize = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int columnIndex = 1; columnIndex <= columnSize; columnIndex++) {
					String columnName = rsmd.getColumnLabel(columnIndex);
					String columnValue = rs.getString(columnName);
					map.put(columnName, columnValue);
				}
				result.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ExceptionCast.cast(CommonCode.FAIL);
		} finally {
			close(rs, pstmt);
		}
		return result;
	}

	@Override
	public List<Table> getTables() {
		List<Table> tables = new ArrayList<Table>();
		List<Map<String, String>> result = query(getQuerySql(QUERY_TABLE), null);
		for (Map<String, String> map : result) {
			tables.add(converter.convertMap2Table(map));
		}
		return tables;
	}
	
	@Override
	public List<Column> getColumns(String tableName) {
		List<Column> columns = new ArrayList<Column>();
		List<Map<String, String>> result = query(getQuerySql(QUERY_COLUMN), new String[] { tableName });
		for (Map<String, String> map : result) {
			columns.add(converter.convertMap2Column(map));
		}

		return columns;
	}

	@Override
	public List<PrimaryKey> getPrimaryKeys(String tableName) {
		List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();
		List<Map<String, String>> result = query(getQuerySql(QUERY_PRIMARY_KEY),
				new String[] { tableName });

		for (Map<String, String> map : result) {
			primaryKeys.add(converter.convertMap2PrimaryKey(map));
		}
		return primaryKeys;
	}

	@Override
	public List<ForeignKey> getForeignKeys(String tableName) {
		List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>(0);
		
		List<Map<String, String>> result = query(getQuerySql(QUERY_FOREIGN_KEY),
				new String[] { tableName });

		for (Map<String, String> map : result) {
			foreignKeys.add(converter.convertMap2ForeignKey(map));
		}

		
		return foreignKeys;
	}

	@Override
	public List<Index> getIndexes(String tableName) {
		Map<String, Index> indexMap = new HashMap<>();
		String querySql = getQuerySql(QUERY_INDEX);
		if (StringUtils.isBlank(querySql)) {
			ExceptionCast.cast(GenerateCode.GENERATE_GET_QUERY_SQL_EXCEPTION);
		}
		List<Map<String, String>> result = query(querySql, new String[] { tableName });
		for (Map<String, String> map : result) {
			Index index = converter.convertMap2Index(map);
			String indexName = index.getName();
			if (!indexMap.containsKey(indexName)) {
				indexMap.put(indexName, index);
			} else {
				if (!index.getCloumns().isEmpty()) {
					indexMap.get(indexName).addCloumn(index.getCloumns().get(0));
				}
			}
		}

		return new LinkedList<Index>(indexMap.values());
	}

	@Override
	public List<Trigger> getTriggers(String tableName) {
		List<Trigger> triggers = new LinkedList<Trigger>();
		List<Map<String, String>> result = query(getQuerySql(QUERY_TRIGGER),
				new String[] { tableName });
		for (Map<String, String> map : result) {
			triggers.add(converter.convertMap2Trigger(map));
		}
		return triggers;
	}

	@Override
	public Connection openConnection() {
		try {
			closeConnection();

			Class.forName(getDriver());

			Properties props = new Properties();
			props.put("remarksReporting", "true");
			props.put("user", dataSource.getUsername());
			props.put("password", dataSource.getPassword());
			if (dataSource.getDbType().equalsIgnoreCase("oracle")) {
				connection = DriverManager.getConnection(getUrl(dataSource.getHost(), Integer.parseInt(dataSource.getPort()), dataSource.getDbName()) , props);
			} else if (dataSource.getDbType().equalsIgnoreCase("mysql")) {
				connection = DriverManager.getConnection(getUrl(dataSource.getHost(), Integer.parseInt(dataSource.getPort()), dataSource.getDbName()) + "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT", props);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionCast.cast(GenerateCode.GENERATE_OPEN_CONNECTION_EXCEPTION);
		}
		return connection;
	}

	@Override
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				ExceptionCast.cast(GenerateCode.GENERATE_CLOSE_CONNECTION_EXCEPTION);
			}
		}
	}

	/**
     * 获取驱动类字符串描述值，抽象方法，由子类实现
     * 
     * @return
     */
    abstract protected String getDriver() ;

    /**
     * 获取要连接的数据库url，抽象方法，由子类实现
     * 
     * @param host
     * @param port
     * @param dbName
     */
    abstract protected String getUrl(String host, int port, String dbName) ;

    /**
     * 获取sql查询语句
     *
     * @param sqlKey
     * @return
     */
    abstract protected String getQuerySql(String sqlKey);

    /**
     * 关闭jdbc资源对象
     * 
     * @param rs
     * @param stmt
     */
    protected void close(ResultSet rs, Statement stmt) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
				ExceptionCast.cast(GenerateCode.GENERATE_CLOSE_CONNECTION_EXCEPTION);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                        stmt = null;
                    } catch (SQLException e) {
						ExceptionCast.cast(GenerateCode.GENERATE_CLOSE_CONNECTION_EXCEPTION);
                    }
                }
            }
        }
    }

    /**
     * @param converter the converter to set
     */
    public void setConverter(IMetaDataConverter converter) {
        this.converter = converter;
    }
}
