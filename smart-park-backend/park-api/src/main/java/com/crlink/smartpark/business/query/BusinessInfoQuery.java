package com.crlink.smartpark.business.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusinessInfoQuery extends BaseQuery {
    @ApiModelProperty(value = "招商标题")
    private String title;

    @ApiModelProperty(value = "招商时效类型,1:长期有效")
    private Integer effectType;

    @ApiModelProperty(value = "招商状态")
    private Integer status;

    private String orgId;


}
