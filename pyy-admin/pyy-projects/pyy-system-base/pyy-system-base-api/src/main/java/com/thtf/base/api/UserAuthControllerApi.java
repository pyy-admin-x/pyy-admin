package com.thtf.base.api;

import com.thtf.base.api.vo.SysUserQueryConditionVO;
import com.thtf.base.api.vo.SysUserSaveOrUpdateVO;
import com.thtf.base.api.vo.SysUserVO;
import com.thtf.common.core.model.LoginUser;
import com.thtf.common.core.response.Pager;
import com.thtf.common.core.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * ---------------------------
 * 用户 (UserAuthControllerApi)接口
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020-01-10 16:06:25
 * 版本：  v1.0
 * ---------------------------
 */
@Api(tags = "用户认证模块")
public interface UserAuthControllerApi {

    /**
     * 获取图形验证码
     * @return
     */
    @ApiOperation(value = "获取图形验证码", notes = "获取图形（算数）验证码  1:字母+数字PNG类型 2:字母+数字GIF类型 3:中文类型 4:中文gif类型 5:算术类型")
    @ApiImplicitParam(name = "type", value = "验证类型", required = true, dataType = "String", paramType = "query")
    @GetMapping("/auth/imgCode")
    ResponseResult<String> getImgCode(@Valid @NotBlank(message = "验证码类型不能为空") @RequestParam("type") String type);

    /**
     * 用户登录
     * @param user
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "LoginUser", paramType = "body")
    @PostMapping("/auth/login")
    ResponseResult<String> login(@Valid @RequestBody LoginUser user);

    /**
     * 退出登录
     * @param request
     * @return
     */
    @ApiOperation("退出登录")
    @GetMapping("/auth/logout")
    ResponseResult logout(HttpServletRequest request);

}
