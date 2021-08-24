package me.wjy.bill.utils;

import io.jsonwebtoken.*;
import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 王金义
 * @date 2021/8/4
 */
public class JWTUtil {
    /**
     * 密钥
     */
    private static final String PRIVATE_KEY = "529151416a5b36d4ec8d2a79d32cc292fb314559aa5559a0e4ffcfbec29d72e3";
    /**
     * 失效时间
     */
    private static final Long EFFECTIVE_TIME_2_HOURS = TimeUnit.HOURS.toMillis(2);
    private static final Long EFFECTIVE_TIME_1_MONTH = TimeUnit.DAYS.toMillis(31);
    /**
     * 签名算法
     */
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    /**
     * 计算签名 Key 值
     */
    private static final Key SIGNING_KEY = new SecretKeySpec(Base64.decodeBase64(PRIVATE_KEY), SIGNATURE_ALGORITHM.getJcaName());


    public static String getToken(String userId) {
        JwtBuilder jwtBuilder = Jwts.builder();

        long nowMillis = System.currentTimeMillis();
        // 7个官方Payload字段
        // 编号/版本
        jwtBuilder.setId("1.0");
        // 发行人
        jwtBuilder.setIssuer("Annie3310");
        // 主题
        jwtBuilder.setSubject("SUBJECT");
        // 受众
        jwtBuilder.setAudience("AUDIENCE");
        // 签发时间
        jwtBuilder.setIssuedAt(new Date(nowMillis));
        // 生效时间
        jwtBuilder.setNotBefore(new Date(nowMillis));

        // 用户自定义字段
        jwtBuilder.claim("id", userId);

        // 进行签名
        jwtBuilder.signWith(SIGNATURE_ALGORITHM, SIGNING_KEY);
        // 失效时间 要写在 claim 和 setClaim 后面, 否则会导致 token 直接过期
        jwtBuilder.setExpiration(new Date(nowMillis + EFFECTIVE_TIME_2_HOURS));

        // 获取token字符串 并返回
        return jwtBuilder.compact();
    }

    public static String checkToken(String token) throws ServiceException {
        if (token == null) {
            return null;
        }
        Claims body;
        try {
            body = Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "Token 认证失败");
        }
        return (String) body.get("id");
    }
}
