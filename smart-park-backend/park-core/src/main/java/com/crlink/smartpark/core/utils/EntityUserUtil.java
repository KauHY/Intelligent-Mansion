package com.crlink.smartpark.core.utils;

import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

@Slf4j
public class EntityUserUtil {

    private static final String CREATE_BY = "createBy";

    private static final String UPDATE_BY = "updateBy";
    private static final String ID = "id";

    public static void setAll(Object obj, boolean isCreate) {
        if (isCreate) {
            setCreateBy(obj);
        }
        setUpdateBy(obj);
    }

    public static void setAll(Object obj) {
        setCreateBy(obj);
        setUpdateBy(obj);
    }

    public static void setCreateBy(Object obj) {
        try {
            setPropertyVal(obj, CREATE_BY, SystemUtil.getInstance().getCurrentUserId());
        } catch (Exception e) {
            log.error("设置登录用户createBy信息出错：", e);
            throw new ResolinkRuntimeException("10002");
        }
    }

    public static void setUpdateBy(Object obj) {
        try {
            setPropertyVal(obj, UPDATE_BY, SystemUtil.getInstance().getCurrentUserId());
        } catch (Exception e) {
            log.error("设置登录用户updateBy信息出错：", e);
            throw new ResolinkRuntimeException("10002");
        }
    }

    private static void setPropertyVal(Object obj, String propertyName, Object newVal) throws IllegalAccessException, NoSuchFieldException {
        Field field = obj.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        field.set(obj, newVal);
    }

    public static void setAllDynamic(Object obj) {
        try {
            BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
            String id = (String) beanWrapper.getPropertyValue(ID);
            if (StringUtils.hasText(id)) {
                setUpdateBy(obj);
            } else {
                setAll(obj);
            }
        } catch (BeansException e) {
            log.error("【获取id失败】", e);
        }
    }
}

