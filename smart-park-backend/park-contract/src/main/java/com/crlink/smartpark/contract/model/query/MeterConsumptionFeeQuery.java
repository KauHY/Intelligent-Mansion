package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 租赁合同费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeterConsumptionFee查询对象", description = "租赁合同费用")
public class MeterConsumptionFeeQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "费用类型 3-租赁合同；4-物业合同；5-公寓合同；")
    @NotNull(message = "合同费用类型不能为空")
    private Integer contractType;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "费用状态 0未收 1已收")
    private Integer feeStatus;

    @ApiModelProperty(value = "0：查询未出费用  1：查询已出费用")
    @NotNull
    private Integer queryType;



}
