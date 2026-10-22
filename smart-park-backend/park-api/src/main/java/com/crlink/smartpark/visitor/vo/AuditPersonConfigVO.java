package com.crlink.smartpark.visitor.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访客审批人配置详情VO
 * */
@Data
@ApiModel(value = "访客审批人配置")
public class AuditPersonConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 园区组织ID
     */
    @ApiModelProperty(value = "园区组织ID")
    private String parkOrgId;

    /**
     * 入驻企业ID
     */
    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    /**
     * 审批人ID
     */
    @ApiModelProperty(value = "审批人ID")
    private String auditEmployeeIds;

    /**
     * 审批人姓名
     */
    @ApiModelProperty(value = "审批人姓名")
    private String auditEmployeeNames;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime updateTime;


    /**
     * 被访人审批，0：否，1：是
     */
    @ApiModelProperty(value = "被访人审批，0：否，1：是")
    private Integer invitorEmployeeAudit;


}
