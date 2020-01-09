package com.thtf.common.core.response;

/**
 * ---------------------------
 * 通用响应状态码
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/26 16:25
 * 版本：  v1.0
 * ---------------------------
 */
public enum CommonCode implements ResponseCode{
    SUCCESS(200,"操作成功！"),
    FAIL(500,"未知异常，请联系管理员！"),
    UNAUTHENTICATED(10001,"此操作需要登陆系统！"),
    UNAUTHORISE(10002,"权限不足，无权操作！"),
    INVALID_PARAM(10003,"非法参数！"),
    INVALID_REQUEST_METHOD( 10004, "请求方式错误！"),
    SERVER_ERROR(99999,"抱歉，系统繁忙，请稍后重试！");;

    //操作代码
    private int code;

    //提示信息
    private String message;

    CommonCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
