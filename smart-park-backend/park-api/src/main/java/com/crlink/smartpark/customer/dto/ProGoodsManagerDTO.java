package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 物品管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProGoodsManager响应对象", description="物品管理")
public class ProGoodsManagerDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @Id2objectField(code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

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

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "库存下限预警值")
    private Integer minStock;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
