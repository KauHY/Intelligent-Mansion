package com.crlink.smartpark.customer.common.enums;

import lombok.Getter;

/**
 * 投诉报修枚举
 * */
public enum OrderEnum {
    ORDER_STATE_WAIT_ASSIGN(1, "待派单"), ORDER_STATE_WAIT_HANDLE(2, "待处理"),
    ORDER_STATE_RETURN(3, "退单"), ORDER_STATE_FINISH(8, "已完成"),
    ORDER_STATE_CANCEL(9, "已撤单"), ORDER_STATE_TRANSFER(10, "转办中"),
    ORDER_CANCEL_REASON_1(1, "问题已自行解决"), ORDER_CANCEL_REASON_2(2, "误会了，没有这个问题"),
    ORDER_CANCEL_REASON_3(3, "已有人现场处理"), ORDER_CANCEL_REASON_4(4, "其他原因"),
    ORDER_CANCEL_REASON_5(5, "因员工不存在/岗位调整，系统自动退单"),
    RECORD_STATE_CREATE_ORDER(1, "创建"), RECORD_STATE_ASSIGN_ORDER(2, "派单"), RECORD_STATE_RETURN_ORDER(3, "退单"),
    RECORD_STATE_FINISH_ORDER(4, "结单"), RECORD_STATE_CANCEL_ORDER(5, "撤单"), RECORD_STATE_TRANSFER(6, "转办"),
    RECORD_STATE_TRANSFER_HANDLE(7, "转办处理");

    public Integer code;
    @Getter
    private String desc;

    OrderEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
