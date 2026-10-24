package com.crlink.smartpark.system.enums;

import lombok.Getter;

public enum HouseStatusEnum {
    FOR_SELF(0, "自用"), RENTING(1, "待租"), RENTED(2, "已租"), SELLING(3, "待售"), SOLD(4, "已售");
    @Getter
    private int code;
    private String label;

    HouseStatusEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
