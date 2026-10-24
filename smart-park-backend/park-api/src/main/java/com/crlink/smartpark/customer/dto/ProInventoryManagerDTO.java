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
 * 盘点管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProInventoryManager响应对象", description="盘点管理")
public class ProInventoryManagerDTO implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "盘点月份")
    private String month;

    @ApiModelProperty(value = "上月结余数量")
    private String lastBalance;

    @ApiModelProperty(value = "上月结余总额（元）")
    private String lastBalanceTotal;

    @ApiModelProperty(value = "本月库存数")
    private Integer currentBalance;

    @ApiModelProperty(value = "本月库存总额（元）")
    private BigDecimal currentBalanceTotal;

    @ApiModelProperty(value = "本月入库数量")
    private Integer currentStockInNumber;

    @ApiModelProperty(value = "本月出库数量")
    private Integer currentStockOutNumber;

    @ApiModelProperty(value = "盘点时间")
    private LocalDateTime inventoryTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
