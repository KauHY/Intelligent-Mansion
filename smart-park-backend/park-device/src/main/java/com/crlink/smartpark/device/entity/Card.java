package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 卡片管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dev_card")
@ApiModel(value = "Card数据库对象", description = "卡片管理")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "卡片介质:CARD_MEDIUM")
    private Integer cardMedium;

    @ApiModelProperty(value = "卡片类型:CARD_TYPE")
    private Integer cardType;

    @ApiModelProperty(value = "关联园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "所属企业ID")
    private String companyId;

    @ApiModelProperty(value = "关联员工ID")
    private String employeeId;

    @ApiModelProperty(value = "有效期开始日期")
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "有效期结束日期")
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态：CARD_STATUS")
    private Integer status;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;


}
