package com.thtf.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.thtf.auth.vo.LoginUserVO;
import com.thtf.auth.vo.ValidateImgVO;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import com.thtf.common.core.response.ResponseResult;
import com.thtf.common.core.utils.ImgCodeUtil;
import com.thtf.common.core.utils.IpUtil;
import com.thtf.common.core.utils.LinkStringUtil;
import com.thtf.common.core.utils.SnowflakeId;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ---------------------------
 * 用户认证service
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/22 17:04
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
@Service
public class UserAuthService {

    /** 验证码前缀 */
    private static final String CODE_KEY = "img_code_";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 获取图片验证码
     * @param type
     * @return
     */
    public ValidateImgVO getImgCode(String type) {
        // 获取图片验证码
        Captcha imgCode = ImgCodeUtil.getImgCode(type, 150, 46);
        // 获取运算的结果：5
        String result = imgCode.text();
        log.info("### 验证码：{} ###", result);
        String uuid = CODE_KEY + SnowflakeId.getId();
        ValidateImgVO validateImgVO = new ValidateImgVO(imgCode.toBase64(), uuid);
        // 保存验证码到redis, 默认10分钟
        stringRedisTemplate.opsForValue().set(uuid, result, 10, TimeUnit.MINUTES);
        return  validateImgVO;
    }

    /**
     * 用户登录
     * @param loginUserVO
     * @param request
     * @return
     */
    public JSONObject login(LoginUserVO loginUserVO, HttpServletRequest request) {
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

        JSONObject token = this.applyToken(loginUserVO.getUsername(), loginUserVO.getPassword(), request);
        return token;
    }

    // 申请令牌
    private JSONObject applyToken(String username, String password, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null && !header.startsWith("Basic")) {
            log.debug("### 请求头中无client信息 ###");
            ExceptionCast.cast(CommonCode.HEADER_NOT_EXISTS_CLIENT);
        }

        loadBalancerClient.choose()

        // 使用OAuth2密码模式获取token
        String authUrl = "http://" + IpUtil.getIpAddr(request) + ":" + request.getServerPort() + "/oauth/token";
        log.info("### authUrl={} ###", authUrl);
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "password"); // 授权模式：password
        map.put("username", username);
        map.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", header);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);  // 必须该模式，不然请求端无法取到 grant_type

        HttpEntity httpEntity = new HttpEntity<>(headers);
        // 设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401){
                    super.handleError(response);
                }
            }
        });

        // http请求spring security的申请令牌接口
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(authUrl + "?" + LinkStringUtil.createLinkStringByGet(map), httpEntity, String.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (response == null || response.getStatusCodeValue() == 200) {
            JSONObject tokenInfo = JSONObject.parseObject(response.getBody());
            log.info("### 登录成功 ###");
            return tokenInfo;
        } else {
            log.info("### 用户名或密码错误 ###");
            ExceptionCast.cast(CommonCode.USERNAME_OR_PASSWORD_ERROR);
        }
        return null;
    }
}
