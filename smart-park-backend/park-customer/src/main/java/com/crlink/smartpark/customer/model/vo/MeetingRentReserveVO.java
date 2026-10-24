package com.crlink.smartpark.customer.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.customer.dto.MeetingRentDTO;
import com.crlink.smartpark.customer.dto.MeetingScheduleTimeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MeetingRentReserveVO
 * @Package com.crlink.smartpark.customer.model.vo
 * @Description: 会议室预定情况VO
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingRentReserve响应对象", description="会议室租赁管理")
public class MeetingRentReserveVO {

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "会议室基础信息")
    @Id2objectNestingField()
    private MeetingRentDTO meetingRentDTO;

    @ApiModelProperty(value = "可预订时段集合")
    private List<MeetingScheduleTimeDTO> timeList;

    @ApiModelProperty(value = "会议时间区段详情")
    private Map<LocalDate,List<ReserveDetailVO>> reserveDetailVOS;


}
