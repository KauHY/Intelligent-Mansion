package com.crlink.smartpark.fee.common;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

public class FeeConstant {

    public enum FeeTypeToCalculation {
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
}
