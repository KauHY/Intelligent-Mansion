package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "sys_organization", autoResultMap = true)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Integer level;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否删除：0否1是")
    private Integer deleted;


    @ApiModelProperty(value = "租户对应的C端小程序appId")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String cAppId;
    @ApiModelProperty(value = "租户对应的C端小程序Secret")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String cAppSecret;
    @ApiModelProperty(value = "租户对应的运营端小程序appId")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String bAppId;
    @ApiModelProperty(value = "租户对应的运营端小程序Secret")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String bAppSecret;

    @ApiModelProperty(value = "状态:0-已禁用,1-正常")
    private Integer status;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "营业执照电子版")
    private String licenseFileKey;

    //@ApiModelProperty(value = "额外信息")
    //@TableField(typeHandler = FastjsonTypeHandler.class)
    //private Map<String, Object> extraInfo;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
