package com.crlink.smartpark.common.constants.enums;

import lombok.Getter;

@Getter
public enum OrgTypeEnum {
    COMPANY_ORGANIZATION(0, "机构"), PARK(1, "园区"), TENANT(2, "租户");
    int code;
    String name;

    OrgTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
