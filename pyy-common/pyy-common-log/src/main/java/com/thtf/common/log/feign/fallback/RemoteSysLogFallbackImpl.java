package com.thtf.common.log.feign.fallback;

import com.thtf.base.api.vo.SysLogQueryConditionVO;
import com.thtf.base.api.vo.SysLogSaveOrUpdateVO;
import com.thtf.base.api.vo.SysLogVO;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.log.feign.RemoteSysLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ---------------------------
 * 日志服务Fallback实现类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@AllArgsConstructor
public class RemoteSysLogFallbackImpl implements RemoteSysLogService {

    private final Throwable throwable;

    @Override
    public ResponseResult<SysLogVO> findById(String logId) {
        log.error("feign 日志详情查询失败:{}", logId, throwable);
        return null;
    }

    @Override
    public ResponseResult<Pager<SysLogVO>> findList(int page, int size, SysLogQueryConditionVO queryConditionVO) {
        log.error("feign 日志查询失败:{}", queryConditionVO, throwable);
        return null;
    }

    @Override
    public ResponseResult<SysLogVO> add(SysLogSaveOrUpdateVO sysLogSaveOrUpdateVO) {
        log.error("feign 保存日志失败:{}", sysLogSaveOrUpdateVO, throwable);
        return null;
    }

    @Override
    public ResponseResult delete(String logId) {
        log.error("feign 删除日志失败:{}", logId, throwable);
        return null;
    }
}
