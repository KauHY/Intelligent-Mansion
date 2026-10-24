package com.crlink.smartpark.common.constants.enums;

public enum PermissionTypeEnum {
    MENU(0, "菜单"), DATA(2, "数据");
    int type;
    String name;

    PermissionTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
