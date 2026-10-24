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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 会议室物资管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingGoods请求对象", description = "会议室物资管理")
public class MeetingGoodsReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属园区", required = true)
    @NotBlank
    private String parkOrgId;

    @ApiModelProperty(value = "物资类型 1购买型、2租用型", required = true)
    @NotNull
    private Integer goodsType;

    @ApiModelProperty(value = "物资名称", required = true)
    @NotBlank
    @Length(max = 100)
    private String goodsName;

    @ApiModelProperty(value = "价格(元)", required = true)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "计量单位", required = true)
    @NotBlank
    private String unit;

    @ApiModelProperty(value = "物资图片", required = true)
    @NotBlank
    private String goodsImage;

    @ApiModelProperty(value = "说明", required = true)
    @NotBlank
    @Length(max = 200)
    private String remark;


}
