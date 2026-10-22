package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class EmployeeAuditDTO {

    @ApiModelProperty(value = "预约流程状态：1:审核通过 2:审核不通过", required = true)
    @Range(min = 1, max = 2, message = "预约流程状态不合法")
    private Integer processStatus;

    @ApiModelProperty(value = "访客类型ID", required = true)
    //@NotBlank(message = "访客类型ID不能为空")
    private String visitorTypeId;

    @ApiModelProperty(value = "通行策略ID", required = true)
    //@NotBlank(message = "通行策略ID不能为空")
    private String accessPolicyId;

    @ApiModelProperty(value = "审批备注")
    private String auditRemark;

}
