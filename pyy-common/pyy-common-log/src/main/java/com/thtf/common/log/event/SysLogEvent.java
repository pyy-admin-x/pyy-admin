package com.thtf.common.log.event;

import com.thtf.base.api.vo.SysLogSaveOrUpdateVO;
import org.springframework.context.ApplicationEvent;

/**
 * ---------------------------
 * 系统日志事件
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 11:55
 * 版本：  v1.0
 * ---------------------------
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogSaveOrUpdateVO sysLog) {
        super(sysLog);
    }
}
