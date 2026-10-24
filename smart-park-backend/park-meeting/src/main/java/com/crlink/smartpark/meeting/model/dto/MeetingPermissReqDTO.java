package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会议室权限关联
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingPermission请求对象", description = "会议室权限关联")
public class MeetingPermissReqDTO implements Serializable {

    @ApiModelProperty(value = "关联类型 1公司 2员工")
    private Integer relationType;

    @ApiModelProperty(value = "关联公司id")
    private String companyId;

    @ApiModelProperty(value = "关联员工id")
    private String employeeId;


}
