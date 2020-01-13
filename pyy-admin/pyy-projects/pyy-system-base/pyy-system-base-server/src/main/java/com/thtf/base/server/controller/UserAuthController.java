package com.thtf.base.server.controller;

import com.thtf.base.api.UserAuthControllerApi;
import com.thtf.base.api.vo.ValidateImgVO;
import com.thtf.base.server.service.SysUserService;
import com.thtf.common.core.model.LoginUser;
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
        ValidateImgVO imgCode = sysUserService.getImgCode(type);
        return ResponseResult.SUCCESS(imgCode);
    }

    @Override
    public ResponseResult<String> login(LoginUser user) {
        return null;
    }

    @Override
    public ResponseResult logout(HttpServletRequest request) {
        return null;
    }
}
