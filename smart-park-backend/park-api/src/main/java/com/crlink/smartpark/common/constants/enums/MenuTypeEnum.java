package com.crlink.smartpark.common.constants.enums;

public enum MenuTypeEnum {


    MENU(0, "菜单"), BUTTON(1, "按钮"), NAVIGATION(2, "导航");
    int type;
    String name;

    MenuTypeEnum(int type, String name) {
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
