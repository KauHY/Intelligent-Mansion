package com.crlink.smartpark.device.mqtt.core;

/**
 * @Description mqtt返回上报数据批量处理
 * @Param
 * @Return
 * @MethodName
 */
public interface MqttAnalyzerEventHandler {

    /**
     * @Description 数据处理
     * @Param [topic, message]
     * @Return void
     * @MethodName handle
     */
    void handle(String topic, String message) throws Exception;

    /**
     * 事件编码
     *
     * @return
     */
    String code();
}
