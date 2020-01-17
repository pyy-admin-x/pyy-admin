package com.thtf.common.log.feign.factory;

import com.thtf.common.log.feign.RemoteSysLogService;
import com.thtf.common.log.feign.fallback.RemoteSysLogFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * ---------------------------
 * 日志服务Fallback工厂
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteSysLogService> {
    @Override
    public RemoteSysLogService create(Throwable throwable) {
        return new RemoteSysLogFallbackImpl(throwable);
    }
}
