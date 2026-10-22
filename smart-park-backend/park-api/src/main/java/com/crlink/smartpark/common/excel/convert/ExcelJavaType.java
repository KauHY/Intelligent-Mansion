package com.crlink.smartpark.common.excel.convert;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelJavaType {

    boolean needIdToName() default false;

    int idToNameType() default -1;

}
