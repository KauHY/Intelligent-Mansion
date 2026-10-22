package com.crlink.smartpark.visitor.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.common.excel.convert.ImageConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.common.excel.convert.StatusConverter;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 * */
@Data
@ContentRowHeight(150)
public class ApartmentPersonExportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    @ColumnWidth(20)
    private String parkOrgId;


    @ExcelProperty(value = "所属企业", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 4)
    @ColumnWidth(20)
    private String companyId;

    @ExcelProperty(value = "姓名", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 6)
    @ColumnWidth(20)
    private String employeeId;


    @ApiModelProperty(value = "手机号")
    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String mobile;

    @ApiModelProperty(value = "身份证号")
    @ExcelProperty("身份证号")
    @ColumnWidth(20)
    private String idCardNumber;

    @ApiModelProperty(value = "照片")
    @ExcelProperty(value = "照片", converter = ImageConverter.class)
    @ColumnWidth(30)
    private String pics;

    @ExcelProperty("关联房间")
    @ColumnWidth(20)
    private String houseName;

    @ExcelProperty(value = "房屋功能", converter = DictConverter.class)
    @DictField(dictType = "HOUSE_FUNCTION")
    @ColumnWidth(20)
    private Integer houseFunction;

    @ExcelProperty(value = "创建日期", converter = LocalDateConverter.class)
    @ColumnWidth(20)
    private LocalDate createDate;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    @ColumnWidth(20)
    private Integer status;

}
