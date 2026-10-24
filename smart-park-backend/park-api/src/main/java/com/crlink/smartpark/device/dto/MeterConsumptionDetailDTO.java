package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 企业或房屋水电用量
 * </p>
 * */
@Data
@ApiModel(description = "企业或房屋水电用量")
public class MeterConsumptionDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类别，1自用表计室内，2公摊表计，公共")
    private Integer categoryType;

    @ApiModelProperty(value = "水表或电表用量,5水，6电")
    private Integer deviceType;

    @ApiModelProperty(value = "水表或电表名称")
    private String deviceName;

    @ApiModelProperty(value = "上次抄表读数")
    private BigDecimal lastReading;

    @ApiModelProperty(value = "本次抄表读数")
    private BigDecimal thisReading;

    @ApiModelProperty(value = "本月实际用量")
    private BigDecimal consumption;

    @ApiModelProperty(value = "分摊房间数")
    //@JsonIgnore
    private int shareCount = 1;


}
