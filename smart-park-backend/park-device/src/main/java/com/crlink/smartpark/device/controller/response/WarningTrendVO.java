package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WarningTrendVO implements Serializable {
    @ApiModelProperty("能耗告警数量")
    private Integer meterWarningCount;
    @ApiModelProperty("消防报警数量")
    private Integer fireWarningCount;
    @ApiModelProperty("总数")
    private Integer totalCount;

    private Integer month;

    public WarningTrendVO(Integer meterWarningCount, Integer fireWarningCount, Integer totalCount, Integer month) {
        this.meterWarningCount = meterWarningCount;
        this.fireWarningCount = fireWarningCount;
        this.totalCount = totalCount;
        this.month = month;
    }
}
