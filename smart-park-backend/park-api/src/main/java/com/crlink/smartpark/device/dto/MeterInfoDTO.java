package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 仪表（电表和水表）
 * </p>
 *
  */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MeterInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表类型")
    private Integer meterType;

    @ApiModelProperty(value = "表计属性")
    private Integer effectProperty;

    @ApiModelProperty(value = "表计类别")
    private Integer effectType;

    @ApiModelProperty(value = "最大量程")
    private Integer maxRange;

    @ApiModelProperty(value = "楼栋")
    private String buildingId;

    @ApiModelProperty(value = "所属楼层多选")
    private List<Integer> floors;

    @ApiModelProperty(value = "所属房屋多选")
    private List<String> houseIds;

    @ApiModelProperty(value = "初始读数")
    private BigDecimal initialReading;

    @ApiModelProperty(value = "初始时间")
    private LocalDateTime initialTime;

}
