package com.crlink.smartpark.common.constants.enums;

public enum OrgCategoryEnum {
    HEAD_COMPANY("0", "组织的顶级公司，即租户");
    String code;
    String name;

    OrgCategoryEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
}
