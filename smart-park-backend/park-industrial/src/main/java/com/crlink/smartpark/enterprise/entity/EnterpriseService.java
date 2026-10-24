package com.crlink.smartpark.enterprise.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ind_enterprise_service", autoResultMap = true )
@ApiModel(value="EnterpriseService数据库对象", description="企业服务")
public class EnterpriseService implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "服务名")
    private String name;

    @ApiModelProperty(value = "推送范围")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> pushRange;

    @ApiModelProperty(value = "所在分组：0-服务指南；1-政策申报；2-其他")
    private Integer groupType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "banner")
    private String banner;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "咨询电话")
    private String tel;

    @ApiModelProperty(value = "附件")
    private String att;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "状态：0-待发布；1-已发布；2-停用")
    private Integer state;

    private String createBy;

    private String updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
