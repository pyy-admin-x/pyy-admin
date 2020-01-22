package com.thtf.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thtf.auth.service.UserAuthService;
import com.thtf.auth.vo.LoginUserVO;
import com.thtf.auth.vo.ValidateImgVO;
import com.thtf.base.api.model.SysUser;
import com.thtf.base.api.vo.ProfileVO;
import com.thtf.common.core.constant.CommonConstant;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.core.utils.*;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ---------------------------
 * 用户认证Controller
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/20 15:36
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@RestController
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    /**
     * 获取图形验证码
     * @return
     */
    @ApiOperation(value = "获取图形验证码", notes = "获取图形（算数）验证码  1:字母+数字PNG类型 2:字母+数字GIF类型 3:中文类型 4:中文gif类型 5:算术类型")
    @ApiImplicitParam(name = "type", value = "验证类型", required = true, dataType = "String", paramType = "query")
    @GetMapping("/auth/imgCode")
    ResponseResult<ValidateImgVO> getImgCode(@Valid @NotBlank(message = "验证码类型不能为空") @RequestParam("type") String type) {
        return ResponseResult.SUCCESS(userAuthService.getImgCode(type));
    }

    /**
     * 用户登录
     * @param loginUserVO
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParam(name = "loginUserVO", value = "用户对象", required = true, dataType = "LoginUserVO", paramType = "body")
    @PostMapping("/auth/login")
    ResponseResult<JSONObject> login(@Valid @RequestBody LoginUserVO loginUserVO, HttpServletRequest request) throws UnsupportedEncodingException {
        return ResponseResult.SUCCESS(userAuthService.login(loginUserVO, request));
    }

}
