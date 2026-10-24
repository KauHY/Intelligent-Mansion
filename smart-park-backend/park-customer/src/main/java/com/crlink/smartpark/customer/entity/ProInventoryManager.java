package com.crlink.smartpark.customer.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
@TableName("cus_pro_inventory_manager")
@ApiModel(value="ProInventoryManager数据库对象", description="盘点管理")
public class ProInventoryManager implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物品id")
    private String goodsId;

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
