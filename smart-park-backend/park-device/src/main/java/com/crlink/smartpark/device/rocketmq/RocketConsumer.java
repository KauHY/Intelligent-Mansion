//package com.crlink.smartpark.device.rocketmq;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Component;
//
///**
// * @Description
// * @Created by ZHOUXINJIAN3
// */
//@Slf4j
//@Component
//@RocketMQMessageListener(topic = "DEVICE_TOPIC", consumerGroup = "springboot-mq-consumer-1", nameServer = "101.34.200.233:9876")
//public class RocketConsumer implements RocketMQListener<String> {
//    @Override
//    public void onMessage(String message) {
//        log.info("Receive message：" + message);
//    }
//}
