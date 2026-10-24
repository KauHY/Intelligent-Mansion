package com.crlink.smartpark.customer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: ReserveDetailVO
 * @Package com.crlink.smartpark.customer.model.vo
 * @Description: 时间段预约详情
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReserveDetailVO响应对象", description="会议室租赁管理")
public class ReserveDetailVO {
    @ApiModelProperty(value = "时间段类型 1上午 2下午 3晚上")
    private Integer timeType;

    @ApiModelProperty(value = "预定情况 123，可预订、不可预定，已预定")
    private Integer noPermission;

    @ApiModelProperty(value = "内容")
    private String msg;
}
