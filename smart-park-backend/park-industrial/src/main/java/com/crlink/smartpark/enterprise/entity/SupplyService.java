package com.crlink.smartpark.enterprise.entity;

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
 * 供应服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ind_supply_service")
@ApiModel(value="SupplyService数据库对象", description="供应服务")
public class SupplyService implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "公司Id")
    private String companyId;

    @ApiModelProperty(value = "服务名")
    private String name;

    @ApiModelProperty(value = "供应类型：0-产品供应；1-服务供应；2-其他供应")
    private Integer supplyType;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "跳转类型：0-无；1-URL；2-小程序")
    private Integer jumpType;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String tel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态：0-待发布；1-已发布；2-下架")
    private Integer state;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "数据来源：0-企业新增；1-管理员新增")
    private Integer source;

    private String createBy;

    private String updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean deleted;


}
