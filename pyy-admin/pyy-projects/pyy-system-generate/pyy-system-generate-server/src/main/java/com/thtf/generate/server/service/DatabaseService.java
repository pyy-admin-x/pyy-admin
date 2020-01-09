package com.thtf.generate.server.service;


import com.thtf.generate.api.model.*;
import java.util.List;
import java.util.Map;

/**
 * 数据库元信息查询服务Service接口
 */
public interface DatabaseService {

    /**
     * 通用查询方法
     * @param dataSource
     * @param sql
     * @param params
     * @return
     */
    List<Map<String, String>> query(DataSource dataSource, String sql, String[] params);

    /**
     * 查询表集合
     * @param dataSource
     * @return
     */
    List<Table> getTables(DataSource dataSource);

    /**
     * 查询表的字段集
     * @param dataSource
     * @param tableName
     * @return
     */
    List<Column> getColumns(DataSource dataSource, String tableName);

    /**
     * 查询主键集
     * @param dataSource
     * @param tableName
     * @return
     */
    List<PrimaryKey> getPrimaryKeys(DataSource dataSource, String tableName);

    /**
     * 查询外键集
     * @param dataSource
     * @param tableName
     * @return
     */
    List<ForeignKey> getForeignKeys(DataSource dataSource, String tableName);

    /**
     * 查询索引集
     * @param dataSource
     * @param tableName
     * @return
     */
    List<Index> getIndexes(DataSource dataSource, String tableName);

    /**
     * 查询触发器集
     * @param dataSource
     * @param tableName
     * @return
     */
    List<Trigger> getTriggers(DataSource dataSource, String tableName);

    /**
     * 测试数据库是否可以连接
     * @param dataSource
     * @return
     */
    boolean canConnect(DataSource dataSource);
}
