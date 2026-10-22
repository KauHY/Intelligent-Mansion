package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "ChargeDetail对象", description = "收费明细")
public class ChargeDetail {
    @ApiModelProperty(value = "收费项")
    private String chargeName;

    @ApiModelProperty(value = "金额（元）")
    private BigDecimal amountCount;

    @ApiModelProperty(value = "本月示数")
    private BigDecimal thisReading;

    @ApiModelProperty(value = "上月底数")
    private BigDecimal lastReading;

    @ApiModelProperty("本月应摊用量")
    private BigDecimal shareAmount;

    @ApiModelProperty("本月用量")
    private BigDecimal roomAmount;

    @ApiModelProperty("本月公共用量")
    private BigDecimal publicAmount;

    @ApiModelProperty("本月实际用量")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "分摊房间数")
    private int shareCount;

    @ApiModelProperty(value = "类型 9电费/公摊电费、10水费/公摊水费,")
    private Integer type;

    @ApiModelProperty(value = "true需要分摊，false不需要分摊")
    private Boolean needShare;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseIds;

    @ApiModelProperty(value = "房源名称")
    private List<String> houseName;

}
