package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 入驻企业表
 * */
@Data
@ApiModel(value = "入驻企业绑定小程序或H5列表查询query")
public class CompanyBindListQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 绑定园区ID
     */
    @ApiModelProperty(value = "绑定园区ID")
    private String parkOrgId;


}
