package com.crlink.smartpark.system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.*;

/**
 * <p>
 * 企业入驻审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CompanyAudit响应对象", description="企业入驻审核")
public class CompanyAuditDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "企业端用户id")
    private String businessUserId;

    @ApiModelProperty(value = "企业端用户名")
    @Id2objectField(code = BUSINESS_USER_NAME_CODE, idField = "businessUserId")
    private String businessUserName;

    @ApiModelProperty(value = "企业信息id")
    private String companyInformationId;

    @ApiModelProperty(value = "企业名称")
    @Id2objectField(code = COMPANY_INFO_NAME_CODE, idField = "companyInformationId")
    private String companyInformationName;

    @ApiModelProperty(value = "申请入驻园区ID")
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "入驻申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "审核人")
    private String auditUserId;

    @ApiModelProperty(value = "审核人姓名")
    @Id2objectField(code = USER_NAME_CODE, idField = "auditUserId")
    private String auditUserName;

    @ApiModelProperty(value = "审核意见")
    private String auditMsg;

    @ApiModelProperty(value = "状态 1待审核 2通过 3拒绝 4已取消")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "取消时间")
    private LocalDateTime cancelTime;

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
