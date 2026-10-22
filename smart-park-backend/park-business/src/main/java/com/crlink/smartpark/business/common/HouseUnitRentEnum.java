package com.crlink.smartpark.business.common;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 * */
public enum HouseUnitRentEnum {
    RENT_LESS_THAN_50(1, "50元/㎡/月以下", 0.0, 50.0),
    RENT_FROM_50_TO_100(2, "50-100元/㎡/月", 50.0, 100.0),
    RENT_FROM_100_TO_200(3, "100-200元/㎡/月", 100.0, 200.0),
    RENT_FROM_200_TO_500(4, "200-500元/㎡/月", 200.0, 500.0),
    RENT_MORE_THAN_500(5, "500元/㎡/月以上", 500.0, null);

    @Getter
    private int code;
    private String desc;
    @Getter
    private Double minRent;
    @Getter
    private Double maxRent;

    HouseUnitRentEnum(int code, String desc, Double minRent, Double maxRent) {
        this.code = code;
        this.desc = desc;
        this.minRent = minRent;
        this.maxRent = maxRent;
    }
}
