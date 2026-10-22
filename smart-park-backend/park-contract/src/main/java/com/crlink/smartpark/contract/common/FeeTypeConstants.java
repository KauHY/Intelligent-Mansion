package com.crlink.smartpark.contract.common;

/**
 * <p>
 *
 * </p>
 * */
public interface FeeTypeConstants {
    enum RentFeeTypeEnum {
        HOUSE_RENT(1, "房屋租金"), APARTMENT_RENT_SINGLE(17, "公寓租金-单间"),
        APARTMENT_RENT_TWIN(18, "公寓租金-标间"), APARTMENT_RENT_SUITE(19, "公寓租金-套间"),
        PROPERTY_FEE(4, "物业费");
        public Integer type;
        private String desc;

        RentFeeTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    enum DepositFeeTypeEnum {
        PROMISED_MONEY(7, "履约保证金"),
        DEPOSIT_SINGLE(20, "公寓押金-单间"),
        DEPOSIT_TWIN(21, "公寓押金-标间"),
        DEPOSIT_SUITE(22, "公寓押金-套间");
        public Integer type;
        private String desc;

        DepositFeeTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }
}
