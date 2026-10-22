package com.crlink.smartpark.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 收费项
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ChargeItemDTO对象", description = "收费项")
public class ChargeItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "收费项名称")
    private String chargeName;

    @ApiModelProperty(value = "费用类型")
    private Integer feeType;

    @ApiModelProperty(value = "收款方")
    private String payeeId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态：0-关闭；1-开启")
    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;


}
