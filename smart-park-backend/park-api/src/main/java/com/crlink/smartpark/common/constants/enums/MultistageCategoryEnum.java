package com.crlink.smartpark.common.constants.enums;

import lombok.Getter;

public enum MultistageCategoryEnum {

    DEFAULT("DEFAULT", 99, false, ""),
    TZ("TZ", 1, false, "设备台账"),
    ENTERPRISE_GROUP("ENTERPRISE_GROUP", 8, false, "企业群组")
    ;
    @Getter
    private String type;
    @Getter
    private int maxHierarchy;
    @Getter
    private boolean onlySelectLeaf;
    private String desc;

    MultistageCategoryEnum(String type, int maxHierarchy, boolean onlySelectLeaf, String desc) {
        this.type = type;
        this.maxHierarchy = maxHierarchy;
        this.onlySelectLeaf = onlySelectLeaf;
        this.desc = desc;
    }

    public static MultistageCategoryEnum getMultistageCategoryByType(String type){
        for (MultistageCategoryEnum value : values()) {
            if (type == value.type) {
                return value;
            }
        }
        return DEFAULT;
    }
}
