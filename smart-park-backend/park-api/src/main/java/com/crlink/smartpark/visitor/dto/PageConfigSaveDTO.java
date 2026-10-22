package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 访客页面输入配置
 * * @date 2021-09-10 16:04:51
 */
@Data
@ApiModel(value = "访客页面输入配置保存实体")
public class PageConfigSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程类型，1：预约流程 2：邀约流程
     */
    @ApiModelProperty(value = "流程类型，1：预约流程 2：邀约流程", required = true)
    @NotNull(message = "流程类型，1：预约流程 2：邀约流程不能为空")
    private Integer processType;

    /**
     * 页面类型，1：访客预约 2：审核 3：员工邀约 4：访客补充信息
     */
    @ApiModelProperty(value = "页面类型，1：访客预约 2：审核 3：员工邀约 4：访客补充信息", required = true)
    @NotNull(message = "页面类型，1：访客预约 2：审核 3：员工邀约 4：访客补充信息不能为空")
    private Integer pageType;

    /**
     * 访客类型，1：普通访客 2：vip访客 3：供应商 4：面试人员
     */
    @ApiModelProperty(value = "访客类型，1：普通访客 2：vip访客 3：供应商 4：面试人员", required = true)
    @NotNull(message = "访客类型，1：普通访客 2：vip访客 3：供应商 4：面试人员不能为空")
    private Integer visitorType;

    /**
     * 页面附加输入信息
     */
    @ApiModelProperty(value = "页面附加输入信息", required = true)
    @NotBlank(message = "页面附加输入信息不能为空")
    private String extendInputInfo;

    /**
     * 园区ID
     */
    @ApiModelProperty(value = "园区ID", required = true)
    @NotBlank(message = "园区ID不能为空")
    private String parkOrgId;

}
