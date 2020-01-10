//package com.thtf.gateway.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.thtf.common.auth.token.properties.TokenProperties;
//import com.thtf.common.auth.utils.JwtUtil;
//import com.thtf.common.core.response.CommonCode;
//import com.thtf.common.core.response.ResponseResult;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
///**
// * ---------------------------
// * 权限过滤器
// * ---------------------------
// * 作者：  pyy
// * 时间：  2020/1/10 15:22
// * 版本：  v1.0
// * ---------------------------
// */
//@Slf4j
//@Data
//@ConfigurationProperties("auth")
////@Component
//public class AuthFilter implements GlobalFilter, Ordered {
//    @Autowired
//    private TokenProperties tokenProperties;
//
//    /** 白名单路径 */
//    private String[] skipAuthUrls;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String url = exchange.getRequest().getURI().getPath();
//
//        // 跳过不需要验证的路径
//        if(null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)){
//            return chain.filter(exchange);
//        }
//
//        // 获取请求头信息authorization信息
//        final String authHeader = exchange.getRequest().getHeaders().getFirst(tokenProperties.tokenKey);
//        log.info("## authHeader= {}", authHeader);
//        ServerHttpResponse response = exchange.getResponse();
//        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(tokenProperties.tokenPrefix)) {
//            // 没有token
//            log.info("### 用户未登录，请先登录 ###");
//            return authErro(response, CommonCode.UNAUTHENTICATED);
//        } else {
//            // 有token, 截取有效token
//            final String token = authHeader.substring(7);
//            // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
//            JwtUtil.parseToken(token, tokenProperties.getBase64Secret());
//            // 通过就放行
//            return chain.filter(exchange);
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return -100;
//    }
//
//    /**
//     * 认证错误输出
//     * @param resp 响应对象
//     * @param commonCode 错误
//     * @return
//     */
//    private Mono<Void> authErro(ServerHttpResponse resp, CommonCode commonCode) {
//        resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
//        ResponseResult responseResult = new ResponseResult(commonCode);
//        String returnStr = JSON.toJSONString(responseResult);
//        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
//        return resp.writeWith(Flux.just(buffer));
//    }
//}
