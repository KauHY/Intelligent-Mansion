package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AuditReqDto对象", description = "审核请求对象")
public class AuditReqDto {


    @ApiModelProperty(value = "合同Id")
    @NotBlank(message = "合同Id不能为空")
    private String id;
    @ApiModelProperty(value = "审核结果:1-通过；2-不通过")
    @NotNull
    private Integer auditResult;
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;
}
