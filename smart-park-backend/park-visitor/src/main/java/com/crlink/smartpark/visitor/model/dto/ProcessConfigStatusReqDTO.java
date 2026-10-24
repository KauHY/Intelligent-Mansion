package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProcessConfigStatusReqDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "禁用：0，启用：1", required = true)
    @NotNull
    @Range(max = 1)
    private Integer enable;
}
