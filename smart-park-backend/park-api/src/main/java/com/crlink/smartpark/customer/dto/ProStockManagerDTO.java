package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProStockManager响应对象", description = "库存管理")
public class ProStockManagerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属园区名称")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "物品ID")
    private String goodsId;

    @ApiModelProperty(value = "物品类别")
    private Integer goodsType;

    @ApiModelProperty(value = "物品编码")
    private String goodsNo;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "入库单价(元)")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "库存数量")
    private Integer stockNum;

    @ApiModelProperty(value = "入库均价（元）")
    private BigDecimal averagePrice;

    @ApiModelProperty(value = "所属仓库ID(暂不实现 统一展示‘公寓仓库’)")
    private String warehouseId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
