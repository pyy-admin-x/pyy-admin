package com.thtf.common.auth.utils;

import com.thtf.common.auth.core.SecurityUser;
import lombok.experimental.UtilityClass;

/**
 * ---------------------------
 * 安全服务工具类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:11
 * 版本：  v1.0
 * ---------------------------
 */
@UtilityClass
public class UserSecurityUtil {

    /**
     * 获取用户
     */
    public SecurityUser getUser() {
        return new SecurityUser();
    }


    /**
     * 获取用户ID
     * @return userId
     */
    public String getUserId() {
        return "root";
    }

    /**
     * 获取用户名称
     * @return username
     */
    public String getUsername() {
        return "root";
    }
}
