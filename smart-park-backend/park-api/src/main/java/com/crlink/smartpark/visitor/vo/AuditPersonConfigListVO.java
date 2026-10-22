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
 * 访客审批人配置列表查询VO
 * */
@Data
@ApiModel(value = "访客审批人列表配置")
public class AuditPersonConfigListVO implements Serializable {

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
     * 园区组织名
     */
    @ApiModelProperty(value = "园区组织名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    /**
     * 入驻企业ID
     */
    @ApiModelProperty(value = "入驻企业ID")
    private String companyId;

    /**
     * 入驻企业名
     */
    @ApiModelProperty(value = "入驻企业名")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    /**
     * 审批人姓名集合
     */
    @ApiModelProperty(value = "审批人姓名集合")
    private String auditEmployeeNames;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime updateTime;

    /**
     * 被访人审批，0：否，1：是
     */
    @ApiModelProperty(value = "被访人审批，0：否，1：是")
    private Integer invitorEmployeeAudit;

}
