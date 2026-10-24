package com.crlink.smartpark.core.id2obj;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Id2objectHandlerManager {

    /**
     * 数据综合处理器
     */
    private static Id2objectOverallHandler overallHandler;

    /**
     * 单数据处理器
     */
    private static Map<String, Id2objectSingleHandler> handlerMap;

    @Autowired
    public void initId2NameHandlerFactory(@Autowired(required = false) Id2objectOverallHandler overallHandler, @Autowired(required = false) List<Id2objectSingleHandler> handlerList) {
        Id2objectHandlerManager.init(overallHandler, handlerList);
    }

    public static void init(Id2objectOverallHandler overallHandler, List<Id2objectSingleHandler> handlerList) {
        Id2objectHandlerManager.overallHandler = overallHandler;
        Id2objectHandlerManager.handlerMap = CollUtil.isEmpty(handlerList) ? MapUtil.newHashMap() : handlerList.stream().collect(Collectors.toMap(Id2objectSingleHandler::code, Function.identity()));
    }

    public static Id2objectSingleHandler getHandler(String code) {
        Id2objectSingleHandler singleHandler = handlerMap.get(code);
        if (singleHandler != null) {
            return singleHandler;
        }
        return overallHandler.buildId2objSingleHandler(code);
    }
}
