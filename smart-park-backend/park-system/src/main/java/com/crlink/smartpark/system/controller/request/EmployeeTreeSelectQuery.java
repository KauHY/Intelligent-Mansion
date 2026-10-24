package com.crlink.smartpark.system.controller.request;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class EmployeeTreeSelectQuery extends BaseQuery {
    @NotEmpty(message = "园区id不能为空")
    @ApiModelProperty("园区id")
    private String parkOrgId;
    private Set<String> companyIds;
    private Set<String> tagIds;
    @ApiModelProperty("员工名")
    private String name;
    @ApiModelProperty("岗位编码")
    private Set<Integer> jobCodes;
}
//@ApiModelProperty("节点id类型：1园区,4入驻企业,5员工标签")
//@NotNull
//private Integer idType;
//@ApiModelProperty("节点id")
//@NotEmpty
//private Set<String> ids;
