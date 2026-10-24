package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.Constants.EXCEL_TIME_PATTERN;

@Data
public class MeterRecordImportDTO implements Serializable {


    @ExcelProperty(index = 0, value = "*设备ID")
    private String deviceId;


    @ExcelProperty(index = 1, value = "表计名称")
    private String deviceName;

    @ExcelProperty(index = 2, value = "*抄表读数")
    private BigDecimal meterReading;

    @ExcelProperty(index = 3, value = "*抄表时间", converter = CommonConvert.class)
    @DateTimeFormat(EXCEL_TIME_PATTERN)
    private LocalDateTime readingTime;

    @ExcelProperty(index = 4, value = "备注")
    @Length(max = 200, message = "备注不能超过200字！")
    private String importRemark;

    @ExcelIgnore
    private String meterId;

    @ExcelIgnore
    private String parkOrgId;

    @ExcelIgnore
    private Integer deviceType;

    @ExcelIgnore
    private Integer effectProperty;

    //@ApiModelProperty(value = "表计类别")
    @ExcelIgnore
    private Integer effectType;

    //@ApiModelProperty(value = "表类型")
    @ExcelIgnore
    private Integer meterType;


    //@ApiModelProperty(value = "最大量程")
    @ExcelIgnore
    private Integer maxRange;

    //@ApiModelProperty(value = "安装位置")
    @ExcelIgnore
    private String location;

    //@ApiModelProperty(value = "楼栋")
    @ExcelIgnore
    private String buildingId;

    //@ApiModelProperty(value = "所属楼层多选")
    @ExcelIgnore
    private List<Integer> floors;

    //@ApiModelProperty(value = "所属房屋多选")
    @ExcelIgnore
    private List<String> houseIds;

    //@ApiModelProperty(value = "初始读数")
    @ExcelIgnore
    private BigDecimal initialReading;

    //@ApiModelProperty(value = "初始时间")
    @ExcelIgnore
    private LocalDateTime initialTime;

}
