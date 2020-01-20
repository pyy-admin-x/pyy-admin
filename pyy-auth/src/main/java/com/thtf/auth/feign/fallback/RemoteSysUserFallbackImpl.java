package com.thtf.auth.feign.fallback;

import com.thtf.auth.feign.RemoteSysUserService;
import com.thtf.base.api.vo.UserDetailsVO;
import com.thtf.common.core.response.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ---------------------------
 * 用户服务Fallback实现类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:31
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@AllArgsConstructor
public class RemoteSysUserFallbackImpl implements RemoteSysUserService {

    private final Throwable throwable;

    @Override
    public ResponseResult<UserDetailsVO> findByUsername(String username) {
        log.error("### feign 根据用户名查询用户信息失败：{} ###", username, throwable);
        return null;
    }
}
