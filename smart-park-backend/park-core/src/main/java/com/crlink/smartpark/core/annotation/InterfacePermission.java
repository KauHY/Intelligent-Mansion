package com.crlink.smartpark.core.annotation;

import com.crlink.smartpark.common.constants.enums.HeaderSource;

public @interface InterfacePermission {
    HeaderSource[] source() default HeaderSource.USER_PARK_MANAGEMENT_SOURCE;
}
