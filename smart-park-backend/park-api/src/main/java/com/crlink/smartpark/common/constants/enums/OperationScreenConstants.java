package com.crlink.smartpark.common.constants.enums;

import lombok.Getter;

/**
 * <p>
 * 运营大屏枚举、常量
 * </p>
 * */
public interface OperationScreenConstants {
    /**
     * 1报事服务(服务访评)、2报修服务、3 物品放行
     */
    int COMPLAINT = 1;
    int REPAIR = 2;
    int GOODS_RELEASE = 3;

    /**
     * 大屏收费管理类型
     */
    enum BillType {
        RENTAL_BILL(1, "租赁费用"),//入园协议
        APARTMENT_METER_BILL(2, "公寓水电费"),//招引合同
        MEETING_BILL(3, "场地预定费"),//房屋租赁合同
        COMPANY_METER_BILL(4, "企业水电费"),//物业合同
        PROPERTY_BILL(5, "物业费"),//公寓合同
        APARTMENT_BILL(6, "公寓租金");

        public final Integer type;
        @Getter
        private final String label;

        BillType(Integer type, String label) {
            this.type = type;
            this.label = label;
        }
    }
}
