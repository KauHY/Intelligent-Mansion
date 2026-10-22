package com.crlink.smartpark.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文本编辑器
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Editor响应对象", description = "文本编辑器")
public class EditorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "项类型")
    private String elementType;

    @ApiModelProperty(value = "题目")
    private String name;
    private String value;

    @ApiModelProperty(value = "组件约束条件")
    private String constraintCondition;

    @ApiModelProperty(value = "选项内容")
    private String elementContent;

}
