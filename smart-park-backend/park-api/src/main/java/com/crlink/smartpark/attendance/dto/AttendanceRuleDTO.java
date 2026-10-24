package com.crlink.smartpark.attendance.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 考勤规则
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AttendanceRule响应对象", description = "考勤规则")
public class AttendanceRuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "上班打卡时间")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT)
    private LocalTime punchInTime;

    @ApiModelProperty(value = "下班打卡时间")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT)
    private LocalTime punchOutTime;

    @ApiModelProperty(value = "考勤地点")
    private String location;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "关联工作日")
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

    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "公司名")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "规则适用类型，0：全公司员工适用，1：指定员工")
    private Integer applyType;
}
