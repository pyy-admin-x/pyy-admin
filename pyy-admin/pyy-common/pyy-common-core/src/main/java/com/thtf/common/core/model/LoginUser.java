package com.thtf.common.core.model;

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
@AllArgsConstructor
@Data
public class LoginUser {
    private String userId;
    private String username;
}
