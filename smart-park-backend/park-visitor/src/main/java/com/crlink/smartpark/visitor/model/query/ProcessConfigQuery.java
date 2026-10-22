package com.crlink.smartpark.visitor.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 访客流程配置
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProcessConfig对象", description = "访客流程配置")
public class ProcessConfigQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程名")
    private String processName;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

}
