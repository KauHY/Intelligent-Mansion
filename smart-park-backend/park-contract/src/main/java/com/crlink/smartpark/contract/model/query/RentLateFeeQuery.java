package com.crlink.smartpark.contract.model.query;

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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 租赁滞纳金费用
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentLateFee查询对象", description = "租赁滞纳金费用")
public class RentLateFeeQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "入驻企业名称")
    private String companyName;

    @ApiModelProperty(value = "状态 1计算中 2已缴清 3已免除")
    private Integer status;

    @ApiModelProperty(value = "合同费用类型3-租赁合同；4-物业合同；5-公寓合同；")
    @NotNull(message = "合同费用类型不能为空")
    private Integer contractType;


}
