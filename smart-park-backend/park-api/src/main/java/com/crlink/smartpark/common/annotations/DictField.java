package com.crlink.smartpark.common.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictField {

    /**
     * 字典类型
     */
    String dictType() default "";

    /**
     * 字典值字段名（默认为当前所注解字段）
     */
    String dictValField() default "";

    /**
     * 字典文案字段名（默认为：idField() + ”Label“）
     */
    String textField() default "";

}
