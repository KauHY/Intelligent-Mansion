package com.crlink.smartpark.core.utils;

import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class SimpleJwtUtil {

    private static final String SECRET = "qazwsx123444$#%#()*&& ascrswwi1235 ?;!@#kmmmpom in***xx**&##";

    private static final Integer MINUTES = 10;

    public static String signToken(String msg) {
        return generate(msg, MINUTES);
    }

    public static String checkToken(String token) {
        String msg = validate(token);
        if (StringUtils.isBlank(msg)) {
            throw new ResolinkRuntimeException("40013");
        }
        return msg;
    }

    public static String generate(String msg, Integer minutes) {
        Date expireDate = Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());
        String jwt = Jwts.builder()
                .setSubject(msg)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setExpiration(expireDate)
                .compact();
        return jwt;
    }

    public static String validate(String token) {
        Claims claims;
        try {
            claims = Jwts
                    .parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("身份token解析异常，token无效或已过期");
            throw new ResolinkRuntimeException("40013");
        }
        return claims.getSubject();
    }

}
