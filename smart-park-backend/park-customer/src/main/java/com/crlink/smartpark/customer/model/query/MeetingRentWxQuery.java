package com.crlink.smartpark.customer.model.query;

import java.io.Serializable;

import com.crlink.smartpark.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: MeetingRentReserveVO
 * @Package com.crlink.smartpark.customer.model.vo
 * @Description: 会议室预定情况查询
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingRentReserve查询对象", description = "会议室租赁管理")
public class MeetingRentWxQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "月份")
    private String month;

}
