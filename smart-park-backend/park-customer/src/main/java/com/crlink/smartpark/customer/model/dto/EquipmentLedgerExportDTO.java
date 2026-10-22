package com.crlink.smartpark.customer.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EquipmentLedgerExportDTO响应对象", description="设备台账")
public class EquipmentLedgerExportDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ExcelProperty("设备名称")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty("设备编码")
    @ColumnWidth(20)
    private String deviceCode;

    @ExcelProperty("安装楼栋")
    @ColumnWidth(20)
    private String buildingName;

    @ExcelProperty("安装楼层")
    @ColumnWidth(20)
    private String floor;

    @ExcelProperty("详细位置")
    @ColumnWidth(20)
    private String installationLocation;

    @ExcelProperty("设备分类")
    @ColumnWidth(20)
    private String category;

    @ExcelProperty("状态")
    @ColumnWidth(20)
    private String stateName ;

    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelProperty(value = "报废日期", converter = LocalDateConverter.class)
    @ColumnWidth(20)
    private LocalDate scrapDate;

}
