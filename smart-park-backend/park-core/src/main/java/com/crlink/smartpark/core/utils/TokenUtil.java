package com.crlink.smartpark.core.utils;

import com.crlink.smartpark.common.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenUtil {

    @Autowired
    private RedisUtil redisUtil;

    public String wxToken(String tokenId, String userId, String headOrgId, String mobile, String source) {
        String token = JwtBaseUtil.generateWxUserToken(userId, mobile, headOrgId, tokenId, source);
        redisUtil.setObject(Constants.Redis.USER_ACCESS_TOKEN_PREFIX.getBuildCode(tokenId), token, Constants.WX_TOKEN_OVERTIME, TimeUnit.MINUTES);
        return token;
    }
}
