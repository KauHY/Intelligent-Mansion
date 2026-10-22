package com.crlink.smartpark.attendance.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 外勤申请记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FieldworkApplyRecord响应对象", description="外勤申请记录")
public class FieldworkApplyRecordExportDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "园区名")
    @ExcelProperty("所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ApiModelProperty(value = "公司名")
    @ExcelProperty("所属公司")
    @ColumnWidth(20)
    private String companyName;

    @ApiModelProperty(value = "申请人姓名")
    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String employeeName;

    @ExcelProperty(value = "申请时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelProperty("外勤时间")
    @ColumnWidth(40)
    private String fieldworkTimeStr;

    @ApiModelProperty(value = "地点")
    @ExcelProperty("外勤地点")
    @ColumnWidth(20)
    private String situation;

    @ApiModelProperty(value = "事由")
    @ExcelProperty("事由")
    @ColumnWidth(20)
    private String reason;

    @ExcelProperty("状态")
    @ColumnWidth(20)
    private String stateName;


}
