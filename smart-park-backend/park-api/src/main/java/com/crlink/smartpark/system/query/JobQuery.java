package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 工作岗位
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Job对象", description = "工作岗位")
public class JobQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "岗位代码")
    private Integer jobCode;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @ApiModelProperty(value = "企业")
    private String companyId;

    @ApiModelProperty(value = "企业类型")
    private Integer companyType;

    @ApiModelProperty(value = "员工名")
    private String employeeName;

}
