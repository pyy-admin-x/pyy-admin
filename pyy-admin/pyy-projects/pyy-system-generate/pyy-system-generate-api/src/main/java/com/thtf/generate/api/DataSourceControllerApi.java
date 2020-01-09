package com.thtf.generate.api;

import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.api.vo.DataSourceQueryConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ========================
 * 代码自动生成 数据源管理接口
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/12/10 14:57
 * Version: v1.0
 * ========================
 */
@Api(value="代码自动生成 数据源管理接口",description = "代码自动生成 数据源管理接口，提供数据源的管理、查询接口")
@RequestMapping("/dataSource")
public interface DataSourceControllerApi {
    @ApiOperation("查询数据源列表")
    @GetMapping("/list")
    ResponseResult<List<DataSource>> findList(DataSourceQueryConditionVO conditionVO) ;

    @ApiOperation("分页查询数据源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码", required=true, paramType="query", dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数", required=true, paramType="query", dataType="int")
    })
    @GetMapping("/page")
    ResponseResult<Pager<DataSource>> findList(@RequestParam("page") int page, @RequestParam("size") int size, DataSourceQueryConditionVO conditionVO) ;

    @ApiOperation("添加数据源")
    @PostMapping()
    ResponseResult<DataSource> add(@RequestBody DataSource dataSource);

    @ApiOperation("根据id查询数据源")
    @GetMapping("/{id}")
    ResponseResult<DataSource> findById(@PathVariable("id") String id);

    @ApiOperation("编辑数据源")
    @PutMapping("/{id}")
    ResponseResult<DataSource> edit(@PathVariable("id") String id, @RequestBody DataSource dataSource);

    @ApiOperation("删除数据源")
    @DeleteMapping("/{id}")
    ResponseResult delete(@PathVariable("id") String id);


}
