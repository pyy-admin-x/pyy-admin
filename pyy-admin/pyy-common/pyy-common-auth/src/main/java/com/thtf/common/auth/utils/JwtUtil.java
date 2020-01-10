package com.thtf.common.auth.utils;

import com.thtf.common.auth.token.properties.TokenProperties;
import com.thtf.common.core.exception.ExceptionCast;
import com.thtf.common.core.response.CommonCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * ---------------------------
 * JWT工具类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/10 14:53
 * 版本：  v1.0
 * ---------------------------
 */
@Slf4j
public class JwtUtil {
    /**
     * 创建JWT
     * @param userId
     * @param username
     * @param extAttribute
     * @param tokenProperties
     * @return
     */
    public static String createToken(String userId, String username, Map<String, Object> extAttribute, TokenProperties tokenProperties) {
        try {
            // 使用HS256加密算法
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);

            //生成签名密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenProperties.getBase64Secret());
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            //userId是重要信息，进行加密下
            String encryUserId = Base64Util.encode(userId);

            //添加构成JWT的参数
            JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                    .claim("userId", encryUserId)
                    .setSubject(username) // 代表这个JWT的主体，即它的所有人了
                    .setIssuer(tokenProperties.getClientId()) // 代表这个JWT的签发者
                    .setIssuedAt(new Date()) // 是一个时间戳，代表这个JWT的签发时间
                    .setAudience(tokenProperties.getName()) // 代表这个JWT的接收对象
                    .signWith(signatureAlgorithm, signingKey);
            //添加JWT扩展属性
            for (Map.Entry<String, Object> entry : extAttribute.entrySet()) {
                builder.claim(entry.getKey(), entry.getValue());
            }

            //添加Token过期时间
            int TTLMillis = tokenProperties.getExpiresSecond();
            if (TTLMillis >= 0) {
                long expMillis = nowMillis + TTLMillis;
                Date exp = new Date(expMillis);
                builder.setExpiration(exp)  // 是一个时间戳，代表这个JWT的过期时间；
                        .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
            }

            //生成JWT
            return builder.compact();
        } catch (Exception e) {
            log.error("签名失败", e);
            ExceptionCast.cast(CommonCode.PERMISSION_SIGNATURE_ERROR);
        }
        return null;
    }

    /**
     * 解析JWT
     * @param token
     * @param base64Security
     * @return
     */
    public static Claims parseToken(String token, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException eje) {
            log.info("===== Token过期 =====");
            ExceptionCast.cast(CommonCode.TOKEN_EXPIRED);
        } catch (Exception e){
            log.info("===== token解析异常 =====");
            ExceptionCast.cast(CommonCode.TOKEN_INVALID);
        }
        return null;
    }

    /**
     * 从token中获取用户名
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUsername(String token, String base64Security){
        return parseToken(token, base64Security).getSubject();
    }

    /**
     * 从token中获取用户ID
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUserId(String token, String base64Security){
        String userId = parseToken(token, base64Security).get("userId", String.class);
        return Base64Util.decode(userId);
    }

    /**
     * 是否已过期
     * @param token
     * @param base64Security
     * @return
     */
    public static boolean isExpiration(String token, String base64Security) {
        return parseToken(token, base64Security).getExpiration().before(new Date());
    }
}
