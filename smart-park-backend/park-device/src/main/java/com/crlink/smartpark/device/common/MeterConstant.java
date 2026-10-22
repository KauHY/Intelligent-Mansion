package com.crlink.smartpark.device.common;

public interface MeterConstant {

    String CONSUMPTION_RECORD_EXPORT_FILENAME = "用量记录";
    String READING_RECORD_EXPORT_FILENAME = "抄表记录";

    String E_METER_EXPORT_FILENAME = "电表管理";
    String W_METER_EXPORT_FILENAME = "水表管理";


    String COMPANY_CONSUMPTION_EXPORT_FILENAME = "企业用量记录";
    String HOUSE_CONSUMPTION_EXPORT_FILENAME = "合同房屋用量记录";


    enum MeteTypeEnum {
        GENERAL_METER(1, "普通表"), INTELLIGENT_METER(2, "智能表");
        public Integer type;
        private String desc;

        MeteTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    //已开启、已停用、已报废、已换表
    enum MeterStatusEnum {
        USING(1, "已开启"), STOP_USING(2, "已停用"), DISCARD(3, "已报废"), REPLACE(4, "已换表");
        public int status;
        private String desc;

        MeterStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }
    }
}
