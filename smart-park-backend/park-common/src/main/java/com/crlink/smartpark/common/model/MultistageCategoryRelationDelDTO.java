package com.crlink.smartpark.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 多级分类关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategoryRelationDelDTO请求对象", description="多级分类关联表")
public class MultistageCategoryRelationDelDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "多级分类表id")
    private String categoryId;

    @ApiModelProperty(value = "业务id")
    private String businessId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;


}
