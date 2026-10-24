package com.crlink.smartpark.customer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 问卷编辑器
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "QuestionnaireEditor响应对象", description = "问卷编辑器")
public class QuestionnaireEditorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "问卷id")
    @JsonIgnore
    private String questionnaireId;

    @ApiModelProperty(value = "问卷项类型")
    private Integer elementType;

    @ApiModelProperty(value = "问卷题")
    private String name;

    @ApiModelProperty(value = "组件约束条件")
    private String constraintCondition;

    @ApiModelProperty(value = "选项内容")
    private String elementContent;

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
