package com.crlink.smartpark.system.dto.platform;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class TenantUpdateStatusDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    @NotBlank(message = "主键id不能为空")
    private String id;

    @ApiModelProperty(value = "状态")
    @Min(0)
    @Max(1)
    private Integer status;

}
