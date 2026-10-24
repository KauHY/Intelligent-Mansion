package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 访客审批人配置
 * */
@Data
@ApiModel(value = "访客审批人配置列表查询bean")
public class AuditPersonConfigListQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 园区组织ID
     */
    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private String companyId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

}
