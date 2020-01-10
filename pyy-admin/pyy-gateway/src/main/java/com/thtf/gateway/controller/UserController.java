package com.thtf.gateway.controller;

import com.thtf.common.auth.utils.JwtUtil;
import com.thtf.common.core.response.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ---------------------------
 * 认证Controller
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/10 15:51
 * 版本：  v1.0
 * ---------------------------
 */
@RestController
@RequestMapping("/auth")
public class UserController {

    @PostMapping("/login")
    public ResponseResult adminLogin(String username, String password) {
//
//
//        // 创建token
//        String token = JwtUtil.
//        log.info("### 登录成功, token={} ###", token);
//
//        // 将token放在响应头
//        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token);
//        // 将token响应给客户端
//        JSONObject result = new JSONObject();
//        result.put("token", token);
//        return Result.SUCCESS(result);
        return null;
    }

}
