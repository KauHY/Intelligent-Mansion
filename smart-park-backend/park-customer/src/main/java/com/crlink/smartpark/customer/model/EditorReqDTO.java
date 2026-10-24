package com.crlink.smartpark.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文本编辑器
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Editor请求对象", description = "文本编辑器")
public class EditorReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "项类型", required = true)
    private String elementType;

    @ApiModelProperty(value = "题目", required = true)
    private String name;

    @ApiModelProperty(value = "组件约束条件", required = true)
    private String constraintCondition;

    @ApiModelProperty(value = "选项内容", required = true)
    private String elementContent;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
