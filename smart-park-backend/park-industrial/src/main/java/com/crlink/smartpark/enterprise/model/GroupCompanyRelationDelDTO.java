package com.crlink.smartpark.enterprise.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 公司分组关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GroupCompanyRelationDel请求对象", description="公司分组关联表")
public class GroupCompanyRelationDelDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "群组Id，对应多级分类表id")
    private String groupId;

    @ApiModelProperty(value = "公司id", required = true)
    private String companyId;

}
