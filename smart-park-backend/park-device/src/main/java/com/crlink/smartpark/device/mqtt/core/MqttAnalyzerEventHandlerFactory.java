package com.crlink.smartpark.device.mqtt.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description mqtt网关上行数据处理工厂
 * @Param
 * @Return
 * @MethodName
 */
@Slf4j
@Component
public class MqttAnalyzerEventHandlerFactory {
    /**
     * 事件处理类集合
     */
    private static Map<String, MqttAnalyzerEventHandler> handlers;

    @Autowired
    private void collectEventHandler(List<MqttAnalyzerEventHandler> list) {
        handlers = list.stream().collect(Collectors.toMap(MqttAnalyzerEventHandler::code, e -> e, (e1, e2) -> e1));
    }

    public static MqttAnalyzerEventHandler createHandler(String eventCode) {
        return handlers.get(eventCode);
    }

}
