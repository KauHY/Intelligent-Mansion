package com.crlink.smartpark.common.constants.enums;

public enum HeaderSource {
    USER_PLATFORM_SOURCE("0", "PC端来源,租户管理平台"), USER_PARK_MANAGEMENT_SOURCE("1", "PC端来源，运营管理平台"),
    USER_WX_SOURCE_B("2", "园区运营小程序"), USER_WX_SOURCE_C("3", "园区访客小程序"), USER_BUSINESS_SOURCE("4", "园区访客小程序");
    public String source;
    public String desc;

    HeaderSource(String source, String desc) {
        this.source = source;
        this.desc = desc;
    }
}
