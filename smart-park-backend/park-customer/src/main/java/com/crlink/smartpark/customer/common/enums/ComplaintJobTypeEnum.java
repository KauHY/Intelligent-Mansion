package com.crlink.smartpark.customer.common.enums;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 * */
public enum ComplaintJobTypeEnum {
    REPAIR_JOB_CODE(2, "维修"), CLEANER_JOB_CODE(4, "保洁"),
    SECURITY_JOB_CODE(5, "保安"), GREEN_JOB_CODE(6, "绿化"),
    ADMINISTRATION_JOB_CODE(8, "行政");

    public Integer code;
    @Getter
    private String name;

    ComplaintJobTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
