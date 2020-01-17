package com.thtf.generate.server.dao.impl;

import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.server.dao.sql.DatabaseType;

import java.util.List;
import java.util.Map;

/**
 * MySQL数据库元信息查询
 */
public class MySql5DatabaseDAO extends CommonDatabaseDAOImpl {

	public MySql5DatabaseDAO(DataSource dataSource, DatabaseType dbType) {
		super(dataSource, dbType);
	}

	@Override
	protected String getQuerySql(String sqlKey){
		return super.getQuerySql(sqlKey);
	}

	@Override
	public List<Map<String, String>> query(String sql, String[] params) {
		String[] realParams;
		if (params == null) {
			realParams = new String[] {dataSource.getDbName()};
		} else {
			realParams = new String[params.length + 1];
			realParams[0] = dataSource.getDbName();
			for (int i = 0; i < params.length; i++) {
				realParams[i + 1] = params[i];
			}
		}
		return super.query(sql, realParams);
	}
}
