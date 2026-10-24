package com.crlink.smartpark.visitor.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 园区运营平台-人行管理-通行策略ListVo
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AccessPolicy对象", description="人行通行策略表")
public class AccessPolicyListVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      private String id;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String policyName;

    /**
     *园区名称
     */
    @ApiModelProperty(value = "园区名称")
    private String parkOrgName;

    /**
     *企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    /**
     *允许员工标签(逗号隔开)
     */
    @ApiModelProperty(value = "允许员工标签(逗号隔开)")
    private String tagsName;

    /**
     *访客通行：不适应，适用
     */
    @ApiModelProperty(value = "访客通行：不适应，适用")
    private Integer allowVisitor;

    /**
     *设备(逗号隔开)
     */
    @ApiModelProperty(value = "设备(逗号隔开)")
    private String deviceNames;

    /**
     *状态：禁用,启用
     */
    @ApiModelProperty(value = "状态：禁用,启用")
    private Integer status;

}
