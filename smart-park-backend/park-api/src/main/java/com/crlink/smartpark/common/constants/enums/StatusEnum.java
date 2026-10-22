package com.crlink.smartpark.common.constants.enums;

public enum StatusEnum {
    DISABLE(0, "已禁用"), ENABLE(1, "已启用");
    public Integer code;
    String name;

    StatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public static String getStatusNameByCode(Integer code) {
        for (StatusEnum value : values()) {
            if (code.equals(value.code)) {
                return value.name;
            }
        }
        return "";
    }
}
