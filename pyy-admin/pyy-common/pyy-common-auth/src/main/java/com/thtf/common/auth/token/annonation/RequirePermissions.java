package com.thtf.common.auth.token.annonation;

import java.lang.annotation.*;

/**
 * ========================
 * JWT验证注解
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/7/18 9:50
 * Version: v1.0
 * ========================
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermissions {
    String value();
}
