package com.thtf.generate.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thtf.common.auth.utils.UserSecurityUtil;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.Pager;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.api.vo.DataSourceQueryConditionVO;
import com.thtf.generate.server.enums.GenerateCode;
import com.thtf.generate.server.mapper.DataSourceMapper;
import com.thtf.generate.server.service.DataSourceService;
import com.thtf.generate.server.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private DatabaseService databaseService;

    @Override
    public DataSource add(DataSource dataSource) {
        if (dataSource == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 校验数据源是否存在，根据数据库类型、用户名、数据库名称查询
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getDbType, dataSource.getDbType())
                .eq(DataSource::getUsername, dataSource.getUsername())
                .eq(DataSource::getDbName, dataSource.getDbName());
        DataSource oldDataSource = dataSourceMapper.selectOne(queryWrapper);

        // 已存在则抛出异常
        if (oldDataSource != null) {
            ExceptionCast.cast(GenerateCode.GENERATE_ADDDATASOURCE_EXISTSNAME);
        }
        dataSource.setId(null);//添加站点主键由spring data 自动生成

        // 执行保存
        int row = dataSourceMapper.insert(dataSource);
        if (row != 1) {
            ExceptionCast.cast(GenerateCode.GENERATE_DATASOURCE_ADD_ERROR);
        }
        // 返回结果
        return dataSource;
    }

    @Override
    public DataSource update(String id, DataSource dataSource) {
        if (StringUtils.isBlank(id) || dataSource == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 根据ID查询数据源信息
        DataSource one = this.findById(id);
        if (one == null) {
            // 数据源数据未找到，抛出异常
            ExceptionCast.cast(GenerateCode.GENERATE_DATASOURCE_NOT_FOUND);
        }
        one.setName(dataSource.getName());
        one.setDbType(dataSource.getDbType());
        one.setHost(dataSource.getHost());
        one.setPort(dataSource.getPort());
        one.setUsername(dataSource.getUsername());
        one.setPassword(dataSource.getPassword());
        one.setDbName(dataSource.getDbName());

        // 校验数据源是否存在，根据数据库类型、用户名、数据库名称查询
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getDbType, dataSource.getDbType())
                .eq(DataSource::getUsername, dataSource.getUsername())
                .eq(DataSource::getDbName, dataSource.getDbName());
        DataSource oldDataSource = dataSourceMapper.selectOne(queryWrapper);

        // 已存在且不是本条数据则抛出异常
        if (oldDataSource != null && !oldDataSource.getId().equals(id)) {
            ExceptionCast.cast(GenerateCode.GENERATE_ADDDATASOURCE_EXISTSNAME);
        }
        // 执行更新
        int row = dataSourceMapper.updateById(one);
        if (row != 1) {
            ExceptionCast.cast(GenerateCode.GENERATE_DATASOURCE_UPDATE_ERROR);
        }
        // 返回更新后对象
        return one;
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        DataSource dataSource = dataSourceMapper.selectById(id);
        if (dataSource == null) {
            ExceptionCast.cast(GenerateCode.GENERATE_DATASOURCE_NOT_FOUND);
        }
        int row = dataSourceMapper.deleteById(id);
        if (row != 1) {
            ExceptionCast.cast(GenerateCode.GENERATE_DATASOURCE_DEL_ERROR);
        }
    }

    @Override
    public DataSource findById(String id) {
        if (StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        DataSource dataSource = dataSourceMapper.selectById(id);
        return dataSource;
    }

    @Override
    public List<DataSource> findList(DataSourceQueryConditionVO conditionVO) {
        if(conditionVO == null){
            conditionVO = new DataSourceQueryConditionVO();
        }
        // 自定义条件查询
        // 定义条件匹配器
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(conditionVO.getName()), DataSource::getName, conditionVO.getName())
                .like(StringUtils.isNotBlank(conditionVO.getDbName()),DataSource::getDbName, conditionVO.getDbName())
                .like(StringUtils.isNotBlank(conditionVO.getUsername()),DataSource::getUsername, conditionVO.getUsername());
        List<DataSource> dataSourceList = dataSourceMapper.selectList(queryWrapper);

        return dataSourceList;
    }

    @Override
    public Pager<DataSource> findList(int page, int size, DataSourceQueryConditionVO conditionVO) {
        if(conditionVO == null){
            conditionVO = new DataSourceQueryConditionVO();
        }
        // 自定义条件查询
        // 定义条件匹配器
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(conditionVO.getName()), DataSource::getName, conditionVO.getName())
                .like(StringUtils.isNotBlank(conditionVO.getDbName()),DataSource::getDbName, conditionVO.getDbName())
                .like(StringUtils.isNotBlank(conditionVO.getUsername()),DataSource::getUsername, conditionVO.getUsername());

        Page<DataSource> pageable = new Page(page,size);
        IPage<DataSource> pageResult = dataSourceMapper.selectPage(pageable, queryWrapper);
        Pager<DataSource> pager = new Pager();
        pager.setRecords(pageResult.getRecords());//数据列表
        pager.setTotal(pageResult.getTotal());//数据总记录数
        return pager;
    }

    @Override
    public void testConnection(DataSource dataSource) {
        boolean success = databaseService.canConnect(dataSource);
        if (!success) {
            ExceptionCast.cast(GenerateCode.GENERATE_CONNECTION_EXCEPTION);
        }
    }
}
