package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeterConsumptionRecordExportDTO implements Serializable {

    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty(value = "所属楼栋单元", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 2)
    private String buildingId;

    @ExcelProperty(value = "所属楼层", converter = CommonConvert.class)
    //@ExcelJavaType(value = List.class, GenericType = Integer.class)
    private List<Integer> floors;

    @ExcelProperty(value = "所属房屋", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 3)
    private List<String> houseIds;

    @ExcelProperty(value = "设备ID")
    private String deviceId;

    @ExcelProperty(value = "表计名称")
    private String deviceName;

    @ExcelProperty(value = "能耗类别", converter = DictConverter.class)
    @DictField(dictType = "ENERGY_CONSUMPTION_CATEGORY")
    private Integer deviceType;

    @ExcelProperty(value = "表计属性", converter = DictConverter.class)
    @DictField(dictType = "METER_EFFECT_PROPERTY")
    private Integer effectProperty;

//    @ExcelProperty(value = "表计类别", converter = DictConverter.class)
//    @DictField(dictType = "ELECTRICITY_METER_EFFECT_TYPE")
    @ExcelProperty(value = "表计类别")
    private String effectType;

    @ExcelProperty(value = "结算月份")
    private String settledMonth;

    @ExcelProperty(value = "上期结算时间", converter = CommonConvert.class)
    private LocalDateTime lastTime;

    @ExcelProperty(value = "本期结算时间", converter = CommonConvert.class)
    private LocalDateTime thisTime;

    @ExcelProperty(value = "上月读数")
    private BigDecimal lastReading;

    @ExcelProperty(value = "本月读数")
    private BigDecimal thisReading;

    @ExcelProperty(value = "本月实际用量")
    private BigDecimal consumption;
}
