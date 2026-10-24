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
 * 问卷
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Questionnaire响应对象", description = "问卷")
public class QuestionnaireDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "封面图片")
    private String coverPhoto;

    @ApiModelProperty(value = "问卷标题")
    private String title;

    @ApiModelProperty(value = "问卷描述")
    private String description;

    @ApiModelProperty(value = "收集对象,公司")
    @Id2objectField(code = COMPANY_NAME_CODE, targetFieldName = "companyNames")
    private List<String> companyIds;
    private List<String> companyNames;

    @ApiModelProperty(value = "收集对象,员工")
    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "employeeNames")
    private List<String> employeeIds;
    private List<String> employeeNames;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadlineTime;

    @ApiModelProperty(value = "是否匿名，0否，1是")
    private Boolean anonymous;

    @ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
    private Integer status;

    private String createBy;

    @Id2objectField(idField = "createBy", code = Id2objCodeConstant.USER_NAME_CODE)
    private String createByName;

    private LocalDateTime createTime;

    @ApiModelProperty(value = "编辑器")
    private List<EditorDTO> editors;


}
