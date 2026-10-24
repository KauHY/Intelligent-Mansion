//package com.crlink.smartpark.device.mqtt.core;
//
//import com.crlink.smartpark.common.constants.Constants;
//import com.crlink.smartpark.core.utils.DateUtil;
//import com.crlink.smartpark.device.common.MqttConstants;
//import com.crlink.smartpark.device.service.ClientMqttLogService;
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.dsl.MessageChannels;
//import org.springframework.integration.endpoint.MessageProducerSupport;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
//import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
//import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @Description mqtt消息服务配置工厂，包括订阅、处理、推送服务
// * @Param
// * @Return
// * @MethodName
// */
//@Slf4j
//@Configuration
//public class MqttBeanConfig {
//
//    @Value("${param.mqtt.host}")
//    private String host;
//    @Value("${param.mqtt.username}")
//    private String username;
//    @Value("${param.mqtt.password}")
//    private String password;
//    @Value("${param.mqtt.upTopic}")
//    private String upTopic;
//
//    @Autowired
//    private ClientMqttLogService iClientMqttLogService;
//
//    /**
//     * mqtt工厂
//     *
//     * @return
//     */
//
//    @Bean
//    public MqttPahoClientFactory mqttClientFactoryPub() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setUserName(username);
//        options.setPassword(password.toCharArray());
//        options.setServerURIs(new String[]{host});
//        options.setKeepAliveInterval(60);
//        options.setAutomaticReconnect(true);
//        options.setCleanSession(true);
//        options.setConnectionTimeout(30);
//        options.setMaxInflight(10000);
//        factory.setConnectionOptions(options);
//        return factory;
//    }
//
//    @Bean
//    public ThreadFactory createThreadFactoryPub() {
//        return new ThreadFactoryBuilder().setNameFormat("pub-pool-%d").build();
//    }
//
//    /**
//     * 消息发送
//     *
//     * @return
//     */
//    @Bean
//    public IntegrationFlow mqttOutFlow() {
//        return IntegrationFlows.from(outChannel())
//                .channel(MessageChannels.executor(
//                        new ThreadPoolExecutor(5, 200, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
//                                createThreadFactoryPub(), new ThreadPoolExecutor.AbortPolicy())))
//                .handle(mqttOutbound()).get();
//    }
//
//    @Bean
//    public MessageChannel outChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageHandler mqttOutbound() {
//        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
//                "mqtt-publish" + System.currentTimeMillis(), mqttClientFactoryPub());
//        messageHandler.setAsync(true);
//        messageHandler.setDefaultRetained(false);
//        //设置消息的默认主题
//        messageHandler.setDefaultQos(0);
//        messageHandler.setConverter(new DefaultPahoMessageConverter());
//        return messageHandler;
//    }
//
//
//    @Bean
//    public ThreadFactory createThreadFactorySub() {
//        return new ThreadFactoryBuilder().setNameFormat("sub-pool-%d").build();
//    }
//
//    /**
//     * 网关上下线通知
//     * 订阅工厂，订阅不同主题
//     *
//     * @return
//     */
//    @Bean
//    public MessageProducerSupport mqttInbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
//                "mqtt-subscribe" + System.currentTimeMillis(), mqttClientFactoryPub(), upTopic);
//        adapter.setCompletionTimeout(5000);
//        DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//        defaultPahoMessageConverter.setPayloadAsBytes(true);
//        adapter.setConverter(defaultPahoMessageConverter);
//        adapter.setQos(0);
//        return adapter;
//    }
//
//    /**
//     * 网关上下线通知
//     * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
//     *
//     * @return
//     */
//    @Bean
//    public IntegrationFlow mqttInFlow() {
//        return IntegrationFlows.from(mqttInbound())
//                .channel(MessageChannels.executor(
//                        new ThreadPoolExecutor(5, 200, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
//                                createThreadFactorySub(), new ThreadPoolExecutor.AbortPolicy())))
//                .channel("datas").get();
//    }
//
//
//    /**
//     * 网关上下线通知
//     */
//    @Bean
//    @ServiceActivator(inputChannel = "datas")
//    public MessageHandler data() {
//        return message -> {
//            String payload = new String((byte[]) message.getPayload());
//            String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
//            String qos = message.getHeaders().get(MqttHeaders.RECEIVED_QOS).toString();
//            String time = DateUtil.parseTimestampToDateTimeStr((Long) message.getHeaders().get("timestamp"));
//            log.info("接收到订阅的消息,主题：{},消息等级：{}, 时间：{}, 内容:{}", topic, qos, time, payload);
//            iClientMqttLogService.saveMqttLog(topic, payload, MqttConstants.MqttMsgType.SUBSCRIBE.getCode());
//
//            String[] strings = topic.split("\\/");
//            if (strings.length <= Constants.TWO) {
//                log.error("---当前信息TOPIC异常---TOPIC:{}，MESSAGE: {} ,TIME: {}", topic, payload, time);
//                return;
//            }
//            String code = strings[2];
//            try {
//                MqttAnalyzerEventHandler eventHandler = MqttAnalyzerEventHandlerFactory.createHandler(code);
//                if (eventHandler == null) {
//                    log.error("暂不支持该请求，主题：{}，内容{}", topic, payload);
//                    return;
//                }
//                eventHandler.handle(topic, payload);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        };
//    }
//
//
//}