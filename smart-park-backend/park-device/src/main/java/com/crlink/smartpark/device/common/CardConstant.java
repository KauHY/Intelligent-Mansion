package com.crlink.smartpark.device.common;

/**
 * <p>
 *
 * </p>
 * */
public interface CardConstant {

    String CARD_EXPORT_FILENAME = "卡片管理";

    enum CardStatusEnum {
        BINDING(1, "已绑定"), LOST(2, "已挂失"), CANCELED(3, "已注销"), UNBUNDLE(4, "已解绑");
        public Integer status;
        private String desc;

        CardStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum OperationTypeEnum {
        CANCEL_LOST(1, "取消挂失"), TO_LOST(2, "挂失"), TO_BINDING(3, "绑卡"),
        TO_UNBUNDLE(4, "解绑"), EXCHANGE_CARD(5, "换卡");
        public Integer type;
        private String desc;

        OperationTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }
}
