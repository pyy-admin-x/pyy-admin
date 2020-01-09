package com.thtf.generate.server.dao.impl;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.server.dao.sql.DatabaseType;
import com.thtf.generate.server.enums.GenerateCode;
import com.thtf.generate.server.utils.Dom4jUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ========================
 * 通用数据库元信息查询类
 * Author：pyy
 * Date：2019-9-25 16:29:13
 * Version: v1.0
 * ========================
 */
@Slf4j
public class CommonDatabaseDAOImpl extends AbstractDatabasetDAOImpl {

    private static final String ELEMENT_DRIVER = DRIVER;
    private static final String ELEMENT_URL = URL;
    private static final String ELEMENT_SELECT = "select";
    private static final String ATTRIBUTE_NAME = "name";
    
    private String driver;
    private String url;
    private Map<String, String> selectMap = new HashMap<String, String>();
    
    private DatabaseType dbType;
    
    /**
     * @param dataSource
     */
    public CommonDatabaseDAOImpl(DataSource dataSource, DatabaseType dbType) {
        super(dataSource);
        
        setDbType(dbType);
        loadSqlXml(dbType);
    }

    /**
     * @param dbType the dbType to set
     */
    public void setDbType(DatabaseType dbType) {
        this.dbType = dbType;
        setConverter(dbType.getConverter());
    }
    
    @Override
    protected String getDriver()  {
        return driver;
    }

    @Override
    protected String getUrl(String host, int port, String dbName) {
        return String.format(url, host, port, dbName);
    }
    
    protected String getQuerySql(String sqlKey) {
        if (selectMap.containsKey(sqlKey)) {
            return selectMap.get(sqlKey);
        }
        log.error("获取sql查询出错sql={},数据库枚举类型为：{}", dbType, sqlKey);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadSqlXml(DatabaseType dbType) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(dbType.getFileName());
        Document doc = Dom4jUtils.getDocument(is);
        if (doc != null) {
            Element root = doc.getRootElement();
            
            driver = root.elementText(ELEMENT_DRIVER);
            url = root.elementText(ELEMENT_URL);
            
            for (Element selectElem : (List<Element>) root.elements(ELEMENT_SELECT)) {
                selectMap.put(selectElem.attributeValue(ATTRIBUTE_NAME), selectElem.getTextTrim());
            }
        }
        try {
            is.close();
        } catch (IOException e) {
            ExceptionCast.cast(GenerateCode.GENERATE_CLOSE_CONNECTION_EXCEPTION);
        }
    }
}
