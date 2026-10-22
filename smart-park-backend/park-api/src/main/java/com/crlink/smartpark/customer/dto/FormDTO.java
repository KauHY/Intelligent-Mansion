package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;

/**
 * <p>
 * 信息统计表单
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Form响应对象", description = "信息统计表单")
public class FormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;
    @Id2objectField(idField = "parkOrgId", code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "表单名称")
    private String title;

    @ApiModelProperty(value = "收集对象,公司")
    @Id2objectField(code = COMPANY_NAME_CODE, targetFieldName = "companyNames")
    private List<String> companyIds;
    private List<String> companyNames;

    @ApiModelProperty(value = "收集对象,员工")
    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "employeeNames")
    private List<String> employeeIds;
    private List<String> employeeNames;

    @ApiModelProperty(value = "周期，1每日、2每周、3每月、4自定义")
    private Integer cycle;

    @ApiModelProperty(value = "截止时间")
    private String deadlineTime;

    @ApiModelProperty(value = "截止时间的周几或第几天")
    private Integer deadlineNum;

    @ApiModelProperty(value = "是否提醒，false不提醒，1true")
    private Boolean reminded;

    @ApiModelProperty(value = "模板id")
    private String templateId;

    @ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
