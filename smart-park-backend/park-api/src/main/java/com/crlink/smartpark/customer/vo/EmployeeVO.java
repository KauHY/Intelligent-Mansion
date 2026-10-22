package com.crlink.smartpark.customer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "照片（英文逗号分隔）")
    private String pics;

    @ApiModelProperty(value = "所属园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "所属园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "所属入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "所属入驻企业名")
    private String companyName;

    @ApiModelProperty(value = "设备人员ID")
    private Long devicePersonId;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "员工标签")
    private List<String> tagIds;

    @ApiModelProperty(value = "员工标签名")
    private List<String> tagLabels;

}
