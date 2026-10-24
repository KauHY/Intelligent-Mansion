package com.crlink.smartpark.fee.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "CalculationReqDTO对象", description = "计费标准")
public class CalculationReqDTO {

    @ApiModelProperty(value = "计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价；5-房间数*单价", required = true)
    @NotNull(message = "计费标准不能为空")
    private Integer calculationStandard;

    @ApiModelProperty(value = "单价", required = true)
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "建筑面积队列")
    private List<BigDecimal> buildingAreas;

    @ApiModelProperty(value = "套内面积队列")
    private List<BigDecimal> coverAreas;

    @ApiModelProperty(value = "用量")
    private BigDecimal dosage;

    @ApiModelProperty(value = "房间数")
    private BigDecimal numberOfRooms;

    @ApiModelProperty(value = "计费开始日期")
    private LocalDate billingStartDate;

    @ApiModelProperty(value = "计费结束日期")
    private LocalDate billingEndDate;

    @ApiModelProperty(value = "保留位数：0-取整数；1-保留一位小数；2-保留两位小数", required = true)
    @NotNull(message = "保留位数不能为空")
    private Integer reservedDigits;

    @ApiModelProperty(value = "进位方式：4-四舍五入；0-向上取整；1-向下取整", required = true)
    @NotNull(message = "进位方式不能为空")
    private Integer carryMode;

    /**
     * 免租期（天）
     */
    @ApiModelProperty(value = "免租期（天）")
    private Integer freeRentDay;

    @ApiModelProperty(value = "费用类型")
    private Integer feeType;


}
