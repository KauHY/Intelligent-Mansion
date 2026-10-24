package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName: MeetingManageStatusDTO
 * @Package com.crlink.smartpark.meeting.model.dto
 * @Description: MeetingManage启用禁用会议室
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingManage启用禁用请求对象", description="会议室管理")
public class MeetingManageStatusDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "状态：1开启 0关闭")
    private Integer status;

}