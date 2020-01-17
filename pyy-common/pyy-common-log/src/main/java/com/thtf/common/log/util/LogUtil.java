package com.thtf.common.log.util;

import com.thtf.common.log.annotation.Log;
import com.thtf.common.core.constant.PyyConstant;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;


/**
 * ---------------------------
 * 日志工具类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 11:55
 * 版本：  v1.0
 * ---------------------------
 */
@UtilityClass
public class LogUtil {

    /***
     * 获取操作信息
     * @param point
     * @return
     */
    public String getControllerMethodDescription(JoinPoint point) throws Exception {
        // 获取连接点目标类名
        String targetName = point.getTarget().getClass().getName();
        // 获取连接点签名的方法名
        String methodName = point.getSignature().getName();
        // 获取连接点参数
        Object[] args = point.getArgs();
        // 根据连接点类的名字获取指定类
        Class targetClass = Class.forName(targetName);
        // 获取类里面的方法
        Method[] methods = targetClass.getMethods();
        String desc = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    desc = method.getAnnotation(Log.class).desc();
                    break;
                }
            }
        }
        return desc;
    }


    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 获取操作类型
     */
    public int getOperateType(String methodName) {
        if (methodName.startsWith("get")) {
            return PyyConstant.OPERATE_TYPE_QUERY;
        }
        if (methodName.startsWith("add")) {
            return PyyConstant.OPERATE_TYPE_ADD;
        }
        if (methodName.startsWith("update")) {
            return PyyConstant.OPERATE_TYPE_UPDATE;
        }
        if (methodName.startsWith("delete")) {
            return PyyConstant.OPERATE_TYPE_DELETE;
        }
        return PyyConstant.OPERATE_TYPE_QUERY;
    }
}
