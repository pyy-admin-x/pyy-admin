package com.thtf.base.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ---------------------------
 * 用户登录VO类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019-12-31 16:10:54
 * 版本：  v1.0
 * ---------------------------
 */
@Data
@ApiModel(value = "LoginUserVO",description = "用户登录VO类")
public class LoginUserVO {

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("验证码UUID")
    private String uuid;
}
