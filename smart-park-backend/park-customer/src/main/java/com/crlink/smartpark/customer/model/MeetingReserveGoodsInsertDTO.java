package com.crlink.smartpark.customer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议室物资管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserveGoods请求对象", description="会议室物资管理")
public class MeetingReserveGoodsInsertDTO implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "对应物资ID", required = true)
    private String goodsId;

    @ApiModelProperty(value = "物品数量", required = true)
    private Integer number;

    @ApiModelProperty(value = "物资类型 1购买型、2租用型（备份）", required = true)
    private Integer oldGoodsType;

    @ApiModelProperty(value = "物资名称（备份）", required = true)
    private String oldGoodsName;

    @ApiModelProperty(value = "价格(元)（备份）", required = true)
    private BigDecimal oldUnitPrice;

    @ApiModelProperty(value = "计量单位（备份）", required = true)
    private String oldUnit;

    @ApiModelProperty(value = "物资图片（备份）", required = true)
    private String oldGoodsImage;


}
