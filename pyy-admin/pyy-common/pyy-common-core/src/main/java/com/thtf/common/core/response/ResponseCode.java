package com.thtf.common.core.response;


/**
 * 响应码接口
 */
public interface ResponseCode {
    // 操作代码
    int code();
    // 提示信息
    String message();
}
