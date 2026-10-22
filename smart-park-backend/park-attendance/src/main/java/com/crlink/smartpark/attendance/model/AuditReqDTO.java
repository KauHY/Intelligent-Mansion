package com.crlink.smartpark.attendance.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AuditReqDTO请求对象", description = "外勤申请记录")
public class AuditReqDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "true-通过;false-拒绝", required = true)
    @NotNull
    private Boolean approval;

    @ApiModelProperty(value = "审批意见")
    @NotBlank(message = "审核意见不能为空！")
    @Length(max = 200, message = "审核意见不能超过200字！")
    private String auditOpinion;
}
