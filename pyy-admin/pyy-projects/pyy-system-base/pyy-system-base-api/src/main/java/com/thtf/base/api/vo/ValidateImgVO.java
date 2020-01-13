package com.thtf.base.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ========================
 * 图形验证码VO
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/11/18 10:08
 * Version: v1.0
 * ========================
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateImgVO {

    @ApiModelProperty("Base64图形码")
    private String img;

    @ApiModelProperty("验证码KEY")
    private String uuid;
}
