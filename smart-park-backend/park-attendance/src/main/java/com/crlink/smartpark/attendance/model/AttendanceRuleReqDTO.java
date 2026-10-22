package com.crlink.smartpark.attendance.model;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 考勤规则
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AttendanceRule请求对象", description = "考勤规则")
public class AttendanceRuleReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "规则名称", required = true)
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @ApiModelProperty(value = "上班打卡时间", required = true)
    @NotNull(message = "上班打卡时间不能为空")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT, timezone = Constants.TIMEZONE)
    private LocalTime punchInTime;

    @ApiModelProperty(value = "下班打卡时间", required = true)
    @NotNull(message = "下班打卡时间不能为空")
    @JsonFormat(pattern = Constants.DEFAULT_TIME_MINUTES_FORMAT, timezone = Constants.TIMEZONE)
    private LocalTime punchOutTime;

    @ApiModelProperty(value = "考勤地点", required = true)
    @NotBlank(message = "考勤地点不能为空")
    private String location;

    @ApiModelProperty(value = "经度", required = true)
    @NotNull(message = "经度不能为空")
    private Double longitude;

    @ApiModelProperty(value = "纬度", required = true)
    @NotNull(message = "纬度不能为空")
    private Double latitude;

    @ApiModelProperty(value = "关联工作日", required = true)
    private List<Integer> daysOfWork;

    @ApiModelProperty(value = "关联工作日类型，0：法定工作日 1：自定义", required = true)
    @NotNull(message = "关联工作日类型不能为空")
    @Range(max = 1, message = "关联工作日类型不合法")
    private Integer workdayType;

    @ApiModelProperty(value = "考勤范围（米）", required = true)
    @NotNull(message = "考勤范围（米）不能为空")
    @Range(max = 1000, message = "考勤范围（米）必须小于等于1000米")
    private Integer punchRange;

    @ApiModelProperty(value = "园区ID", required = true)
    @NotBlank(message = "园区ID不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "公司ID", required = true)
    @NotBlank(message = "公司ID不能为空")
    private String companyId;

    @ApiModelProperty(value = "规则适用类型，0：全公司员工适用，1：指定员工", required = true)
    @NotNull(message = "规则适用类型不能为空")
    @Range(max = 1, message = "规则适用类型不合法")
    private Integer applyType;

    @ApiModelProperty(value = "关联员工ID集合")
    private Set<String> employeeIds;

}
