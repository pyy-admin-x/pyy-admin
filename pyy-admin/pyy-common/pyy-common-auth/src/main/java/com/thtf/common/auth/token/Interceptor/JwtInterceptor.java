package com.thtf.common.auth.token.Interceptor;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /** 管理员角色标识 */
    private static final String ADMIN_ROLE_CODE = "ADMIN";

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
        String token = request.getHeader(jwtProperties.getTokenKey());
        log.info("### token={} ###", token);
        // 判断请求头信息是否为空，或者是否已Bearer开头
        if (!StringUtils.isEmpty(token) ) {
            // 验证token，并返回claims
            Claims claims = JwtUtil.parseToken(token, jwtProperties.getBase64Secret());
            if (claims != null) {
                // 绑定上下文
                LoginUserUtil.setCurrentUser(JwtUtil.getLoginUser(claims));
                // 通过claims获取到当前用户角色和权限信息
                Map<String, Object> roles = (HashMap<String, Object>)claims.get(jwtProperties.getRolePremissionKey());
                if (CollUtil.isNotEmpty(roles)) {
                    List<HashMap<String, Object>> roleList = (List<HashMap<String, Object>>)roles.get("roleList");
                    List<String> permissionList = (List<String>) roles.get("permissionList");
                    List<String> roleCodeList = roleList.stream().map(role -> role.get("code").toString()).collect(Collectors.toList());
                    if (roleCodeList.contains(ADMIN_ROLE_CODE)) {
                        log.info("### 当前角色为管理员，直接放行请求 ###");
                        return true;
                    }
                    // 获取当前请求接口授权地址
                    String apiCode = requirePermissions.value();
                    // 判断当前用户是否具有相应的请求权限
                    if (permissionList.contains(apiCode)) {
                        request.setAttribute(USER_CLAIMS, claims);
                        log.info("### 鉴权通过，放行请求 ###");
                        return true;
                    }
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
