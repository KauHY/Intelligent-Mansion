package com.crlink.smartpark.system.dto.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Job对象", description = "工作岗位")
public class JobReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区组织Id")
    @NotNull
    private String parkOrgId;

    @ApiModelProperty(value = "企业Id")
    @NotNull
    private String companyId;

    @NotNull
    private Integer jobCode;

    @ApiModelProperty(value = "员工Id")
    private List<String> employeeIds;
}
