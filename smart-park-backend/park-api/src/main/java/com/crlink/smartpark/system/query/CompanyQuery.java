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
 * 入驻企业表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Company对象", description = "入驻企业表")
public class CompanyQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "企业类型")
    private Integer companyType;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "企业统一信用代码")
    private String code;

    @ApiModelProperty(value = "企业状态:0-已禁用,1-正常")
    private Integer status;

    @ApiModelProperty(value = "用户所属园区")
    @JsonIgnore
    private Set<String> parkOrgIds;

}
