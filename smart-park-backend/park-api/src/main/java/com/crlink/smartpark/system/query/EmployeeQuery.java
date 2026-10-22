package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 入住企业员工信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Employee对象", description = "入住企业员工信息表")
public class EmployeeQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "所属入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "所属园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;

    @ApiModelProperty(value = "岗位")
    private String jobId;

    @JsonIgnore
    private Set<String> parkOrgIds;

}
