package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("cus_editor")
@ApiModel(value = "Editor数据库对象", description = "文本编辑器")
public class Editor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
