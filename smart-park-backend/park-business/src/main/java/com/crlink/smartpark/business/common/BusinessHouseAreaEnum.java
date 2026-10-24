package com.crlink.smartpark.business.common;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 * */
public enum BusinessHouseAreaEnum {
    AREA_LESS_THAN_100(1, "100㎡以下", 0.0, 100.0),
    AREA_FROM_100_TO_500(2, "100-500㎡", 100.0, 500.0),
    AREA_FROM_500_TO_1000(3, "500-1000㎡", 500.0, 1000.0),
    AREA_MORE_THAN_1000(4, "1000㎡以上", 1000.0, null);

    @Getter
    private int code;
    private String desc;
    @Getter
    private Double minArea;
    @Getter
    private Double maxArea;

    BusinessHouseAreaEnum(int code, String desc, Double minArea, Double maxArea) {
        this.code = code;
        this.desc = desc;
        this.minArea = minArea;
        this.maxArea = maxArea;
    }
}
