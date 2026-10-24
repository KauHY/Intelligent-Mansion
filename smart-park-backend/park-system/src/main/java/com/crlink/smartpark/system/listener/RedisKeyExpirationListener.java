package com.crlink.smartpark.system.listener;

import com.crlink.smartpark.attendance.feign.AttendanceRuleProvider;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.WxOAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WxOAUtil wxOAUtil;
    private static AttendanceRuleProvider attendanceRuleProvider;


    @Autowired
    public void setAttendanceRuleProvider(AttendanceRuleProvider attendanceRuleProvider){
        RedisKeyExpirationListener.attendanceRuleProvider = attendanceRuleProvider;
    }

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 使用该方法监听 ,当我们的key失效的时候执行改方法
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if (key.startsWith(Constants.Redis.PUNCH_REMINDER_IN_PREFIX.getCode()) ||
                key.startsWith(Constants.Redis.PUNCH_REMINDER_OUT_PREFIX.getCode())) {
            CompletableFuture.runAsync(() -> attendanceRuleProvider.punchReminder(Constants.Redis.getOriginKey(key)));
        }else if (key.startsWith(Constants.Redis.WX_OA_ACCESS_TOKEN_PREFIX.getCode())){
            wxOAUtil.setAccessToken();
        }
    }
}