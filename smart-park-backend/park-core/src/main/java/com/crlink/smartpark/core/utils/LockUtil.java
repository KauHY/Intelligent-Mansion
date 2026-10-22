package com.crlink.smartpark.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LockUtil {

    private static RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        LockUtil.redisTemplate = redisTemplate;
    }

    /**
     * 加锁
     *
     * @param key
     * @return
     */
    public static boolean lock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, key, 10, TimeUnit.SECONDS);
    }

    /**
     * 解锁
     *
     * @param key
     * @return
     */
    public static boolean unLock(String key) {
        return redisTemplate.delete(key);
    }
}
