package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
@ApiModel(value = "RentContractFee查询对象", description = "租赁合同费用")
public class RentContractFeeQuery extends BaseQuery implements Serializable {


    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "收费项 1房屋租金 7履约保证金")
    private Integer feeType;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "应收日期开始")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate dueDateStart;

    @ApiModelProperty(value = "应收日期结束")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate dueDateEnd;

    @ApiModelProperty(value = "最晚缴纳日期开始")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate latestPayDateStart;

    @ApiModelProperty(value = "最晚缴纳日期结束")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate latestPayDateEnd;

    @ApiModelProperty(value = "出账状态 1待出账 2已出账 3已废除")
    private Integer billingStatus;

    @ApiModelProperty(value = "费用状态 0未收 1已收")
    private Integer feeStatus;

    @ApiModelProperty(value = "押金状态 1未缴清 2已缴清 3已结清")
    private Integer cashStatus;

    @ApiModelProperty(value = "退租日期开始")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate leaseDateStart;

    @ApiModelProperty(value = "退租日期结束")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate leaseDateEnd;

    @ApiModelProperty(value = "0：查询未出费用  1：查询已出费用  2：押金费用")
    @NotNull
    private Integer queryType;

    @ApiModelProperty(value = "合同费用类型3-租赁；4-物业；5-公寓；")
    @NotNull(message = "合同费用类型不能为空")
    private Integer contractType;


}
