package com.crlink.smartpark.contract.common;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ContractConstants {

    public static final String PROCESS_NO_START = "start";
    public static final String FILE_NAME_CONTRACT = "合同管理";

    public static final int FOUR = 4;

    public static AtomicInteger ADMISSION_AGREEMENT_INT = new AtomicInteger();
    public static AtomicInteger INVITATION_BID_INT = new AtomicInteger();
    public static AtomicInteger RENTAL_CONTRACT_INT = new AtomicInteger();
    public static AtomicInteger PROPERTY_CONTRACT_INT = new AtomicInteger();
    public static AtomicInteger APARTMENT_CONTRACT_INT = new AtomicInteger();

    public enum ContractNoType {
        ADMISSION_AGREEMENT(1, "RYXY", "KJ-ZHCS-RYXY-%04d-%04d", ADMISSION_AGREEMENT_INT),
        INVITATION_BID(2, "ZY", "KJ-ZHCS-ZY-%04d-%04d", INVITATION_BID_INT),
        RENTAL_CONTRACT(3, "ZC", "KJ-ZHCS-ZC-%04d-%04d", RENTAL_CONTRACT_INT),
        PROPERTY_CONTRACT(4, "WY", "KJ-ZHCS-WY-%04d-%04d", PROPERTY_CONTRACT_INT),
        APARTMENT_CONTRACT(5, "XM", "KJ-ZHCS-XM-%04d-%04d", APARTMENT_CONTRACT_INT),
        ;
        @Getter
        int type;
        @Getter
        String processNoPrefix;
        @Getter
        String noFormat;
        @Getter
        AtomicInteger atomicInteger;

        ContractNoType(int type, String processNoPrefix, String noFormat, AtomicInteger atomicInteger) {
            this.type = type;
            this.noFormat = noFormat;
            this.processNoPrefix = processNoPrefix;
            this.atomicInteger = atomicInteger;
        }

        public static ContractNoType getContractNoTypeByType(int type) {
            for (ContractNoType value : values()) {
                if (value.type == type) {
                    return value;
                }
            }
            return null;
        }
    }

    public enum ContractProcess {
        ;

        @Getter
        private int state;
        @Getter
        private int auditResult;
        @Getter
        private int nextState;
        @Getter
        private String desc;

        ContractProcess(int state, int auditResult, int nextState, String desc) {
            this.state = state;
            this.auditResult = auditResult;
            this.nextState = nextState;
            this.desc = desc;
        }
    }

    public enum ContractTypeToSchemeFeeType{
        RENTAL_CONTRACT(3, Sets.newHashSet(1,7), "租赁合同：房屋租金、履约保证金"),
        PROPERTY_CONTRACT(4, Sets.newHashSet(4), "物业合同：物业费"),
        APARTMENT_CONTRACT(5, Sets.newHashSet(17,18,19,20,21,22), "公寓合同：公寓单、标、套间租金/押金"),
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

        public static Set<Integer> getFeeTypeByType(int type){
            for (ContractTypeToSchemeFeeType value : values()) {
                if (value.type == type) {
                    return value.calculationValues;
                }
            }
            return Sets.newHashSet();
        }
    }

    public enum HouseRelationType {
        CONTRACT(1, "合同关联房屋"),
        FEE_BILLING(2, "计费信息应用房源");
        public int value;
        String description;

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        HouseRelationType(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    /**
     * 计费周期
     */
    public enum BillingCycleEnum {
        ONE(1, "一次性"), MONTH(2, "月"), QUARTER(3, "季度"), YEAR(4, "年");
        public Integer status;
        private String desc;

        BillingCycleEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }


}
