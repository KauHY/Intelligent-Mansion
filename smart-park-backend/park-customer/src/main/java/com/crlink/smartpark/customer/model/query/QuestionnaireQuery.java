package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 问卷
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Questionnaire查询对象", description = "问卷")
public class QuestionnaireQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "园区id")
    private String parkOrgId;


    @ApiModelProperty(value = "问卷标题")
    private String title;

//    @ApiModelProperty(value = "问卷描述")
//    private String description;
//
//    @ApiModelProperty(value = "收集对象,公司")
//    private List<String> companyIds;
//
//    @ApiModelProperty(value = "收集对象,员工")
//    private Set<String> employeeIds;
//
//    @ApiModelProperty(value = "开始时间")
//    private LocalDateTime startTime;
//
//    @ApiModelProperty(value = "截止时间")
//    private LocalDateTime deadlineTime;
//
//    @ApiModelProperty(value = "是否匿名，0否，1是")
//    private Boolean anonymous;
//
//    @ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
//    private Integer status;
//
//    private String createBy;
//
//    private LocalDateTime createTime;
//
//    private String updateBy;
//
//    private LocalDateTime updateTime;
//
//    private Boolean deleted;


}
