package com.crlink.smartpark.customer.model.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.crlink.smartpark.common.excel.convert.LocalDateExcelConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "EquipmentLedgerExportDTO响应对象", description = "设备台账")
public class EquipmentLedgerImportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设备名不能为空")
    @Length(max = 30, message = "设备名最长30个字符")
    @ExcelProperty(value = "*设备名称")
    private String name;

    @NotBlank(message = "设备分类不能为空")
    @ExcelProperty(value = "*设备分类")
    private String categoryName;

    @NotBlank(message = "安装位置-楼栋不能为空")
    @ExcelProperty(value = "*安装位置-楼栋")
    private String buildingName;

    @NotNull(message = "安装位置-楼层不能为空")
    @ExcelProperty(value = "*安装位置-楼层")
    private String floor;

    @Length(max = 30)
    @ExcelProperty(value = "设备品牌")
    private String brand;

    @ApiModelProperty(value = "详细位置")
    @Length(max = 30)
    @ExcelProperty(value = "详细位置")
    private String installationLocation;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "规格型号")
    @Length(max = 30)
    @ExcelProperty(value = "规格型号")
    private String specifications;

    @ApiModelProperty(value = "安装日期")
    @ExcelProperty(value = "安装日期", converter = LocalDateExcelConverter.class)
    private LocalDate installationDate;

    @ApiModelProperty(value = "原厂编号")
    @Length(max = 30)
    @ExcelProperty(value = "原厂编号")
    private String originalNumber;

    @ApiModelProperty(value = "生产厂家")
    @Length(max = 30)
    @ExcelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "维保单位")
    @Length(max = 30)
    @ExcelProperty(value = "维保单位")
    private String manufacturerUnit;

    @ApiModelProperty(value = "维保电话")
    @Length(max = 30)
    @ExcelProperty(value = "维保电话")
    private String manufacturerMobile;

    /**
     * 设备分类ID
     */
    @ExcelIgnore
    private String categoryId;

}
