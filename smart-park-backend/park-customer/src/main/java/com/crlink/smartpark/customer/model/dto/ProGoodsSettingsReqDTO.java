package com.crlink.smartpark.customer.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: ProGoodsSettingsReqDTO
 * @Package com.crlink.smartpark.customer.model.dto
 * @Description: 物品管理预警值设置
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProGoodsSettings请求对象", description = "物品管理")
public class ProGoodsSettingsReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", required = true)
    @NotBlank(message = "主键id不能为空")
    private String id;

    @ApiModelProperty(value = "库存下限预警值", required = true)
    @NotNull(message = "库存下限预警值不能为空")
    @Min(value = 1)
    private Integer minStock;
}
