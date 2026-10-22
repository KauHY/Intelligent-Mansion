package com.crlink.smartpark.attendance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 员工考勤记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("att_attendance_record")
@ApiModel(value = "AttendanceRecord数据库对象", description = "员工考勤记录")
public class AttendanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "员工名")
    private String employeeName;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @ApiModelProperty(value = "日期")
    private LocalDate date;

    @ApiModelProperty(value = "上班打卡时间")
    private LocalDateTime punchInTime;

    @ApiModelProperty(value = "下班打卡时间")
    private LocalDateTime punchOutTime;

    @ApiModelProperty(value = "是否假期，0：否 1：是")
    private Integer holiday;

    @ApiModelProperty(value = "考勤结果，1：正常 2：异常")
    private Integer attendanceResult;

    @ApiModelProperty(value = "上班打卡方式，1：上下班打卡 2：外勤打卡")
    private Integer punchInType;

    @ApiModelProperty(value = "下班打卡方式，1：上下班打卡 2：外勤打卡")
    private Integer punchOutType;

    @ApiModelProperty(value = "打卡方式，1：上下班打卡 2：外勤打卡")
    private Integer punchType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
