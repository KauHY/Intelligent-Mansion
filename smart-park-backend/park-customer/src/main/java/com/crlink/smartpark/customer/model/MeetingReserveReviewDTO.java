package com.crlink.smartpark.customer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租赁会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingReserve审批请求对象", description = "租赁会议室预订")
public class MeetingReserveReviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", required = true)
    private String id;

    @ApiModelProperty(value = "审核（实际金额）", required = true)
    private BigDecimal reviewAmount;

    @ApiModelProperty(value = "审核状态 2通过 3拒绝", required = true)
    private Integer reviewStatus;

    @ApiModelProperty(value = "审批意见", required = true)
    private String reviewMsg;

}
