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
 * 问卷
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_questionnaire", autoResultMap = true)
@ApiModel(value = "Questionnaire数据库对象", description = "问卷")
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "封面图片")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String coverPhoto;

    @ApiModelProperty(value = "问卷标题")
    private String title;

    @ApiModelProperty(value = "问卷描述")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String description;

    @ApiModelProperty(value = "收集对象,公司")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> companyIds;

    @ApiModelProperty(value = "收集对象,员工")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> employeeIds;

    @ApiModelProperty(value = "编辑器ids")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> editorIds;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadlineTime;

    @ApiModelProperty(value = "是否匿名，0否，1是")
    private Boolean anonymous;

    @ApiModelProperty(value = "状态，1设计中，2收集中，3已结束")
    private Integer status;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
