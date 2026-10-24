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

/**
 * <p>
 * 公告信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_notice", autoResultMap = true)
@ApiModel(value = "Notice数据库对象", description = "公告信息表")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String title;

    @ApiModelProperty(value = "公告类型")
    private Integer type;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @ApiModelProperty(value = "推送范围：0 不限， 1 指定公司/人员")
    private Boolean withoutLimit;

    @ApiModelProperty(value = "投放类型：1 C端，2 运营端，3 C端 运营端")
    private Integer putIntoType;

    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean popuped;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeEnd;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "内容")
    private String content;

    private Integer status;

    @ApiModelProperty(value = "附件")
    @TableField(typeHandler = JacksonTypeHandler.class, updateStrategy = FieldStrategy.IGNORED)
    private String files;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    private String updateBy;

    @ApiModelProperty(value = "0,未删除，1删除")
    private Boolean deleted;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;


}
