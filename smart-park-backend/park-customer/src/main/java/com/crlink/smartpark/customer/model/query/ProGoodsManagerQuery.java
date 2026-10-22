package com.crlink.smartpark.customer.controller.request.query;

import com.crlink.smartpark.BaseQuery;
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
 * 物品管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProGoodsManager查询对象", description="物品管理")
public class ProGoodsManagerQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物品类别(预留不使用)")
    private Integer goodsType;

    @ApiModelProperty(value = "物品编码(预留不使用)")
    private String goodsNo;

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

}
