package com.crlink.smartpark.meeting.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 会议室预订表
 * </p>
 * */
@Data
public class MeetingReserveQuery implements Serializable {

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "所属园区")
    @NotEmpty(message = "所属园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元")
    @NotEmpty(message = "所属楼栋单元不能为空")
    private String buildingId;

    @ApiModelProperty(value = "所在楼层")
    @NotEmpty(message = "所在楼层不能为空")
    private String floorId;

    @ApiModelProperty(value = "日期")
    @NotEmpty(message = "日期不能为空")
    private String nowDate;


}
