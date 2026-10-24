package com.crlink.smartpark.common.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id2objectField {

    /**
     * 数据编码
     */
    String code();

    /**
     * id获取的对象对应字段名
     * （如对应code获取到的对象为基本类型可以忽略该字段）
     */
    String objField() default "name";

    /**
     * 当前id字段名（默认为当前注解属性名去掉”Name“加上”Id“）
     */
    String idField() default "";

    String targetFieldName() default "";

//    String auto() default "";

}
