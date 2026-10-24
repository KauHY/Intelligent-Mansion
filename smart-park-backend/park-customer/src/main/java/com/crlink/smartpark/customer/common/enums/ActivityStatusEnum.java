package com.crlink.smartpark.customer.common.enums;

public enum ActivityStatusEnum {
    NOT_EFFECT(1, "待生效"), NOTICING(2, "预告中"), ONGOING(3, "进行中"), END(4, "已结束");
    public int code;
    public String desc;

    ActivityStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    //public enum PC {
    //}

    //public enum WX {
    //    BEGINNING(2, "即将开始"), ONGOING(3, "进行中"), END(4, "已结束");
    //    public int code;
    //    public String desc;
    //
    //    WX(int code, String desc) {
    //        this.code = code;
    //        this.desc = desc;
    //    }
    //}
}
