package com.thtf.common.log;

import com.thtf.common.log.aspect.SysLogAspect;
import com.thtf.common.log.listener.SysLogListener;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ---------------------------
 * 当web项目引入此依赖时，自动配置对应的内容 初始化log的事件监听与切面配置
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@EnableAsync
@Configuration
@ConditionalOnWebApplication
@EnableFeignClients({"com.thtf.common.log.feign"})
public class LogAutoConfiguration {
    @Bean
    public SysLogListener sysLogListener() {
        return new SysLogListener();
    }

    @Bean
    public SysLogAspect sysLogAspect(ApplicationEventPublisher publisher) {
        return new SysLogAspect(publisher);
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
