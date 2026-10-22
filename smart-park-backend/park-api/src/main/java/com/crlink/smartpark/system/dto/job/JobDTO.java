package com.crlink.smartpark.system.dto.job;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工作岗位
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Job对象", description = "工作岗位")
public class JobDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "岗位代码")
    private Integer jobCode;

    private String jobName;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "企业Id")
    private String companyId;
    private String companyName;

    private Integer companyType;
    @ApiModelProperty(value = "关联员工Id")
    private List<String> employeeIds;

    @ApiModelProperty(value = "关联员工Id")
    private List<String> employeeNames;

    //private LocalDateTime createTime;


}
