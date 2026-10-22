package com.crlink.smartpark.core.utils;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class JwtBaseUtil {

    private static final String SECRET = "qazwsx123444$#%#()*&& ascrswwi1235 ?;!@#kmmmpom in***xx**&";
    private static final String USER_SUBJECT = "user info";
    private static final String TOKEN_PREFIX = "Bearer_";
    //private static final String USER_WX_TOKEN_PREFIX = "Bearer_wx_";
    //private static final String  ="Bearer_wx_";


    public static String generateUserToken(String userId, String username, String headOrgId, String tokenId, int source) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(Constants.USER_ID, userId);
        param.put(Constants.TOKEN_ID, tokenId);
        param.put(Constants.USER_NAME, username);
        param.put(Constants.HEAD_ORG_ID, headOrgId);
        param.put(Constants.JWT_TS, Instant.now().getEpochSecond());
        // 生成token
        String tokenPrefix = TOKEN_PREFIX + source + StringUtils.SPACE;
        return generateToken(USER_SUBJECT, SignatureAlgorithm.HS512, SECRET, tokenPrefix, param);
    }

    public static String generateWxUserToken(String userId, String mobile, String headOrgId, String tokenId, String source) {
        Map<String, Object> param = new HashMap<>(4);
        param.put(Constants.USER_ID, userId);
        param.put(Constants.TOKEN_ID, tokenId);
        param.put(Constants.USER_MOBILE, mobile);
        param.put(Constants.HEAD_ORG_ID, headOrgId);
        param.put(Constants.JWT_TS, Instant.now().getEpochSecond());
        // 生成token
        String tokenPrefix = TOKEN_PREFIX + source + StringUtils.SPACE;
        return generateToken(USER_SUBJECT, SignatureAlgorithm.HS512, SECRET, tokenPrefix, param);
    }

    public static String generateBusinessUserToken(String tokenId, String userId, String headOrgId, String username, String mobile, String source) {
        Map<String, Object> param = new HashMap<>(4);
        param.put(Constants.USER_ID, userId);
        param.put(Constants.HEAD_ORG_ID, headOrgId);
        param.put(Constants.TOKEN_ID, tokenId);
        param.put(Constants.USER_MOBILE, mobile);
        param.put(Constants.USER_NAME, username);
        param.put(Constants.JWT_TS, Instant.now().getEpochSecond());
        // 生成token
        String tokenPrefix = TOKEN_PREFIX + source + StringUtils.SPACE;
        return generateToken(USER_SUBJECT, SignatureAlgorithm.HS512, SECRET, tokenPrefix, param);
    }


    // 解析token 解析JWT参数
    public static Map<String, String> validateToken(String token) {
        int prefixLength;
        String prefix;
        Map<String, Object> map;
        if (StringUtils.isBlank(token)) {
            throw new ResolinkRuntimeException("40009");
        } else if (token.startsWith(prefix = TOKEN_PREFIX)) {
            prefixLength = prefix.length() + 2;
            map = validateToken(SECRET, prefixLength, token);
        } else {
            throw new ResolinkRuntimeException("40009");
        }
        if (ObjectUtils.isEmpty(map.get(Constants.TOKEN_ID))) {
            throw new ResolinkRuntimeException("40009");
        }
        Map<String, String> res = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            res.put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());
        }
        return res;
    }

    private static String generateToken(String subject, SignatureAlgorithm sa, String secret, String prefix, Map<String, Object> param) {
        String jwt = Jwts.builder()
                .setSubject(subject).setClaims(param)
                .signWith(sa, secret)
                .compact();
        if (StringUtils.isBlank(prefix)) {
            prefix = StrUtil.EMPTY;
        }
        String finalJwt = prefix + jwt;
        return finalJwt;
    }

    private static String generateToken(String subject, Map<String, Object> param) {
        String jwt = Jwts.builder()
                .setSubject(subject).setClaims(param)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return jwt;
    }

    private static Map<String, Object> validateToken(String subject, int prefixLength, String token) {
        if (StringUtils.isBlank(token)) {
            throw new ResolinkRuntimeException("40009");
        }

        Map<String, Object> map = Jwts.parser()
                .setSigningKey(subject)
                .parseClaimsJws(token.substring(prefixLength, token.length()))
                .getBody();
        return map;
    }

}
