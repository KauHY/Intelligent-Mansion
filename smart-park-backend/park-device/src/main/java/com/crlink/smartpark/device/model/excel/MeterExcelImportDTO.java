package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.LocalDateExcelConverter;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 仪表（电表和水表）
 * </p>
 *
  */
@Data
public class MeterExcelImportDTO implements Serializable {


    private static final long serialVersionUID = 1L;
    @ExcelIgnore
    private String id;
    @ExcelIgnore
    private String parkOrgId;

    @ExcelIgnore
    private Integer deviceType;

    @ExcelProperty(value = "*表计名称")
    //@ExcelProperty(index = 0, value = "*表计名称")
    @NotBlank(message = "名称不能为空")
    private String deviceName;

    @ExcelProperty(value = "*表计属性", converter = DictConverter.class)
    //@ExcelProperty(index = 1, value = "*表计属性", converter = DictConverter.class)
    @DictField(dictType = "METER_EFFECT_PROPERTY")
    @NotNull(message = "表计属性不能为空")
    private Integer effectProperty;

    //    @ExcelProperty(value = "*表计类别")
    @ExcelProperty(value = "*表计类别", converter = DictConverter.class)
    @DictField(dictType = "ELECTRICITY_METER_EFFECT_TYPE")
    private Integer effectType;

    @ExcelProperty(value = "*表类型", converter = DictConverter.class)
//    @ExcelProperty(index = 3, value = "*表类型", converter = DictConverter.class)
    @DictField(dictType = "METER_TYPE")
    @NotNull(message = "表类型不能为空")
    private Integer meterType;


    @ExcelProperty(value = "*设备ID")
//    @ExcelProperty(index = 8, value = "*设备ID")
//    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    @ExcelProperty(value = "*最大量程")
//    @ExcelProperty(index = 9, value = "*最大量程")
    @NotNull(message = "最大量程不能为空")
    @Max(value = 99999999, message = "最大量程有误")
    @Min(value = 0, message = "最大量程有误")
    private Integer maxRange;

    @ExcelProperty(value = "安装位置")
//    @ExcelProperty(index = 10, value = "安装位置")
    private String location;

    @ExcelProperty(value = "*计量楼栋")
//    @ExcelProperty(index = 11, value = "*计量楼栋")
    @NotEmpty(message = "计量楼栋不能为空")
    private String buildingId;

    @ExcelProperty(value = "*计量楼层", converter = CommonConvert.class)
//    @ExcelProperty(index = 12, value = "*计量楼层", converter = CommonConvert.class)
    @NotEmpty(message = "计量楼层不能为空")
    private List<Integer> floors;

    @ExcelProperty(value = "计量房屋", converter = CommonConvert.class)
//    @ExcelProperty(index = 13, value = "计量房屋", converter = CommonConvert.class)
    private List<String> houseIds;

    @ExcelProperty(value = "*初始读数")
//    @ExcelProperty(index = 14, value = "*初始读数")
    @NotNull(message = "初始读数不能为空")
    @DecimalMin(value = "0.00", message = "初始读数仅支持大于等于0的数字")
    private BigDecimal initialReading;

    //    @ExcelProperty(value = "*初始时间", converter = CommonConvert.class)
//    @ExcelProperty(index = 15, value = "*初始时间", converter = CommonConvert.class)
//    @NotNull(message = "初始时间不能为空")
//    @DateTimeFormat(EXCEL_DATE_PATTERN)
//    private LocalDateTime initialTime;
    @ExcelProperty(value = "*初始时间", converter = LocalDateExcelConverter.class)
//    @ExcelProperty(index = 15, value = "*初始时间", converter = LocalDateExcelConverter.class)
    @NotNull(message = "初始时间不能为空")
    private LocalDate initialTime;

    @ExcelProperty(value = "备注")
    @Length(max = 200, message = "备注不能超过200字")
    private String remark;


}
