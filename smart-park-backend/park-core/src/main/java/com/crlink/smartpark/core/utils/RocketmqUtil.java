package com.crlink.smartpark.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RocketmqUtil {

    private static RocketMQTemplate rocketMQTemplate;

    @Autowired(required = false)
    public void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        RocketmqUtil.rocketMQTemplate = rocketMQTemplate;
    }

    public static void sendSyncMessageWithoutTag(Enum topic, Object msg) {
        //构建消息
        //方法2：使用rocketMQTemplate封装的消息发送方法
        // 第一个参数指定Topic与Tag，格式: `topicName:tags`
        // 第二个参数，Message对象
        SendResult sendResult = null;
        try {
            sendResult = rocketMQTemplate.syncSend(topic.name(), msg);
        } catch (Exception e) {
            log.error("===>>>消息发送失败<<======{}", msg, e);
        }
        log.info("===>>>消息发送<<======{}", sendResult);
    }

    public static void sendSyncMessage(Enum topic, Enum tag, Object msg) {
        //构建消息
        //方法2：使用rocketMQTemplate封装的消息发送方法
        // 第一个参数指定Topic与Tag，格式: `topicName:tags`
        // 第二个参数，Message对象
        SendResult sendResult = null;
        try {
            sendResult = rocketMQTemplate.syncSend(topic.name() + ":" + tag.name(), msg);
        } catch (Exception e) {
            log.error("===>>>消息发送失败<<======{}", msg, e);
        }
        log.info("===>>>消息发送<<======{}", sendResult);
    }

}
