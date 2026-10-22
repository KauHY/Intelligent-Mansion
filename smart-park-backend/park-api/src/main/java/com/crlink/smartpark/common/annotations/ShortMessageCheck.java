package com.crlink.smartpark.common.annotations;

import com.crlink.smartpark.common.constants.ShortMessageConstant;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShortMessageCheck {

    /**
     * 1-只校验验证码；2-只校验手机号验证码;3-全部；
     * @return
     */
    int type() default 3;

    /**
     * 短信存redis前缀
     * @return
     */
    ShortMessageConstant.TemplateEnum telRedisPrefix() default ShortMessageConstant.TemplateEnum.DEFAULT_TEMPLATE;
}
