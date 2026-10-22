package com.crlink.smartpark.contract.common;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

public interface RentFeeConstants {

    /**
     * 与计费周期一致
     */
    Integer chargingCycle = 1;

    Integer ZERO = 0;

    Integer ONE_INT = 1;

    Integer FIVE_INT = 5;

    /**
     * 押金类型
     */
    Integer CASH_TYPE = 7;

    /**
     * 押金类型
     */
    Integer CONTRACT_STATUS = 8;

    String RENT_IN_EXPORT_FILENAME = "未出费用";

    String RENT_OUT_EXPORT_FILENAME = "已出费用";

    String RENT_CASH_EXPORT_FILENAME = "押金管理";

    String RENT_LATE_EXPORT_FILENAME = "滞纳金管理";

    String RENT_BILL_EXPORT_FILENAME = "账单";

    String RENT_TRADE_EXPORT_FILENAME = "交易明细";

    String RENT_LEAVE_EXPORT_FILENAME = "离场结算记录";

    String RENT_BILL_CODE_PREFIX = "BN";

    String RENT_TRADE_CODE_PREFIX = "ON";

    String WATER_CHARGE_NAMES = "水费";

    String POWER_CHARGE_NAMES = "电费";

    /**
     * 租赁出账状态
     */
    enum RentFeeOutEnum {
        BINDING(1, "待出账"), LOST(2, "已出账"), CANCELED(3, "已废除");
        public Integer status;
        private String desc;

        RentFeeOutEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum RentFeeBillEnum {
        UNRECEIVED(0, "未收"), RECEIVED(1, "已收");
        public Integer status;
        private String desc;

        RentFeeBillEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum RentFeePayTypeEnum {
        OFFLINE_BANK(1, "线下银行转账");
        public Integer type;
        private String desc;

        RentFeePayTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    enum RentFeeTradeTypeEnum {
        PAY_ORDER(1, "付款订单"), RETURN_ORDER(2, "退款订单");
        public Integer type;
        private String desc;

        RentFeeTradeTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    enum RentFeeTradeReturnOrderEnum {
        CAN_NOT_RETURN(0, "不可退款"), CAN_RETURN(1, "可退款");
        public Integer type;
        private String desc;

        RentFeeTradeReturnOrderEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    /**
     * 数据来源
     */
    enum RentDataSourceEnum {
        CON_CREATE(1, "合同生成"),
        FEE_CREATE(2, "抄表计费");
        public Integer status;
        private String desc;

        RentDataSourceEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 押金状态
     */
    enum RentCashStatusEnum {
        UNPAID(1, "未缴清"), PAID_UP(2, "已缴清"), CLEARED(3, "已结清");
        public Integer status;
        private String desc;

        RentCashStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 滞纳金状态
     */
    enum RentLateStatusEnum {
        COMPUTING(1, "计算中"), PAID_UP(2, "已缴清 "), CANCELED(3, "已免除");
        public Integer status;
        private String desc;

        RentLateStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 滞纳金规则
     */
    enum RentLateRuleEnum {
        ONE(1, "逾期金额*逾期天数*滞纳率"), TWO(2, "每日滞纳金*逾期天数*建筑面积"), THREE(3, "每日滞纳金*逾期天数*套内面积");
        public Integer status;
        private String desc;

        RentLateRuleEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    public enum ContractTypeToSchemeFeeType {
        RENTAL_CONTRACT_1(3, Sets.newHashSet(1), "租赁合同：房屋租金"),
        RENTAL_CONTRACT_2(3, Sets.newHashSet(7), "租赁合同：履约保证金"),
        PROPERTY_CONTRACT(4, Sets.newHashSet(4), "物业合同：物业费"),
        APARTMENT_CONTRACT_1(5, Sets.newHashSet(17, 18, 19), "公寓合同：公寓单、标、套间租金"),
        APARTMENT_CONTRACT_2(5, Sets.newHashSet(20, 21, 22), "公寓合同：公寓单、标、套间押金"),
        Meter_CONTRACT(0, Sets.newHashSet(9, 10, 11, 12), "水费+公摊水费+电费+公摊电费"),
        ;
        @Getter
        int type;
        @Getter
        Set<Integer> calculationValues;
        String description;

        ContractTypeToSchemeFeeType(int type, Set<Integer> calculationValues, String description) {
            this.type = type;
            this.calculationValues = calculationValues;
            this.description = description;
        }
    }

    /**
     * 滞纳金规则
     */
    enum SeasonEnum {
        ONE(1, "一季度"),
        TWO(2, "二季度"),
        THREE(3, "三季度"),
        FOUR(4, "四季度");
        public Integer status;
        private String desc;

        SeasonEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum MeteTypeEnum {
        GENERAL_METER(1, "普通表"), INTELLIGENT_METER(2, "智能表");
        public Integer type;
        private String desc;

        MeteTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    enum DeviceType {
        WATER_METER(5, "水"), ELECTRICITY_METER(6, "电");
        public Integer type;
        private String desc;

        DeviceType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    enum FeeTypeToCalculation {
        HOUSE_RENT(1, new int[]{1, 2}, "房屋租金-建筑面积*月单价、套内面积*月单价"),
        PROPERTY_FEE(4, new int[]{1, 2}, "物业-建筑面积*月单价、套内面积*月单价"),
        APARTMENT_RENT_SINGLE(17, new int[]{4}, "公寓租金-单间-天单价"),
        APARTMENT_RENT_TWIN(18, new int[]{4}, "公寓租金-标间-天单价"),
        APARTMENT_RENT_SUITE(19, new int[]{4}, "公寓租金-套间-天单价"),
        DEPOSIT_SINGLE(20, new int[]{5}, "公寓押金-单间-房间数*单价"),
        DEPOSIT_TWIN(21, new int[]{5}, "公寓押金-标间-房间数*单价"),
        DEPOSIT_SUITE(22, new int[]{5}, "公寓押金-套间-房间数*单价"),
        WATER_METER(10, new int[]{3}, "水表-用量*单价"),
        ELECTRICITY_METER(9, new int[]{3}, "电表-用量*单价"),
        SHARED_ELECTRICITY_CHARGE(11, new int[]{3}, "公摊电费-用量*单价"),
        SHARED_WATER_CHARGE(12, new int[]{3}, "公摊水费-用量*单价"),
        ;
        @Getter
        int type;
        @Getter
        int[] calculationValues;
        String description;

        FeeTypeToCalculation(int type, int[] calculationValues, String description) {
            this.type = type;
            this.calculationValues = calculationValues;
            this.description = description;
        }

        public static FeeTypeToCalculation getFeeTypeToCalculationByFeeType(int type) {
            for (FeeTypeToCalculation value : values()) {
                if (value.type == type) {
                    return value;
                }
            }
            return null;
        }
    }

    enum BillType {
        ALL_BILL(0, "全部"),
        COMPANY_METER_BILL(1, "企业水电费"),
        APARTMENT_METER_BILL(2, "公寓水电费"),
        RENTAL_BILL(3, "租赁费用"),
        PROPERTY_BILL(4, "物业费"),
        APARTMENT_BILL(5, "公寓租金");

        public final Integer type;
        @Getter
        private final String label;

        BillType(Integer type, String label) {
            this.type = type;
            this.label = label;
        }
    }

}
