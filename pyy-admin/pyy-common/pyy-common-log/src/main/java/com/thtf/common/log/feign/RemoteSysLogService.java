package com.thtf.common.log.feign;

import com.thtf.base.api.SysLogControllerApi;
import com.thtf.common.log.feign.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * ---------------------------
 * 日志服务Feign客户端
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@FeignClient(contextId = "remoteLogService", value = "pyy-system-base-server", fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteSysLogService extends SysLogControllerApi {

}
