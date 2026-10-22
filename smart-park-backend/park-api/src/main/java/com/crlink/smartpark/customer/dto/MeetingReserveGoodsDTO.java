package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租赁会议室预定物资管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserveGoods响应对象", description="租赁会议室预定物资管理")
public class MeetingReserveGoodsDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
      private String id;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "会议室预定id")
    private String meetingReserveId;

    @ApiModelProperty(value = "对应物资ID")
    private String goodsId;

    @ApiModelProperty(value = "物品数量")
    private Integer number;

    @ApiModelProperty(value = "物资类型 1购买型、2租用型（备份）")
    private Integer oldGoodsType;

    @ApiModelProperty(value = "物资名称（备份）")
    private String oldGoodsName;

    @ApiModelProperty(value = "价格(元)（备份）")
    private BigDecimal oldUnitPrice;

    @ApiModelProperty(value = "计量单位（备份）")
    private String oldUnit;

    @ApiModelProperty(value = "物资图片（备份）")
    private String oldGoodsImage;

    @ApiModelProperty(value = "说明（备份）")
    private String remark;

}
