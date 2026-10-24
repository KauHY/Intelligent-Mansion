package com.crlink.smartpark.customer.model;

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
 * 租赁会议室预定物资管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserveGoods请求对象", description="租赁会议室预定物资管理")
public class MeetingReserveGoodsReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id", required = true)
      private String id;

    @ApiModelProperty(value = "会议室id", required = true)
    private String meetingId;

    @ApiModelProperty(value = "会议室预定id", required = true)
    private String meetingReserveId;

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

    @ApiModelProperty(value = "说明（备份）", required = true)
    private String remark;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人", required = true)
    private String createBy;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人", required = true)
    private String updateBy;

    @ApiModelProperty(value = "是否删除 1是 0否", required = true)
    private Boolean deleted;


}
