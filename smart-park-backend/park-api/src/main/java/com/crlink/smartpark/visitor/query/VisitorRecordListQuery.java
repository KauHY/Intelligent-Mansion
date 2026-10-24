package com.crlink.smartpark.visitor.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 访客记录
 * */
@Data
@ApiModel(value = "访客记录列表查询bean")
public class VisitorRecordListQuery extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -1777599889599356455L;

    /**
     * 访客名
     */
    @ApiModelProperty(value = "访客名")
    private String visitorName;

    /**
     * 园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     * 入驻企业ID
     */
    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

}
