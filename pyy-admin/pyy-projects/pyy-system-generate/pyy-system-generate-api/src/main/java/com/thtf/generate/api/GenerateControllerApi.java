package com.thtf.generate.api;

import com.thtf.common.core.response.ResponseResult;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.api.model.GenerateModel;
import com.thtf.generate.api.model.Table;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
@Api(value="代码自动生成接口",description = "代码自动生成管理，提供代码生成相关借款")
public interface GenerateControllerApi {

    @ApiOperation("测试数据源连接")
    @PostMapping("/testConnection")
    ResponseResult testConnection(@Valid @RequestBody DataSource dataSource);

    @ApiOperation("获取当前数据源下所有表信息")
    @PostMapping("/getTables")
    ResponseResult<List<Table>> getTables(@Valid @RequestBody DataSource dataSource);

    @ApiOperation("获取生成代码信息")
    @PostMapping("/getGenerateModel")
    ResponseResult<GenerateModel> getGenerateModel(@Valid @RequestBody GenerateModel generateModel);

    @ApiOperation("生成代码")
    @PostMapping("/generateModels")
    ResponseResult generateModels(@Valid @RequestBody GenerateModel generateModel);
}
