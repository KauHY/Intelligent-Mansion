package com.crlink.smartpark.meeting.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 会议室平面图
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingPlaneFigure请求对象", description="会议室平面图")
public class MeetingPlaneFigureReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "所属园区")
    @NotEmpty(message = "所属园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "所属楼栋单元")
    @NotEmpty(message = "所属楼栋单元不能为空")
    private String buildingId;

    @ApiModelProperty(value = "所在楼层")
    @NotEmpty(message = "所在楼层不能为空")
    private String floorId;

    @ApiModelProperty(value = "会议室平面图")
    @NotEmpty(message = "会议室分布图不能为空")
    private String meetingPlaneFigure;

}
