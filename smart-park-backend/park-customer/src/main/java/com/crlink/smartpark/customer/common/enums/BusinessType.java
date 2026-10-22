package com.crlink.smartpark.customer.common.enums;

import lombok.Getter;

public enum BusinessType {
    INFORMATION(1, "资讯"), ADVERTISEMENT(2, "广告"), ACTIVITY(3, "活动");
    @Getter
    public int code;
    private String name;

    BusinessType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
