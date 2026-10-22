package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 信息统计表单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Form请求对象", description = "信息统计表单")
public class FormReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    @NotBlank
    private String parkOrgId;

    @ApiModelProperty(value = "表单名称", required = true)
    @Length(min = 1, max = 30)
    private String title;

    @ApiModelProperty(value = "收集对象,公司", required = true)
    private List<String> companyIds;

    @ApiModelProperty(value = "收集对象,员工", required = true)
    private List<String> employeeIds;

    @ApiModelProperty(value = "周期，1每日、2每周、3每月、4自定义", required = true)
    @NotNull
    private Integer cycle;

    @ApiModelProperty(value = "截止时间", required = true)
    @NotBlank
    private String deadlineTime;

    @ApiModelProperty(value = "截止时间的周几或第几天")
    private Integer deadlineNum;

    @ApiModelProperty(value = "是否提醒，false不提醒，1true")
    private Boolean reminded;

    @ApiModelProperty(value = "模板id", required = true)
    private String templateId;


}
