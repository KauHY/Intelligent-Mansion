package com.crlink.smartpark.common.constants.enums;

import lombok.Getter;

public enum RoleTypeEnum {
    SYS_ROLE(0, "系统"), ORG_TENANT_ROLE(1, "租户的角色"), ORG_ORDINARY_ROLE(2, "非租户，组织的角色");
    @Getter
    int code;
    String name;

    RoleTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
