package com.thtf.generate.server.enums;

import com.thtf.common.core.response.ResponseCode;
import lombok.ToString;

/**
 * ---------------------------
 * 代码自动生成系统业务响应码
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 16:02
 * 版本：  v1.0
 * ---------------------------
 */
@ToString
public enum GenerateCode implements ResponseCode {
    GENERATE_ADDDATASOURCE_EXISTSNAME(33001,"数据源已存在！"),
    GENERATE_DATASOURCE_NOT_FOUND(33002,"数据源数据未找到！"),
    GENERATE_QUERY_SQL_ISNULL(33003,"输入的sql查询语句为空！！"),
    GENERATE_OPEN_CONNECTION_EXCEPTION(33004,"连接创建失败！"),
    GENERATE_CLOSE_CONNECTION_EXCEPTION(33005,"连接销毁失败！"),
    GENERATE_CONNECTION_EXCEPTION(33006,"连接失败！"),
    GENERATE_GET_QUERY_SQL_EXCEPTION(33007,"获取sql查询出错！"),
    GENERATE_DATASOURCE_ADD_ERROR(33008,"数据源保存失败！"),
    GENERATE_DATASOURCE_UPDATE_ERROR(33009,"数据源修改失败！"),
    GENERATE_DATASOURCE_DEL_ERROR(33010,"数据源删除失败！"),
    ;
    //操作代码
    int code;
    //提示信息
    String message;

    GenerateCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
