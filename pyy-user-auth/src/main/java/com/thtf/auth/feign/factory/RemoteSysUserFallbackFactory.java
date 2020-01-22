package com.thtf.auth.feign.factory;

import com.thtf.auth.feign.RemoteSysUserService;
import com.thtf.auth.feign.fallback.RemoteSysUserFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * ---------------------------
 * 用户服务Fallback工厂
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@Component
public class RemoteSysUserFallbackFactory implements FallbackFactory<RemoteSysUserService> {
    @Override
    public RemoteSysUserService create(Throwable throwable) {
        return new RemoteSysUserFallbackImpl(throwable);
    }
}
