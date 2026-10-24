package com.crlink.smartpark.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class GetConsumptionByContract implements Serializable {

    @ApiModelProperty("合同Id")
    @NotBlank(message = "合同Id不能为空")
    private String contractId;

    @ApiModelProperty("0房屋用量记录，1企业用量记录")
    @NotNull(message = "用量记录类型不能为空")
    private Integer type;

    @ApiModelProperty("房屋Id")
    private String houseId;
}
