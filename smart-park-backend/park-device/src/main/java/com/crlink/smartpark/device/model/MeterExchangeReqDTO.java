package com.crlink.smartpark.device.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MeterExchangeReqDTO implements Serializable {
    //@ApiModelProperty(value = "能耗类别（1电能耗，2水能耗）")
    //@CustomValid({"5", "6"})
    //private Integer deviceType;
    @ApiModelProperty(value = "旧表id")
    @NotEmpty
    private String oldMeterId;

    @ApiModelProperty(value = "新表读数")
    @Min(0)
    private BigDecimal oldReading;

    @ApiModelProperty(value = "新表deviceId")
    @Length(max = 30)
    private String newDeviceId;

    @ApiModelProperty(value = "新表读数")
    @Min(0)
    private BigDecimal newReading;
    @ApiModelProperty(value = "新表最大量程")
    @Min(0)
    private Integer newMaxRange;
}
