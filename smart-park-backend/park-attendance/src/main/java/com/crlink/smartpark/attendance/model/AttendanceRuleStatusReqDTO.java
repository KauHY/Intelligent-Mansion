package com.crlink.smartpark.attendance.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AttendanceRuleStatusReqDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "禁用：0，启用：1", required = true)
    @NotNull(message = "状态不能为空")
    @Range(max = 1, message = "状态不合法")
    private Integer enable;
}
