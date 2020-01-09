package com.thtf.generate.server.exception;


import com.google.common.collect.ImmutableMap;
import com.thtf.common.core.exception.CustomException;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.ResponseCode;
import com.thtf.common.core.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * ---------------------------
 * 全局异常处理类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/26 17:43
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@RestControllerAdvice
public class ExceptionCatch {

    // 使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>, ResponseCode> EXCEPTIONS;

    // 使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResponseCode> builder = ImmutableMap.builder();

    static{
        // 在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
        builder.put(HttpRequestMethodNotSupportedException.class, CommonCode.INVALID_REQUEST_METHOD);
    }

    // 捕获 Exception异常
    @ExceptionHandler(Exception.class)
    public ResponseResult customException(Exception e) {
        log.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }

        final ResponseCode responseCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if (responseCode != null) {
            responseResult = new ResponseResult(responseCode);
        } else {
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }

        return responseResult;
    }

    /**
     * 参数错误异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseResult handleException(Exception e) {
        StringBuffer errorMsg = new StringBuffer();
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            BindingResult result = validException.getBindingResult();
            if (result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                errors.forEach(p ->{
                    FieldError fieldError = (FieldError) p;
                    errorMsg.append(fieldError.getDefaultMessage()).append(",");
                    log.error("### 请求参数错误："+fieldError.getObjectName()+" - field ["+fieldError.getField()+ "]: "+fieldError.getDefaultMessage()+" ###");
                });
            }
        } else if (e instanceof BindException) {
            BindException bindException = (BindException)e;
            if (bindException.hasErrors()) {
                log.error("### 请求参数错误: {} ###", bindException.getAllErrors());
            }
        }
        String message = StringUtils.removeEnd(errorMsg.toString(), ",");
        log.error("### 参数错误：{} ###", message);
        return new ResponseResult(CommonCode.INVALID_PARAM, message);
    }

    // 捕获 CustomException异常
    @ExceptionHandler(CustomException.class)
    public ResponseResult customException(CustomException e) {
        log.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        return new ResponseResult(e.getResponseCode());
    }

}
