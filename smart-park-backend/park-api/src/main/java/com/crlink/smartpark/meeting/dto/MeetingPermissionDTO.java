package com.crlink.smartpark.meeting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议室权限关联
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingPermission响应对象", description = "会议室权限关联")
public class MeetingPermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "关联类型 1公司 2员工")
    private Integer relationType;

    @ApiModelProperty(value = "关联公司id")
    private String companyId;

    @ApiModelProperty(value = "关联公司")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "关联员工id")
    private String employeeId;

    @ApiModelProperty(value = "关联员工姓名")
    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;

}
