package com.thtf.generate.server.controller;

import com.thtf.common.core.response.ResponseResult;
import com.thtf.generate.api.GenerateControllerApi;
import com.thtf.generate.api.model.DataSource;
import com.thtf.generate.api.model.GenerateModel;
import com.thtf.generate.api.model.Table;
import com.thtf.generate.server.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class GenerateController implements GenerateControllerApi {

    @Autowired
    private GenerateService generateService;

    @Override
    public ResponseResult testConnection(DataSource dataSource) {
        boolean success = generateService.testConnection(dataSource);
        if (success) {
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }

    @Override
    public ResponseResult<List<Table>> getTables(DataSource dataSource) {
        return ResponseResult.SUCCESS(generateService.getTables(dataSource));
    }

    @Override
    public ResponseResult<GenerateModel> getGenerateModel(GenerateModel generateModel) {
        return ResponseResult.SUCCESS(generateService.getGenerateModel(generateModel));
    }

    @Override
    public ResponseResult generateModels(GenerateModel generateModel) {
        boolean success = generateService.generateModels(generateModel);
        if (success) {
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }
}
