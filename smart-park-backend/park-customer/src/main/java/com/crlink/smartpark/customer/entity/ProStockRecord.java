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
 * 库存出入库记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_pro_stock_record")
@ApiModel(value="ProStockRecord数据库对象", description="库存出入库记录")
public class ProStockRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物品ID")
    private String goodsId;

    @ApiModelProperty(value = "库存ID")
    private String stockId;

    @ApiModelProperty(value = "出入库单号")
    private String recordNo;

    @ApiModelProperty(value = "入库数量")
    private Integer stockInNum;

    @ApiModelProperty(value = "入库单价（元）")
    private BigDecimal stockInUnitPrice;

    @ApiModelProperty(value = "入库操作后库存")
    private Integer currentNum;

    @ApiModelProperty(value = "入库操作后均价（元）")
    private BigDecimal currentAveragePrice;

    @ApiModelProperty(value = "出入库方式（业务使用）：1.手动入库、2.归还入库、3.手动出库、4.领用出库、5.修改库存")
    private Integer stockType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "出入库方式(统计使用)：1.入库、2.出库、0.修改库存")
    private Integer stockMode;

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
