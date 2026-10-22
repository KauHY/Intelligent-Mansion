package com.crlink.smartpark.system.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 企业信息审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CompanyInformationAudit请求对象", description="企业信息审核")
public class CompanyInfoAuditDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "审核意见")
    @NotBlank
    private String auditMsg;

    @ApiModelProperty(value = "状态 1待审核 2通过 3拒绝 4已取消")
    @NotNull
    private Integer auditStatus;

}
