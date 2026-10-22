package com.crlink.smartpark.attendance.model;

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
@ApiModel(value = "AttendanceRecord请求对象", description = "员工考勤记录")
public class AttendanceRecordReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工ID", required = true)
    private String employeeId;

    @ApiModelProperty(value = "员工名", required = true)
    private String employeeName;

    @ApiModelProperty(value = "园区ID", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "园区名", required = true)
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID", required = true)
    private String companyId;

    @ApiModelProperty(value = "公司名", required = true)
    private String companyName;

    @ApiModelProperty(value = "日期", required = true)
    private LocalDate date;

    @ApiModelProperty(value = "上班打卡时间", required = true)
    private LocalDateTime punchInTime;

    @ApiModelProperty(value = "下班打卡时间", required = true)
    private LocalDateTime punchOutTime;

    @ApiModelProperty(value = "是否假期，0：否 1：是", required = true)
    private Integer holiday;

    @ApiModelProperty(value = "考勤结果，1：正常 2：异常", required = true)
    private Integer status;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "修改人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "删除状态", required = true)
    private Boolean deleted;


}
