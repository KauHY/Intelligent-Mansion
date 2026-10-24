package com.crlink.smartpark.attendance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalTime;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 考勤规则
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "att_attendance_rule", autoResultMap = true)
@ApiModel(value = "AttendanceRule数据库对象", description = "考勤规则")
public class AttendanceRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "上班打卡时间")
    private LocalTime punchInTime;

    @ApiModelProperty(value = "下班打卡时间")
    private LocalTime punchOutTime;

    @ApiModelProperty(value = "考勤地点")
    private String location;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "关联工作日")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<Integer> daysOfWork;

    @ApiModelProperty(value = "关联工作日类型，0：法定工作日 1：自定义")
    private Integer workdayType;

    @ApiModelProperty(value = "考勤范围（米）")
    private Integer punchRange;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "公司ID")
    private String companyId;

    @ApiModelProperty(value = "规则适用类型，0：全公司员工适用，1：指定员工")
    private Integer applyType;

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
