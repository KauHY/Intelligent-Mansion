package com.crlink.smartpark.device.common;

public interface FireControlConstant {

    String FIRE_CONTROL_DEVICE_EXPORT_FILENAME = "消防设备管理";

    //字典值type
    String SYSTEM_TYPE = "SYSTEM_TYPE";


    //String READING_RECORD_EXPORT_FILENAME = "抄表记录";
    //
    //String E_METER_EXPORT_FILENAME = "电表管理";
    //String W_METER_EXPORT_FILENAME = "水表管理";

    enum FireWarningRecordStatusEnum {
        PENDING_STATUS(0, "待处理"), PROCESSED_STATUS(1, "已处理");
        public Integer status;
        private String desc;

        FireWarningRecordStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }
    //
    ////已开启、已停用、已报废、已换表
    //enum MeterStatusEnum {
    //    USING(1, "已开启"), STOP_USING(2, "已停用"), DISCARD(3, "已报废"), REPLACE(4, "已换表");
    //    public Integer status;
    //    private String desc;
    //
    //    MeterStatusEnum(int status, String desc) {
    //        this.status = status;
    //        this.desc = desc;
    //    }
    //}
}
