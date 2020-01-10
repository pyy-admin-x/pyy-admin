package com.thtf.common.auth.token.service.impl;

import com.thtf.common.auth.token.properties.TokenProperties;
import com.thtf.common.auth.token.service.TokenService;
import com.thtf.common.auth.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ========================
 * 基于JWT的TokenService实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/11/18 10:47
 * Version: v1.0
 * ========================
 */
@Slf4j
@Component
public class JwtTokenServiceImpl implements TokenService {
    @Override
    public String createToken(String userId, String username, Map<String, Object> extAttribute, TokenProperties tokenProperties) {
       return JwtUtil.createToken(userId, username, extAttribute, tokenProperties);
    }

    @Override
    public Object parseToken(String token, String base64Security) {
        return JwtUtil.parseToken(token, base64Security);
    }
}
