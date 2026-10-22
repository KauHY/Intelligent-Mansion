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
 * 物品管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_pro_goods_manager")
@ApiModel(value="ProGoodsManager数据库对象", description="物品管理")
public class ProGoodsManager implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

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
