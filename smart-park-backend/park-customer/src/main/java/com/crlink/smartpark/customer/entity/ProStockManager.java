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
 * 库存管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_pro_stock_manager")
@ApiModel(value="ProStockManager数据库对象", description="库存管理")
public class ProStockManager implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物品ID")
    private String goodsId;

    @ApiModelProperty(value = "库存数量")
    private Integer stockNum;

    @ApiModelProperty(value = "入库均价（元）")
    private BigDecimal averagePrice;

    @ApiModelProperty(value = "所属仓库ID(暂不实现 统一展示‘公寓仓库’)")
    private String warehouseId;

    @ApiModelProperty(value = "累计入库数量（隐藏）")
    private Integer totalStockNum;

    @ApiModelProperty(value = "累计入库总价（隐藏）")
    private BigDecimal totalPrice;

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
