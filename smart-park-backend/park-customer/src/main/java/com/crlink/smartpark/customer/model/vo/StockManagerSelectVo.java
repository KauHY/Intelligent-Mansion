package com.crlink.smartpark.customer.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 库存管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProStockManager请求对象", description="库存管理")
public class StockManagerSelectVo implements Serializable {

    @ApiModelProperty(value = "库存id")
    private String id;

    @ApiModelProperty(value = "物品ID")
    private String goodsId;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "库存数量")
    private Integer stockNum;



}
