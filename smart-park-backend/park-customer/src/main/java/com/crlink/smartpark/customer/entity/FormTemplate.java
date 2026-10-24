package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 表单模板
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_form_template", autoResultMap = true)
@ApiModel(value = "FormTemplate数据库对象", description = "表单模板")
public class FormTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "模板标题,模板名称")
    private String title;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String description;

    @ApiModelProperty(value = "编辑器ids")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> editorIds;

    @ApiModelProperty(value = "状态")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
