package com.crlink.smartpark.customer.model.query;

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
 * 盘点管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProInventoryManager查询对象", description="盘点管理")
public class ProInventoryManagerQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物品ID")
    private String goodsName;

    @ApiModelProperty(value = "盘点月份")
    private String month;

}
