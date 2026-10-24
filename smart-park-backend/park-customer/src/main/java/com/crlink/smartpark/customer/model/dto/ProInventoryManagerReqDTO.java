package com.crlink.smartpark.customer.model.dto;

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
import java.util.List;

/**
 * <p>
 * 盘点管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProInventoryManager请求对象", description = "盘点管理")
public class ProInventoryManagerReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", required = true)
    private String id;

    @ApiModelProperty(value = "所属园区", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "物品id", required = true)
    private String goodsId;

    @ApiModelProperty(value = "盘点月份", required = true)
    private String month;

    @ApiModelProperty(value = "上月结余数量", required = true)
    private String lastBalance;

    @ApiModelProperty(value = "上月结余总额（元）", required = true)
    private String lastBalanceTotal;

    @ApiModelProperty(value = "本月库存数", required = true)
    private Integer currentBalance;

    @ApiModelProperty(value = "本月库存总额（元）", required = true)
    private BigDecimal currentBalanceTotal;

    @ApiModelProperty(value = "本月入库数量", required = true)
    private Integer currentStockInNumber;

    @ApiModelProperty(value = "本月出库数量", required = true)
    private Integer currentStockOutNumber;

    @ApiModelProperty(value = "盘点时间", required = true)
    private LocalDateTime inventoryTime;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

}
