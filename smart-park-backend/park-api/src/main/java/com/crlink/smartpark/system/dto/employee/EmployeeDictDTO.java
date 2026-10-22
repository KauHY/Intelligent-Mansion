package com.crlink.smartpark.system.dto.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDictDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "所属入驻企业ID")
    private String companyId;
    private String companyName;

    @ApiModelProperty(value = "员工标签")
    private String tagId;
    private String tagName;


}


