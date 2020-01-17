package com.thtf.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * ========================
 * JWT配置类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/11/14 9:43
 * Version: v1.0
 * ========================
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@PropertySource("classpath:jwt.properties")
@Component
public class JwtProperties {
    /** token key */
    private String tokenKey = "Authorization";

    /** token value 前缀 */
    private String tokenPrefix = "Bearer ";

    /** 用户角色权限标识 */
    private String rolePremissionKey = "role_permissions";

    /** 代表这个JWT的签发主体 */
    private String clientId;

    /** 签名密钥 */
    private String base64Secret;

    /** 代表这个JWT的接收对象 */
    private String name;

    /** 失效时间：单位秒 */
    private int expiresSecond;
}
