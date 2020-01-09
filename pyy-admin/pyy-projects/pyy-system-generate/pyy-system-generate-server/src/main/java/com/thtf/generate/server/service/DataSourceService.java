package com.thtf.generate.server.service;


import com.thtf.common.core.response.Pager;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.api.vo.DataSourceQueryConditionVO;

import java.util.List;

public interface DataSourceService {

    /**
     * 根据ID查询数据源
     * @param id
     * @return
     */
    DataSource findById(String id);

    /**
     * 数据源列表查询
     * @param conditionVO
     * @return
     */
    List<DataSource> findList(DataSourceQueryConditionVO conditionVO) ;

    /**
     * 数据源列表分页查询
     * @param page
     * @param size
     * @param conditionVO
     * @return
     */
    Pager<DataSource> findList(int page, int size, DataSourceQueryConditionVO conditionVO) ;

    /**
     * 添加数据源
     * @param DataSource
     * @return
     */
    DataSource add(DataSource DataSource);

    /**
     * 修改数据源
     * @param id
     * @param DataSource
     * @return
     */
    DataSource update(String id, DataSource DataSource);

    /**
     * 根据ID删除数据源
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 测试数据库连接
     * @param dataSource
     */
    void testConnection(DataSource dataSource);
}
