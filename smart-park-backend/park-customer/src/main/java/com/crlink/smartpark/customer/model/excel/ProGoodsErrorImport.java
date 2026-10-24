package com.crlink.smartpark.customer.model.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 物品管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProGoodsErrorImport implements Serializable {


    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "物品类别")
    private Integer goodsType;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "库存下限预警值")
    private Integer minStock;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "入库单价(元)")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "异常原因*")
    private String errorMsg;


}
