package com.crlink.smartpark.customer.model.dto;

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

import javax.validation.constraints.*;
import java.util.List;

/**
 * <p>
 * 物品管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProGoodsManager请求对象", description = "物品管理")
public class ProGoodsManagerReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", required = true)
    private String id;

    @ApiModelProperty(value = "所属园区", required = true)
    @NotBlank(message = "所属园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "物品类别", required = true)
    @NotNull(message = "物品类别不能为空")
    private Integer goodsType;

    @ApiModelProperty(value = "物品名称", required = true)
    @NotBlank(message = "物品名称不能为空")
    private String goodsName;

    @ApiModelProperty(value = "入库单价(元)", required = true)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "单位", required = true)
    @NotBlank(message = "物品单位不能为空")
    private String unit;

    @ApiModelProperty(value = "品牌", required = true)
    private String brand;

    @ApiModelProperty(value = "型号", required = true)
    private String model;

    @ApiModelProperty(value = "备注信息", required = true)
    private String remark;

}
