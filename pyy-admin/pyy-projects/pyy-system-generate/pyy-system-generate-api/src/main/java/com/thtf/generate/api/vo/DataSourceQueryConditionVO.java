package com.thtf.generate.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ---------------------------
 * 数据源查询条件
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/30 10:30
 * 版本：  v1.0
 * ---------------------------
 */
@Data
public class DataSourceQueryConditionVO implements Serializable {
    private static final long serialVersionUID = 5742492986608879530L;

    @ApiModelProperty("数据源名称")
    private String name;

    @ApiModelProperty("数据库用户名")
    private String username;

    @ApiModelProperty("数据库名称")
    private String dbName;
}
