package com.thtf.generate.server.controller;

import com.thtf.common.auth.token.annonation.RequirePermissions;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.log.annotation.Log;
import com.thtf.generate.api.DataSourceControllerApi;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.api.vo.DataSourceQueryConditionVO;
import com.thtf.generate.server.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ---------------------------
 * 代码自动生成数据源管理Controller
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019-12-13 14:30:05
 * 版本：  v1.0
 * ---------------------------
 */
@RestController
public class DataSourceController implements DataSourceControllerApi {

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    @RequirePermissions("generate:datasource:find")
    public ResponseResult<List<DataSource>> findList(DataSourceQueryConditionVO conditionVO) {
        return ResponseResult.SUCCESS(dataSourceService.findList(conditionVO));
    }

    @Override
    @RequirePermissions("generate:datasource:find")
    public ResponseResult<Pager<DataSource>> findList(int page, int size, DataSourceQueryConditionVO conditionVO) {
        return ResponseResult.SUCCESS(dataSourceService.findList(page, size, conditionVO));
    }

    @Override
    @Log(desc = "添加数据源")
    @RequirePermissions("generate:datasource:add")
    public ResponseResult<DataSource> add(DataSource dataSource) {
        return ResponseResult.SUCCESS(dataSourceService.add(dataSource));
    }

    @Override
    @RequirePermissions("generate:datasource:find")
    public ResponseResult<DataSource> findById(String id) {
        return ResponseResult.SUCCESS(dataSourceService.findById(id));
    }

    @Override
    @Log(desc = "修改数据源")
    @RequirePermissions("generate:datasource:update")
    public ResponseResult<DataSource> edit(String id, DataSource dataSource) {
        return ResponseResult.SUCCESS(dataSourceService.update(id, dataSource));
    }

    @Override
    @Log(desc = "删除数据源")
    @RequirePermissions("generate:datasource:del")
    public ResponseResult delete(String id) {
        dataSourceService.delete(id);
        return ResponseResult.SUCCESS();
    }
}
