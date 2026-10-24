package com.crlink.smartpark.system.dto.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 组织机构
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Organization对象", description = "组织机构")
public class OrganizationDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "当前组织的顶级组织的id,即租户")
    private String headOrgId;

    @ApiModelProperty(value = "上级公司ID ，0表示是租户，是组织的顶级机构")
    private String parentId;

    @ApiModelProperty(value = "0，机构，1园区")
    private Integer orgType;

    @ApiModelProperty(value = "公司名称")
    private String orgName;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否删除：0否1是")
    private Integer deleted;

    @ApiModelProperty(value = "状态:0-已禁用,1-正常")
    private Integer status;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "营业执照电子版")
    private String licenseFileKey;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
