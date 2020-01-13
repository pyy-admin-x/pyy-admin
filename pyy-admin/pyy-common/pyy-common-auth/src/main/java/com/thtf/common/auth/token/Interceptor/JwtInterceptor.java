package com.thtf.common.auth.token.Interceptor;


import com.alibaba.fastjson.JSON;
import com.thtf.common.auth.token.annonation.RequirePermissions;
import com.thtf.common.core.properties.JwtProperties;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.ResponseCode;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.core.utils.JwtUtil;
import com.thtf.common.core.utils.LoginUserUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ========================
 * JWT鉴权拦截器
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/11/14 14:45
 * Version: v1.0
 * ========================
 */
@Slf4j
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    public static final String USER_CLAIMS = "user_claims";

    /** 权限标识 */
    private static final String PERMISSION_KEY = "permissions";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        log.info("### 请求地址：{} ###", request.getRequestURL());
        HandlerMethod handlerMethod = null;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        }
        // 获取@RequirePermissions注解,没有这个注解接口直接访问
        RequirePermissions requirePermissions = handlerMethod.getMethodAnnotation(RequirePermissions.class);
        if (requirePermissions == null || StringUtils.isEmpty(requirePermissions.value())) {
            return true;
        }
        // 请求中获取key为Authorization的头信息
        String authorization = request.getHeader(jwtProperties.getTokenKey());
        log.info("### authorization={} ###", authorization);
        // 判断请求头信息是否为空，或者是否已Bearer开头
        if (!StringUtils.isEmpty(authorization)  && authorization.startsWith(jwtProperties.getTokenPrefix())) {
            // 前后端约定头信息内容以 Bearer+空格+token 形式组成
            String token = authorization.replace(jwtProperties.getTokenPrefix(), "");
            // 验证token，并返回claims
            Claims claims = (Claims) JwtUtil.parseToken(token, jwtProperties.getBase64Secret());
            if (claims != null) {
                // 绑定上下文
                LoginUserUtil.setCurrentUser(JwtUtil.getLoginUser(claims));
                // 通过claims获取到当前用户的可访问API权限字符串
                String apis = (String) claims.get(PERMISSION_KEY); //sys:dept:add
                // 获取当前请求接口授权地址
                String apiCode = requirePermissions.value();
                // 判断当前用户是否具有相应的请求权限
                if (apis.contains(apiCode)) {
                    request.setAttribute(USER_CLAIMS, claims);
                    log.info("### 鉴权通过，放行请求 ###");
                    return true;
                }
                log.info("### 权限不足，禁止访问 ###");
                responseError(response, CommonCode.UNAUTHORISE);
            }
        }
        log.info("### 用户未登录，请先登录 ###");
        responseError(response, CommonCode.UNAUTHENTICATED);
        return false;
    }

    /**
     * 非法请求响应
     * @param resp
     */
    private void responseError(HttpServletResponse resp, ResponseCode code) {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            ResponseResult result = new ResponseResult(code);
            out.append(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error("返回Response信息出现IOException异常:" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
