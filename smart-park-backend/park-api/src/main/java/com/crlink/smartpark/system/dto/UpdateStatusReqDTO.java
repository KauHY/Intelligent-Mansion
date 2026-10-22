package com.crlink.smartpark.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class UpdateStatusReqDTO implements Serializable {
    @Min(0)
    private Integer status;

    @ApiModelProperty(value = "id")
//    @NotNull(message = "id不能为空")
    private String id;
}
