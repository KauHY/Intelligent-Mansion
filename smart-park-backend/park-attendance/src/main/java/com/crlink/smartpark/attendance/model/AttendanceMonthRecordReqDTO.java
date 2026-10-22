package com.crlink.smartpark.attendance.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 考勤月度统计
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AttendanceMonthRecord请求对象", description = "考勤月度统计")
public class AttendanceMonthRecordReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工id", required = true)
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

    @ApiModelProperty(value = "年份", required = true)
    private Integer year;

    @ApiModelProperty(value = "月份", required = true)
    private Integer month;

    @ApiModelProperty(value = "异常考勤次数", required = true)
    private Integer exceptionTimes;

    @ApiModelProperty(value = "缺勤次数", required = true)
    private Integer absenceTimes;

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
