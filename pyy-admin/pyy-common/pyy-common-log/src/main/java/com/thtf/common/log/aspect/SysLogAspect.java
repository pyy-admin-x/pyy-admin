package com.thtf.common.log.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.thtf.base.api.vo.SysLogSaveOrUpdateVO;
import com.thtf.common.auth.utils.UserSecurityUtil;
import com.thtf.common.core.constant.PyyConstant;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.log.event.SysLogEvent;
import com.thtf.common.log.util.LogUtil;
import com.thtf.common.log.util.ip.utils.IPUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * ---------------------------
 * 系统日志切面
 *
 * ①切面注解得到请求数据 -> ②发布监听事件 -> ③异步监听日志入库
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 11:55
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class SysLogAspect {

    private ApplicationEventPublisher publisher;

    /**
     * log实体类
     **/
    private ThreadLocal<SysLogSaveOrUpdateVO> sysLogThreadLocal = new ThreadLocal<>();

    /**
     * 事件发布是由ApplicationContext对象管控的，我们发布事件前需要注入ApplicationContext对象调用publishEvent方法完成事件发布
     * @param publisher
     */
    public SysLogAspect(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /***
     * 定义controller切入点拦截规则，拦截@Log注解的方法
     */
    @Pointcut("@annotation(com.thtf.common.log.annotation.Log)")
    public void sysLogAspect() {

    }

    /***
     * 拦截控制层的操作日志
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Before(value = "sysLogAspect()")
    public void recordLog(JoinPoint joinPoint) throws Throwable {
        SysLogSaveOrUpdateVO sysLog = new SysLogSaveOrUpdateVO();
        // 将当前实体保存到threadLocal
        sysLogThreadLocal.set(sysLog);
        // 开始时间
        long beginTime = Instant.now().toEpochMilli();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setUsername(UserSecurityUtil.getUsername());
        sysLog.setActionUrl(URLUtil.getPath(request.getRequestURI()));
        sysLog.setStartTime(new Date());
        String requestIp = ServletUtil.getClientIP(request);
        // 请求IP
        sysLog.setRequestIp(requestIp);
        // IP归属地
        sysLog.setIpLocation(IPUtil.getCityInfo(requestIp));
        sysLog.setRequestMethod(request.getMethod());
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 浏览器
        sysLog.setBrowser(userAgent.getBrowser().getName());
        // 操作系统
        sysLog.setOperatingSystem(userAgent.getOperatingSystem().getName());
        //访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();
        // 类名
        sysLog.setClassPath(joinPoint.getTarget().getClass().getName());
        //获取执行的方法名
        String name = joinPoint.getSignature().getName();
        sysLog.setActionMethod(name);
        sysLog.setFinishTime(new Date());
        // 参数
        sysLog.setParams(Arrays.toString(args));
        // 获取操作类型
        sysLog.setOperateType(LogUtil.getOperateType(name));
        sysLog.setDescription(LogUtil.getControllerMethodDescription(joinPoint));
        long endTime = Instant.now().toEpochMilli();
        sysLog.setConsumingTime(endTime - beginTime);

    }

    /**
     * 返回通知
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        //得到当前线程的log对象
        SysLogSaveOrUpdateVO sysLog = sysLogThreadLocal.get();
        // 处理完请求，返回内容
        ResponseResult r = Convert.convert(ResponseResult.class, ret);
        if (r.getCode() == 200) {
            // 正常返回
            sysLog.setType(PyyConstant.LOG_TYPE_OPERATION);
        } else {
            sysLog.setType(PyyConstant.LOG_TYPE_EXCEPTION);
            sysLog.setExDetail(r.getMessage());
        }
        // 发布事件
        publisher.publishEvent(new SysLogEvent(sysLog));
        //移除当前log实体
        sysLogThreadLocal.remove();
    }

    /**
     * 异常通知
     *
     * @param e
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        //得到当前线程的log对象
        SysLogSaveOrUpdateVO sysLog = sysLogThreadLocal.get();
        // 异常
        sysLog.setType(PyyConstant.LOG_TYPE_EXCEPTION);
        // 异常对象
        sysLog.setExDetail(LogUtil.getStackTrace(e));
        // 异常信息
        sysLog.setExDesc(e.getMessage());
        // 发布事件
        publisher.publishEvent(new SysLogEvent(sysLog));
        // 移除当前log实体
        sysLogThreadLocal.remove();
    }

}
