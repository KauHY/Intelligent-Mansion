package com.crlink.smartpark.customer.common;

public interface FormConstant {

    enum FormCycleEnum {
        EVERY_DAY(1, "1每日"), EVERY_WEEK(2, "2每周"), EVERY_MONTH(3, "3每月"), CUSTOM(4, "4自定义");
        public Integer type;
        private String desc;

        FormCycleEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    enum FormListStatusEnum {
        FILL_FORM(1, "信息填报"), ME_SUBMITTED(2, "我填报的"), ME_RECEIVED(3, "已结束");
        public Integer status;
        private String desc;

        FormListStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }
}
