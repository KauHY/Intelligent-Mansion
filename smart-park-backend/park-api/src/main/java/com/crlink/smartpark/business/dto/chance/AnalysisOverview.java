package com.crlink.smartpark.business.dto.chance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class AnalysisOverview {

    @ApiModelProperty(value = "数据")
    private Map<String, Long> businessData;

    @ApiModelProperty(value = "总数")
    private Integer totalAmount = 0;

}
