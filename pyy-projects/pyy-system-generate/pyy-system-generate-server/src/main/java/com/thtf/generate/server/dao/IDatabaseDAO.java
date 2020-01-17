package com.thtf.generate.server.dao;

import com.thtf.generate.api.model.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * ========================
 * 数据库元信息查询接口
 * Author：pyy
 * Date：2019-9-25 16:23:28
 * Version: v1.0
 * ========================
 */
public interface IDatabaseDAO {
	
	/**
	 * 通用查询方法
	 * @param sql
	 * @param params
	 * @return
	 */
	List<Map<String, String>> query(String sql, String[] params);

	/**
	 * 查询表集合
	 * @return
	 */
	List<Table> getTables();

	/**
	 * 查询表的字段集
	 * @param tableName
	 * @return
	 */
	List<Column> getColumns(String tableName) ;

	/**
	 * 查询主键集
	 * @param tableName
	 * @return
	 */
	List<PrimaryKey> getPrimaryKeys(String tableName) ;
	
	/**
     * 查询外键集
     * @param tableName
     * @return
     */
	List<ForeignKey> getForeignKeys(String tableName);
	
	/**
     * 查询索引集
     * @return 
     */
	List<Index> getIndexes(String tableName);
	
	/**
     * 查询触发器集
     * @param tableName
     * @return
     */
	List<Trigger> getTriggers(String tableName);
	
	/**
     * 打开数据库连接
     */
	Connection openConnection() ;
	
	/**
     * 关闭数据库连接
     */
	void closeConnection();
}
