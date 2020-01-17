package com.thtf.generate.server.service.impl;

import com.thtf.generate.api.model.*;
import com.thtf.generate.server.dao.DatabaseDAOFactory;
import com.thtf.generate.server.dao.IDatabaseDAO;
import com.thtf.generate.server.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {
    @Override
    public List<Map<String, String>> query(DataSource dataSource, String sql, String[] params) {
        List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
        if(dataSource == null) {
            return maps;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            maps = dao.query(sql, params);
            dao.closeConnection();
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表信息耗时：{} 毫秒 ###",(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    @Override
    public List<Table> getTables(DataSource dataSource) {
        List<Table> tables = new ArrayList<Table>();
        if(dataSource == null) {
            return tables;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            tables = dao.getTables();
            dao.closeConnection();
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表信息耗时：{} 毫秒 ###",(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public List<Column> getColumns(DataSource dataSource, String tableName) {
        List<Column> columns = new ArrayList<Column>();
        if(dataSource == null) {
            return columns;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            columns = dao.getColumns(tableName);
            dao.closeConnection();
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表列信息耗时：{} 毫秒 ###",(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    @Override
    public List<PrimaryKey> getPrimaryKeys(DataSource dataSource, String tableName) {
        List<PrimaryKey> primaryKeys = new ArrayList<PrimaryKey>();
        if(dataSource == null) {
            return primaryKeys;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            primaryKeys = dao.getPrimaryKeys(tableName);
            dao.closeConnection();
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表主键信息耗时：{} 毫秒 ###",(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return primaryKeys;
    }

    @Override
    public List<ForeignKey> getForeignKeys(DataSource dataSource, String tableName) {
        List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
        if(dataSource == null) {
            return foreignKeys;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            foreignKeys = dao.getForeignKeys(tableName);
            dao.closeConnection();
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表外键信息耗时：{} 毫秒 ###",(end - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foreignKeys;
    }

    @Override
    public List<Index> getIndexes(DataSource dataSource, String tableName) {
        List<Index> indexes = new ArrayList<Index>();
        if(dataSource == null) {
            return indexes;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            indexes = dao.getIndexes(tableName);
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表索引信息耗时：{} 毫秒 ###",(end - start));
            dao.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indexes;
    }

    @Override
    public List<Trigger> getTriggers(DataSource dataSource, String tableName) {
        List<Trigger> trigger = new ArrayList<Trigger>();
        if(dataSource == null) {
            return trigger;
        }
        try {
            IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
            long start = System.currentTimeMillis();
            dao.openConnection();
            trigger = dao.getTriggers(tableName);
            long end = System.currentTimeMillis();
            log.info("### 反向获取数据库表触发器信息耗时：{} 毫秒 ###",(end - start));
            dao.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trigger;
    }

    @Override
    public boolean canConnect(DataSource dataSource) {
        IDatabaseDAO dao = DatabaseDAOFactory.getDAO(dataSource);
        if (dao == null) {
            return false;
        }
        try {
            dao.openConnection();
            log.info("### 数据库连接成功! ###");
            return true;
        } catch (Exception e) {
            log.info("### 数据库连接失败,请检查端口号、用户名或密码 !###");
        } finally {
            try {
                dao.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
