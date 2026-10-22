package com.crlink.smartpark.attendance.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.excel.convert.AttendanceResultConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工考勤记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AttendanceRecord响应对象", description = "员工考勤记录")
public class AttendanceRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    private String id;

    @ApiModelProperty(value = "园区id")
    @ExcelIgnore
    private String parkOrgId;

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

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    @ExcelProperty(value = "日期", converter = LocalDateConverter.class)
    @ColumnWidth(20)
    private LocalDate date;

    @ApiModelProperty(value = "上班打卡时间")
    @ExcelProperty(value = "上班时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime punchInTime;

    @ApiModelProperty(value = "下班打卡时间")
    @ExcelProperty(value = "下班时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime punchOutTime;

    @ApiModelProperty(value = "是否假期，0：否 1：是")
    @ExcelIgnore
    private Integer holiday;

    @ApiModelProperty(value = "考勤结果，1：正常 2：异常")
    @ExcelProperty(value = "考勤结果", converter = AttendanceResultConverter.class)
    @ColumnWidth(20)
    private Integer attendanceResult;

    @ApiModelProperty(value = "上班打卡方式，1：上下班打卡 2：外勤打卡")
    @ExcelIgnore
    private Integer punchInType;

    @ApiModelProperty(value = "下班打卡方式，1：上下班打卡 2：外勤打卡")
    @ExcelIgnore
    private Integer punchOutType;

}
