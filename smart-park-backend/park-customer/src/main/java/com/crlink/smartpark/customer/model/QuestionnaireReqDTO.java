package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 问卷
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Questionnaire请求对象", description = "问卷")
public class QuestionnaireReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id", required = true)
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "封面图片")
    private String coverPhoto;

    @ApiModelProperty(value = "问卷标题", required = true)
    @Length(min = 1, max = 30)
    private String title;

    @ApiModelProperty(value = "收集对象,公司", required = true)
    private List<String> companyIds;

    @ApiModelProperty(value = "收集对象,员工", required = true)
    private List<String> employeeIds;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止时间", required = true)
    private LocalDateTime deadlineTime;

    @ApiModelProperty(value = "是否匿名，0否，1是", required = true)
    @NotNull
    private Boolean anonymous;

}
