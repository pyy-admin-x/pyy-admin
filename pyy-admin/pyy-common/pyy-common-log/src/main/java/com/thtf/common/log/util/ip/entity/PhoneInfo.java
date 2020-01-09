package com.thtf.common.log.util.ip.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PhoneInfo {
    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 号码种类 移动 联通等
     */
    private String phoneType;
}
