package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 访客流程配置
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProcessConfig对象", description = "访客流程配置")
public class ProcessConfigReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "流程名", required = true)
    @NotBlank(message = "流程名不能为空")
    private String processName;

    @ApiModelProperty(value = "入驻企业ID", required = true)
    @NotBlank(message = "入驻企业ID不能为空")
    private String companyId;

    @ApiModelProperty(value = "流程类型，1：访客预约 2：访客邀约", required = true)
    @NotNull(message = "流程类型不能为空")
    @Range(min = 1, max = 2, message = "流程类型参数异常")
    private Integer processType;

    @ApiModelProperty(value = "访客类型ID", required = true)
    @NotBlank(message = "访客类型ID不能为空")
    private String visitorTypeId;

    @ApiModelProperty(value = "访客流程节点集合（英文逗号分隔），1：访客预约 2：员工审核 3：员工邀请 4：访客补充信息 5：前台核验", required = true)
    @NotBlank(message = "访客流程节点集合不能为空")
    private String processNodes;

}
