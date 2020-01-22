package com.thtf.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ---------------------------
 * web安全配置
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/20 14:30
 * 版本：  v1.0
 * ---------------------------
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PyyUserDetailsService pyyUserDetailsService;

    /**
     * security的鉴权排除的url列表
     */
    private static final String[] EXCLUDED_AUTH_PAGES = {
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/*/v2/api-docs",
            "/v2/api-docs",
            "/api/socket/**",
            "/log",
            "/actuator/**",
            "/*/api-docs",
            "/css/**", "/js/**", "/images/**", "/webjars/**", "**/favicon.ico",
            "/*.html", "/**/*.html", "/**/*.css", "/**/*.js",
            "/auth/imgCode/**", "/auth/login/**", "/oauth/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 认证管理器配置（密码授权模式必须）
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 安全拦截机制（最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(EXCLUDED_AUTH_PAGES).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }

    /**
     * 这个方法是去数据库查询用户的密码，做权限验证
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void config(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //设置UserDetailsService以及密码规则
        authenticationManagerBuilder
                .userDetailsService(pyyUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    public static void main(String[] args) {
        String secret = new BCryptPasswordEncoder().encode("secret");
        System.out.println(secret);
    }
}
