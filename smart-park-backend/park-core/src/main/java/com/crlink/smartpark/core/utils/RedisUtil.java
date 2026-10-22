package com.crlink.smartpark.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Title: RedisUtil
 * @Description: RedisUtil
 */
@Component
public class RedisUtil<T> {

    @Autowired
    private RedisTemplate<String, T> redisTemplate;

    public void setObject(String key, T value) {
        this.setObject(key, value, null, null);
    }

    public void setObject(String key, T value, Long timeout, TimeUnit timeUnit) {
        if (value != null) {
            if (timeout != null) {
                redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
        }
    }

    public T getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public Long delete(Set<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Set<String> keys(String keyPattern) {
        return redisTemplate.keys(keyPattern);
    }

    /**
     * 根据前缀删除所有缓存
     *
     * @param prefix
     * @return
     */
    public Long deleteByPrefix(String prefix) {
        Set<String> keys = keys(prefix + "*");
        return delete(keys);
    }

    public T getObjectOrSet(Supplier<T> dbOperation, String key, Long timeout, TimeUnit timeUnit) {
        T result = getObject(key);
        if (result != null) {
            return result;
        }
        result = dbOperation.get();
        if (timeout == null || timeout == null) {
            setObject(key, result);
        } else {
            setObject(key, result, timeout, timeUnit);
        }
        return result;
    }

    public T getObjectOrSet(Supplier<T> dbOperation, String key) {
        return getObjectOrSet(dbOperation, key, null, null);
    }


    /**
     * 计数
     *
     * @param key
     * @param values
     */
    public Long add(String key, T... values) {
        return redisTemplate.opsForHyperLogLog().add(key, values);
    }

    /**
     * 获取总数
     *
     * @param key
     */
    public Long size(String key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 存储为 List
     *
     * @param key    键
     * @param values 要存储的值集合
     */
    public void setListByLeftPush(String key, Collection<T> values) {
        if (values != null) {
            redisTemplate.opsForList().leftPushAll(key, values);
        }
    }

    /**
     * 移除并获取集合中的左边第一个元素
     *
     * @param key 键
     * @return
     */
    public T leftPopList(String key) {
        return this.leftPopList(key, null, null);
    }

    public T leftPopList(String key, Long timeout, TimeUnit timeUnit) {
        T result = null;
        if (key != null) {
            if (timeout != null) {
                result = redisTemplate.opsForList().leftPop(key, timeout, timeUnit);
            } else {
                result = redisTemplate.opsForList().leftPop(key);
            }
        }
        return result;
    }
}
