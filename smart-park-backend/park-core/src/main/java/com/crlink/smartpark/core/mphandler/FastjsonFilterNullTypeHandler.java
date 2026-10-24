package com.crlink.smartpark.core.mphandler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

public class FastjsonFilterNullTypeHandler extends FastjsonTypeHandler {

    public FastjsonFilterNullTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    protected String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

}
