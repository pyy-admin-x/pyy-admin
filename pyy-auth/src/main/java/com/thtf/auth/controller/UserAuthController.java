package com.thtf.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thtf.auth.vo.LoginUserVO;
import com.thtf.auth.vo.ValidateImgVO;
import com.thtf.base.api.model.SysUser;
import com.thtf.base.api.vo.ProfileVO;
import com.thtf.common.core.constant.CommonConstant;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.core.utils.*;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ---------------------------
 * 用户认证Controller
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/20 15:36
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@RestController
public class UserAuthController {
    /** 验证码前缀 */
    private static final String CODE_KEY = "img_code_";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取图形验证码
     * @return
     */
    @ApiOperation(value = "获取图形验证码", notes = "获取图形（算数）验证码  1:字母+数字PNG类型 2:字母+数字GIF类型 3:中文类型 4:中文gif类型 5:算术类型")
    @ApiImplicitParam(name = "type", value = "验证类型", required = true, dataType = "String", paramType = "query")
    @GetMapping("/auth/imgCode")
    ResponseResult<ValidateImgVO> getImgCode(@Valid @NotBlank(message = "验证码类型不能为空") @RequestParam("type") String type) {
        // 获取图片验证码
        Captcha imgCode = ImgCodeUtil.getImgCode(type, 150, 46);
        // 获取运算的结果：5
        String result = imgCode.text();
        log.info("### 验证码：{} ###", result);
        String uuid = CODE_KEY + SnowflakeId.getId();
        ValidateImgVO validateImgVO = new ValidateImgVO(imgCode.toBase64(), uuid);
        // 保存验证码到redis, 默认10分钟
        stringRedisTemplate.opsForValue().set(uuid, result, 10, TimeUnit.MINUTES);

        return ResponseResult.SUCCESS(validateImgVO);
    }

    /**
     * 用户登录
     * @param loginUserVO
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParam(name = "loginUserVO", value = "用户对象", required = true, dataType = "LoginUserVO", paramType = "body")
    @PostMapping("/auth/login")
    ResponseResult<String> login(@Valid @RequestBody LoginUserVO loginUserVO, HttpServletRequest request) throws UnsupportedEncodingException {
        String header = request.getHeader("Authorization");
        if (header == null && !header.startsWith("Basic")) {
            log.debug("### 请求头中无client信息 ###");
            ExceptionCast.cast(CommonCode.HEADER_NOT_EXISTS_CLIENT);
        }
        if (loginUserVO == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        // 查询验证码
        String code = stringRedisTemplate.opsForValue().get(loginUserVO.getUuid());
        // 清除验证码
        stringRedisTemplate.delete(loginUserVO.getUuid());
        if (StringUtils.isBlank(code)) {
            log.debug("### 验证码已过期 ###");
            ExceptionCast.cast(CommonCode.VALIDATE_CODE_EXPIRED);
        }
        if (StringUtils.isBlank(loginUserVO.getCode()) || !loginUserVO.getCode().equals(code)) {
            log.debug("### 验证码错误 ###");
            ExceptionCast.cast(CommonCode.VALIDATE_CODE_INVALID);
        }
        // 使用OAuth2密码模式获取token
        String url = "http://" + request.getRemoteAddr() + ":" + request.getServerPort() + "/oauth/token";

        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "password"); // 授权模式：password
        map.put("username", loginUserVO.getUsername());
        map.put("password", loginUserVO.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", header);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // 必须该模式，不然请求端无法取到 grant_type

        HttpEntity httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url + "?" + LinkStringUtil.createLinkStringByGet(map), httpEntity, String.class);
        if (response.getStatusCodeValue() == 200) {
            JSONObject tokenInfo = JSONObject.parseObject(response.getBody());
            log.info("### 登录成功 ###");
            return ResponseResult.SUCCESS(tokenInfo);
        } else {
            log.info("### 用户名或密码错误 ###");
            ExceptionCast.cast(CommonCode.USERNAME_OR_PASSWORD_ERROR);
        }
        return null;
    }

}
