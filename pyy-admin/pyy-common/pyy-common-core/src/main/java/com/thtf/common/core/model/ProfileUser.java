package com.thtf.common.core.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * ---------------------------
 * 登录用户信息
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/27 15:11
 * 版本：  v1.0
 * ---------------------------
 */
@NoArgsConstructor
@Data
@ApiModel(value = "ProfileUser",description = "用户信息")
public class ProfileUser {
    private String userId;
    private String username;

    public ProfileUser(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
