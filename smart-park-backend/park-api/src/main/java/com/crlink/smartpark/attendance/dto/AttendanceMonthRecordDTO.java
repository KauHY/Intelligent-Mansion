package com.crlink.smartpark.attendance.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 考勤月度统计
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AttendanceMonthRecord响应对象", description = "考勤月度统计")
public class AttendanceMonthRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ApiModelProperty(value = "园区id")
    @ExcelIgnore
    private String parkOrgId;

    @ApiModelProperty(value = "员工id")
    @ExcelIgnore
    private String employeeId;

    @ApiModelProperty(value = "园区名")
    @ExcelProperty("所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ApiModelProperty(value = "公司名")
    @ExcelProperty("所属公司")
    @ColumnWidth(20)
    private String companyName;

    @ApiModelProperty(value = "员工名")
    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String employeeName;

    @ApiModelProperty(value = "年份")
    @ExcelProperty("年份")
    @ColumnWidth(20)
    private Integer year;

    @ApiModelProperty(value = "月份")
    @ExcelProperty("月份")
    @ColumnWidth(20)
    private Integer month;

    @ApiModelProperty(value = "异常考勤次数")
    @ExcelProperty("打卡异常天数")
    @ColumnWidth(20)
    private Integer exceptionTimes;

    @ApiModelProperty(value = "缺勤次数")
    @ExcelProperty("未出勤天数")
    @ColumnWidth(20)
    private Integer absenceTimes;

}
