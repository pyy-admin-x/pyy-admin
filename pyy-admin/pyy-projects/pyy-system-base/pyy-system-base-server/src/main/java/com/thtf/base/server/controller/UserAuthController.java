package com.thtf.base.server.controller;

import com.thtf.base.api.UserAuthControllerApi;
import com.thtf.base.api.vo.LoginUserVO;
import com.thtf.base.server.service.SysUserService;
import com.thtf.common.core.model.ProfileUser;
import com.thtf.common.core.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ---------------------------
 * 用户 (UserAuthController)控制器
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/13 15:25
 * 版本：  v1.0
 * ---------------------------
 */
@RestController
public class UserAuthController implements UserAuthControllerApi {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public ResponseResult<String> getImgCode(String type) {
        return ResponseResult.SUCCESS(sysUserService.getImgCode(type));
    }

    @Override
    public ResponseResult<String> login(LoginUserVO loginUserVO) {
        return ResponseResult.SUCCESS(sysUserService.login(loginUserVO));
    }

    @Override
    public ResponseResult logout(HttpServletRequest request) {
        return null;
    }
}
