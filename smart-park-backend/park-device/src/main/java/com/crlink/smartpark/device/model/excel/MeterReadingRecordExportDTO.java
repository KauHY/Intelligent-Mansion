package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MeterReadingRecordExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "设备ID")
    @ColumnWidth(20)
    private String deviceId;

    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ColumnWidth(20)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty(value = "表计名称")
    @ColumnWidth(30)
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

    @ExcelProperty(value = "数据来源", converter = DictConverter.class)
    @DictField(dictType = "METER_TYPE_READING_METHOD")
    private Integer meterType;

    @ExcelProperty(value = "抄表读数")
    private BigDecimal meterReading;

    @ExcelProperty(value = "抄表时间", converter = CommonConvert.class)
    @ColumnWidth(20)
    private LocalDateTime readingTime;

    @ExcelProperty(value = "导入备注")
    @ColumnWidth(40)
    private String importRemark;

    @ExcelProperty(value = "修正后读数")
    private BigDecimal correctedReading;

    @ExcelProperty(value = "修正备注")
    @ColumnWidth(40)
    private String remark;
}
