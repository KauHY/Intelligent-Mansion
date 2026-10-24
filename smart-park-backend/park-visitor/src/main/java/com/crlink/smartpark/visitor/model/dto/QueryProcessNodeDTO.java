package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 通过类型id获取邀约流程节点
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryProcessNodeDTO implements Serializable {
    private static final long serialVersionUID = 9093553903388994848L;

    @ApiModelProperty(value = "公司ID")
    @NotBlank(message = "公司ID不能为空")
    private String companyId;

    @ApiModelProperty(value = "访客类型ID", required = true)
    @NotBlank(message = "访客类型ID不能为空")
    private String visitorTypeId;

}
