package com.crlink.smartpark.customer.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 物品领用管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceiveManager请求对象", description="物品领用管理")
public class ProReceiveGoodsDTO extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "物品名称")
    private String goodsName;

    @ApiModelProperty(value = "物品类别")
    private Integer goodsType;


}
