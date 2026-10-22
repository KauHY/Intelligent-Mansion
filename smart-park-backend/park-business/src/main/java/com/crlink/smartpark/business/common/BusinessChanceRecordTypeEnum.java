package com.crlink.smartpark.business.common;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 */
public enum BusinessChanceRecordTypeEnum {
    CREATE_CHANCE_1(1, "创建商机"), ASSIGN_CHANCE_2(2, "分配商机"), RECEIVE_CHANCE_3(3, "领取商机"),
    FOLLOW_CHANCE_4(4, "跟进商机"), REMIND_CHANCE_5(5, "提醒商机"), TRANSFER_CUSTOMER_6(6, "转成客户"),
    TRANSFER_CHANCE_7(7, "转移商机"), RELEASE_CHANCE_8(8, "释放商机"), DELETE_CHANCE_9(9, "删除商机");
    @Getter
    private int code;
    @Getter
    private String desc;

    BusinessChanceRecordTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
