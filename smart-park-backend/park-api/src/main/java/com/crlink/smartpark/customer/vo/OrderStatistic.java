package com.crlink.smartpark.customer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class OrderStatistic implements Serializable {

    @ApiModelProperty(value = "已完成数量")
    private Integer finishCount;

    @ApiModelProperty(value = "进行中数量")
    private Integer ongoingCount;

    public OrderStatistic(Integer finishCount, Integer ongoingCount) {
        this.finishCount = finishCount;
        this.ongoingCount = ongoingCount;
    }

}
