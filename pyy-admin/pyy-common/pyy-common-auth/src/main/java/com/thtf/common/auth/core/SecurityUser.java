package com.thtf.common.auth.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author 李号东
 * @Description 用户手机号和账号密码 身份权限认证类 登陆身份认证
 * @Date 2019-05-07 09:11
 * @Param
 * @return
 **/
@Setter
@Getter
public class SecurityUser {
    private String userId;
    private String username;
    private String password;
}
