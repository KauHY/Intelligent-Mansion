package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WarningStatisticsVO implements Serializable {
    @ApiModelProperty("已处理")
    private Integer handledCount;
    @ApiModelProperty("未处理")
    private Integer unhandledCount;
    @ApiModelProperty("总数")
    private Integer totalCount;

    public WarningStatisticsVO(Integer handledCount, Integer unhandledCount, Integer totalCount) {
        this.handledCount = handledCount;
        this.unhandledCount = unhandledCount;
        this.totalCount = totalCount;
    }
}
