package com.crlink.smartpark.common.constants.enums;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 * */

public enum JobCodeEnum {
    ADMINISTRATOR_JOB_CODE(1, "管理员"), REPAIR_JOB_CODE(2, "维修"),
    CUSTOMER_SERVICE_JOB_CODE(3, "客服"), CLEANER_JOB_CODE(4, "保洁"),
    SECURITY_JOB_CODE(5, "保安"), GREEN_JOB_CODE(6, "绿化"),
    FINANCE_JOB_CODE(7, "财务"), ADMINISTRATION_JOB_CODE(8, "行政"),
    MANAGER_JOB_CODE(9, "经理"), BUSINESS_MAN_JOB_CODE(10, "招商人员"),
    INSPECTION_MAN_JOB_CODE(11, "巡检人员"), GOODS_RELEASE_CHECK_JOB_CODE(12, "物品放行审核员");
    public Integer code;
    @Getter
    private String name;

    JobCodeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
