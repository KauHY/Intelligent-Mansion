package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserve请求对象", description="会议室预订")
public class MeetingReserveUpdateDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "会议开始时间")
    private LocalDateTime meetingTimeStart;

    @ApiModelProperty(value = "会议结束时间")
    private LocalDateTime meetingTimeEnd;

    @ApiModelProperty(value = "会议状态：0候补中（不展示） 1未开始、2进行中、3已结束、4已取消")
    private Integer meetingStatus;


}
