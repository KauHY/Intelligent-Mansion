package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 访客审批人配置
 * */
@Data
@ApiModel(value = "访客审批人配置保存实体")
public class AuditPersonConfigSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入驻企业ID
     */
    @ApiModelProperty(value = "入驻企业ID", required = true)
    @NotBlank(message = "入驻企业ID不能为空")
    private String companyId;

    /**
     * 被访人审批，0：否，1：是
     */
    @ApiModelProperty(value = "被访人审批，0：否，1：是", required = true)
    //@NotNull(message = "被访人审批不能为空")
    @Range(min = 0, max = 1, message = "被访人审批参数非法")
    private Integer invitorEmployeeAudit;

    /**
     * 审批员工ID集合
     */
    @ApiModelProperty(value = "审批员工ID集合")
    private List<String> auditEmployeeIds;

}
