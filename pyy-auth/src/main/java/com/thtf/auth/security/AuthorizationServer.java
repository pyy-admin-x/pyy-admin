package com.thtf.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * ---------------------------
 * OAuth2.0认证服务器配置
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/20 10:54
 * 版本：  v1.0
 * ---------------------------
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 1.配置客户端详情服务配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()// 使用in-memory 存储
                .withClient("client_abc")// client_id
                .secret(new BCryptPasswordEncoder().encode("secret")) // 密钥
                .resourceIds("resource_abc")// 资源ID
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")//该client允许的授权类型
                .scopes("all")// 允许的授权范围（自定义字符串标识）
                .autoApprove(false) // 是否显示用户认证对话框：false 显示 true不显示
                .redirectUris("http://www.baidu.com"); // 验证回调地址
    }

    // 2.令牌管理服务配置
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); // 客户端详情服务
        service.setSupportRefreshToken(true); // 支持刷新令牌 refresh_token
        service.setTokenStore(tokenStore);  // 令牌存储策略
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

    // 3.令牌访问端点配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // 认证管理器
                .authorizationCodeServices(authorizationCodeServices) // 授权码服务
                .tokenServices(tokenService()) // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    // 4.令牌端口的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") //  tokenkey这个endpoint（/oauth/token_key）当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个 endpoint完全公开。
                .checkTokenAccess("permitAll()") // checkToken这个endpoint（/oauth/check_token）完全公开
                .allowFormAuthenticationForClients(); // 允许表单认证（申请令牌）
    }


    @Bean public AuthorizationCodeServices authorizationCodeServices() {
        // 设置授权码模式的授权码如何存取，暂时采用内存方式(后面改为JWT)
        return new InMemoryAuthorizationCodeServices();
    }
}
