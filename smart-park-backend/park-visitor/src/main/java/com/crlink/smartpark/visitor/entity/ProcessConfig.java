package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 访客流程配置
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vis_process_config")
@ApiModel(value = "ProcessConfig对象", description = "访客流程配置")
public class ProcessConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "流程名")
    private String processName;

    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "流程类型，1：访客预约 2：访客邀约")
    private Integer processType;

    @ApiModelProperty(value = "访客类型ID")
    private String visitorTypeId;

    @ApiModelProperty(value = "访客流程节点集合（英文逗号分隔），1：访客预约 2：员工审核 3：员工邀请 4：访客补充信息 5：前台核验")
    private String processNodes;

    @ApiModelProperty(value = "启用，0：否，1：是")
    private Integer enable;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
