package com.crlink.smartpark.visitor.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MobileVisitorRecordVO {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "访客名")
    private String visitorName;

    @ApiModelProperty(value = "访客电话号码")
    private String visitorTel;

    @ApiModelProperty(value = "访客身份证号")
    private String visitorIdCard;

    @ApiModelProperty(value = "访客类型ID")
    private String visitorTypeId;

    @ApiModelProperty(value = "访客类型名")
    private String visitorTypeName;

    @ApiModelProperty(value = "访客人数")
    private Integer visitorCount;

    @ApiModelProperty(value = "访客人脸图")
    private String visitorFaceImg;

    @ApiModelProperty(value = "访客车牌号")
    private String carNum;

    @ApiModelProperty(value = "访问的企业ID,被访人的公司，邀请人的公司")
    private String companyId;

    @ApiModelProperty(value = "访问的企业名,被访人的公司，邀请人的公司")
    @Id2objectField(code = Id2objCodeConstant.COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他")
    private Integer visitReason;

    @ApiModelProperty(value = "访客备注")
    private String visitorRemark;

    @ApiModelProperty(value = "被访人姓名，邀请人姓名")
    private String invitorName;

    @ApiModelProperty(value = "被访人电话号码，邀请人电话")
    private String invitorTel;

    @ApiModelProperty(value = "来访日期")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate visitDate;

    @ApiModelProperty(value = "邀请人或被访人")
    private String invitorEmployeeId;

    @ApiModelProperty(value = "邀请人备注")
    private String invitorRemark;

    @ApiModelProperty(value = "访客来源：1:访客预约 2:访客邀约 3:现场登记")
    private Integer visitorSource;

    @ApiModelProperty(value = "审批人")
    private String auditEmployeeId;

    @ApiModelProperty(value = "审批备注/审核意见")
    private String auditRemark;

    @ApiModelProperty(value = "流程状态：0审核中/邀请中，1审核通过/邀请成功，2审核不通过，3已取消，4登记/门禁核验，5签出")
    private Integer processStatus;

    @ApiModelProperty(value = "通行策略ID")
    private String accessPolicyId;

    @ApiModelProperty(value = "通行策略名/访问区域")
    @Id2objectField(code = Id2objCodeConstant.VISITOR_ACCESS_POLICY_NAME_CODE)
    private String accessPolicyName;

}
