package com.crlink.smartpark.customer.common.enums;

import lombok.Getter;

public enum InformationStatusEnum {
    NOT_PUBLISH(0, "待生效"), PUBLISHED(1, "已发布");
    @Getter
    private int code;
    private String desc;

    InformationStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
