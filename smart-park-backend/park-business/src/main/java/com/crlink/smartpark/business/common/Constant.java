package com.crlink.smartpark.business.common;

import lombok.Getter;

public interface Constant {

    /**
     * 商机来源：1 商机录入
     */
    int BUSINESS_SOURCE_FROM_CREATE = 1;
    /**
     * 商机来源：2 看房申请
     */
    int BUSINESS_SOURCE_FROM_APPLY_FOR_HOUSE = 2;
    /**
     * 房源招商状态
     */
    enum BusinessInfoStatusEnum {
        NOT_ON_THE_SHELF(0, "未上架"), BUSINESS_ONGOING(1, "招商中"), REMOVE_FROM_SHELF(2, "已下架");
        public int status;
        String desc;

        BusinessInfoStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum ApplyForHouseStatusEnum {

        WAIT_FOR_ACCEPT(1, "待受理"), FINISH(2, "已完成"), RETURN(3, "已撤回");

        @Getter
        private int code;
        @Getter
        private String desc;

        ApplyForHouseStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


    /**
     * 房源招商类型
     */
    enum EffectType {
        TIME_LIMITED_VALIDITY,//限时有效
        LONG_TERM_EFFECTIVENESS//长期有效
    }
}
