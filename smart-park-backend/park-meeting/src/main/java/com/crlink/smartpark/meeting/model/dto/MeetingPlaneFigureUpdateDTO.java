package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: MeetingPlaneFigureUpdateDTO
 * @Package com.crlink.smartpark.meeting.model.dto
 * @Description: MeetingPlaneFigure更新对象
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingPlaneFigure更新对象", description="会议室平面图")
public class MeetingPlaneFigureUpdateDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;


    @ApiModelProperty(value = "会议室平面图")
    @NotEmpty(message = "会议室分布图不能为空")
    private String meetingPlaneFigure;

}
