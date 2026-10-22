package com.crlink.smartpark.visitor.vo;

import com.crlink.smartpark.visitor.dto.SpecifiedTimePeriodDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 园区运营平台-人行管理-通行策略Vo */
@Data
@ApiModel(value="人行通行策略")
public class AccessPolicyVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      private String id;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称" )
    private String policyName;

    /**
     *园区ID
     */
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    /**
     *企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Set<String> companyId;

    /**
     *允许员工标签ID(逗号隔开)
     */
    @ApiModelProperty(value = "允许员工标签ID(逗号隔开)")
    private Set<String> tagIds;

    /**
     *访客通行：0-不适应，1-适用
     */
    @ApiModelProperty(value = "访客通行：0-不适应，1-适用")
    private Integer allowVisitor;

    /**
     *设备id(逗号隔开)
     */
    @ApiModelProperty(value = "设备id(逗号隔开)")
    private Set<String> deviceIds;

    /**
     *策略生效时间
     */
    @ApiModelProperty(value = "策略生效时间" )
    private LocalDate policyStartTime;

    /**
     *策略过期时间
     */
    @ApiModelProperty(value = "策略过期时间" )
    private LocalDate policyExpireTime;

    /**
     *0-全时段，1-指定时段
     */
    @ApiModelProperty(value = "0-全时段，1-指定时段")
    private Integer allTime;

    /**
     *指定时段具体信息：dayOfWeek-选中天（数组）;startTime-开始时间(时分秒);endTime-结束时间(时分秒)
     */
    @ApiModelProperty(value = "指定时段具体信息：dayOfWeek-选中天（数组）;startTime-开始时间(时分秒);endTime-结束时间(时分秒)")
    private Map<String, Object> specifiedTimePeriod;

}
