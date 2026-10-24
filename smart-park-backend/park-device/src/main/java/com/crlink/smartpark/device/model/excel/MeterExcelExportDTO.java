package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 仪表（电表和水表）
 * </p>
 *
  */
@Data
public class MeterExcelExportDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ExcelProperty(value = "设备ID")
    private String deviceId;

    @ExcelProperty(value = "表计名称")
    private String deviceName;


    @ExcelProperty(value = "表计属性", converter = DictConverter.class)
    @DictField(dictType = "METER_EFFECT_PROPERTY")
    private Integer effectProperty;

    @ExcelProperty(value = "表计类别", converter = DictConverter.class)
    @DictField(dictType = "ELECTRICITY_METER_EFFECT_TYPE")
    private Integer effectType;

    @ExcelProperty(value = "抄表方式", converter = DictConverter.class)
    @DictField(dictType = "METER_TYPE_READING_METHOD")
    private Integer meterType;

    @ExcelProperty(value = "运行状态", converter = DictConverter.class)
    @DictField(dictType = "DEVICE_STATUS")
    private Integer status;

    @ExcelProperty(value = "表计状态", converter = DictConverter.class)
    @DictField(dictType = "METER_STATUS")
    private Integer usingStatus;

}
