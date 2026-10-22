package com.crlink.smartpark.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils {

    private static ApplicationContext staticApplicationContext;

    @Autowired
    private void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextUtils.staticApplicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> cls) {
        if (staticApplicationContext != null) {
            return staticApplicationContext.getBean(cls);
        }
        return null;
    }

}
