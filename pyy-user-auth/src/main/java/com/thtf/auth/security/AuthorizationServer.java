package com.thtf.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

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
    @Qualifier("jdbcClientDetailsService")
    private ClientDetailsService jdbcClientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // 1.配置客户端详情服务配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    // 2.配置令牌访问端点和令牌服务
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // 认证管理器（密码模式必须）
                .authorizationCodeServices(authorizationCodeServices) // 授权码服务（授权码模式必须）
                .tokenServices(tokenService()) // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    // 3.配置令牌端口的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") //  tokenkey这个endpoint（/oauth/token_key）当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个 endpoint完全公开。
                .checkTokenAccess("permitAll()") // checkToken这个endpoint（/oauth/check_token）完全公开
                .allowFormAuthenticationForClients(); // 允许表单认证（申请令牌）
    }

    // 令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(jdbcClientDetailsService); // 客户端详情服务
        service.setSupportRefreshToken(true); // 支持刷新令牌 refresh_token
        service.setTokenStore(tokenStore);  // 令牌存储策略

        // 令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

    // 授权码服务（授权码模式必须）
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        // 设置授权码模式的授权码如何存取，暂时采用JDBC存储
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    // 将客户端的信息存储到数据库（从数据库获取客户端信息）
    @Bean("jdbcClientDetailsService")
    @Primary
    public ClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

}
