package com.crlink.smartpark.fee.entity;

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

/**
 * <p>
 * 收费项
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fee_charge_item")
@ApiModel(value = "ChargeItem对象", description = "收费项")
public class ChargeItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 园区
     */
    private String parkOrgId;

    /**
     * 收费名称
     */
    private String chargeName;

    /**
     * 费用类型
     */
    private Integer feeType;

    /**
     * 收款方
     */
    private String payeeId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 备注
     */
    private String remark;


    /**
     * 状态：0-关闭；1-开启
     */
    private Integer status;


    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
