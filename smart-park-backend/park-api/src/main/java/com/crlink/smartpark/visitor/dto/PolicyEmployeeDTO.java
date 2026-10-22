package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 策略员工表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PolicyEmployee响应对象", description = "策略员工表")
public class PolicyEmployeeDTO implements Serializable {
    private static final long serialVersionUID = -7042026279615735182L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "通行策略ID")
    private String policyId;

    @ApiModelProperty(value = "企业ID")
    private String companyId;

    @ApiModelProperty(value = "员工ID")
    private String employeeId;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "设备ID集合(逗号隔开)")
    private Set<String> deviceIds;

}
