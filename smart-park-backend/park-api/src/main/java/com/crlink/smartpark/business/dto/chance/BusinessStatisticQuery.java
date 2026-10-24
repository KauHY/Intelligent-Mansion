package com.crlink.smartpark.business.dto.chance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class BusinessStatisticQuery {

    @ApiModelProperty(value = "园区id")
    @NotBlank(message = "园区id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

}
