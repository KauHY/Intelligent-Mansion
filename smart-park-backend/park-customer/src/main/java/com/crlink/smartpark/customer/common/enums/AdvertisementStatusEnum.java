package com.crlink.smartpark.customer.common.enums;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 * */
public enum AdvertisementStatusEnum {
    EFFECTIVELY(0, "生效中"), TO_BE_EFFECTIVE(1, "待生效"),
    OUT_OF_DATE(2, "已过期");

    @Getter
    private int code;
    @Getter
    private String desc;

    AdvertisementStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
