package com.crlink.smartpark.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.crlink.smartpark.customer.constants.Constant.DEFAULT_TOPPING_TIME;

@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增活动时，置顶时间字段（toppingTime）添加默认值
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.equals("com.crlink.smartpark.customer.entity.Activity", metaObject.getOriginalObject().getClass().getName())) {
            this.fillStrategy(metaObject, "toppingTime", DEFAULT_TOPPING_TIME);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
