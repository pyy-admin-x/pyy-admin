package com.thtf.common.core.utils;

import cn.hutool.core.util.StrUtil;
import com.thtf.common.core.constant.ImgCodeType;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;

/**
 * ---------------------------
 * 图片验证码工具类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/13 15:48
 * 版本：  v1.0
 * ---------------------------
 */
public class ImgCodeUtil {
    /**
     * 获取图片验证码
     * @param type
     * @param width
     * @param height
     * @return
     */
    public static Captcha getImgCode(String type, int width, int height) {
        if (StrUtil.isBlank(type)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Captcha captcha = null;
        switch (type) {
            case ImgCodeType.PNG:
                // png类型
                captcha = new SpecCaptcha(width, height);
            case ImgCodeType.GIF :
                // gif类型
                captcha = new GifCaptcha(width, height);
                break;
            case ImgCodeType.CHINESE :
                // 中文类型
                captcha = new ChineseCaptcha(width, height);
                break;
            case ImgCodeType.CHINESE_GIF :
                // 中文gif类型
                captcha = new ChineseGifCaptcha(width, height);
                break;
            case ImgCodeType.ARITHMETIC :
                // 算术类型
                ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(width, height);
                arithmeticCaptcha.setLen(2);  // 几位数运算，默认是两位
                captcha = arithmeticCaptcha;
                break;
            default:
                // png类型
                captcha = new SpecCaptcha(width, height);
        }
        return captcha;
    }}
