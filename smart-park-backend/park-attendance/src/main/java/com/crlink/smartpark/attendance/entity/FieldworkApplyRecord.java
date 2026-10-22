package com.crlink.smartpark.attendance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Blob;
import java.io.Serializable;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 外勤申请记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("att_fieldwork_apply_record")
@ApiModel(value = "FieldworkApplyRecord数据库对象", description = "外勤申请记录")
public class FieldworkApplyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "公司")
    private String companyId;

    @ApiModelProperty(value = "公司名")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "申请人姓名")
    private String employeeName;

    @ApiModelProperty(value = "申请人号码")
    private String mobile;

    @ApiModelProperty(value = "外勤开始时间")
    private LocalDateTime fieldworkStartTime;

    @ApiModelProperty(value = "外勤结束时间")
    private LocalDateTime fieldworkEndTime;

    @ApiModelProperty(value = "事由")
    private String reason;

    @ApiModelProperty(value = "状态：1-待审批；2-审核通过；3-审批不通过；4-已取消")
    private Integer state;

    @ApiModelProperty(value = "地点")
    private String situation;

    @ApiModelProperty(value = "审批意见")
    private String auditOpinion;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "申请人")
    private String createBy;

    @ApiModelProperty(value = "更新/审批时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "修改人/审批人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
