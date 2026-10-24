package com.crlink.smartpark.meeting.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.BUILDING_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 会议室平面图
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingPlaneFigure响应对象", description="会议室平面图")
public class MeetingPlaneFigureVO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @Id2objectField(code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "所属楼栋单元")
    private String buildingId;

    @ApiModelProperty("楼栋名")
    @Id2objectField (code = BUILDING_NAME_CODE)
    private String buildingName;

    @ApiModelProperty(value = "所在楼层")
    private String floorId;

    @ApiModelProperty(value = "会议室平面图")
    private String meetingPlaneFigure;


}
