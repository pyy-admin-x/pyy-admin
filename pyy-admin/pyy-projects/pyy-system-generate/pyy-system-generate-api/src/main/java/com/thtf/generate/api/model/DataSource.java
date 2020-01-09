package com.thtf.generate.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.thtf.common.data.mybatis.model.CommonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_datasource")
public class DataSource extends CommonModel {
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String name;
    private String dbType;
    private String host;
    private String port;
    private String username;
    private String password;
    private String dbName;
}
