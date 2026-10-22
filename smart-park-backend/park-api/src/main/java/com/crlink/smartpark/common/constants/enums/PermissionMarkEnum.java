package com.crlink.smartpark.common.constants.enums;

import lombok.Getter;

public enum PermissionMarkEnum {
    NORMAL(1, "正常"), EDIT_MARK(2, "数据权限，用于编辑时显示");

    @Getter
    int code;
    String name;

    PermissionMarkEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
