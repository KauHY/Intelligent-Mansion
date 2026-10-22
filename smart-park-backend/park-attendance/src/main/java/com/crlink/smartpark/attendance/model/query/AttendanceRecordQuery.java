package com.crlink.smartpark.attendance.model.query;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

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
@ApiModel(value = "AttendanceRecord查询对象", description = "员工考勤记录")
public class AttendanceRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate date;

    @ApiModelProperty(value = "上班时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime punchInTime;

    @ApiModelProperty(value = "下班时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime punchOutTime;

    @ApiModelProperty(value = "考勤结果，1：正常 2：异常")
    private Integer attendanceResult;

    @ApiModelProperty(value = "打卡方式，1：上下班打卡 2：外勤打卡")
    @Range(min = 1, max = 2, message = "打卡方式不合法")
    private Integer punchType;

    @ApiModelProperty(value = "员工名")
    private String employeeName;

}
