package com.crlink.smartpark.visitor.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 访客记录详情VO
 * */
@Data
@ApiModel(value = "访客记录详情")
public class VisitorRecordVO implements Serializable {

    private static final long serialVersionUID = 5795213518470172696L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "访问的企业ID,被访人的公司，邀请人的公司")
    private String companyId;

    /**
     * 访客名
     */
    @ApiModelProperty(value = "访客名")
    private String visitorName;

    /**
     * 访客电话号码
     */
    @ApiModelProperty(value = "访客电话号码")
    private String visitorTel;

    /**
     * 访客身份证号
     */
    @ApiModelProperty(value = "访客身份证号")
    private String visitorIdCard;

    /**
     * 访客类型ID
     */
    @ApiModelProperty(value = "访客类型ID")
    private String visitorTypeId;

    /**
     * 访客类型名
     */
    @ApiModelProperty(value = "访客类型名")
    private String visitorTypeName;

    /**
     * 访客人数
     */
    @ApiModelProperty(value = "访客人数")
    private Integer visitorCount;

    /**
     * 访客人脸图url路径
     */
    @ApiModelProperty(value = "访客人脸图url路径")
    private String visitorFaceImg;

    /**
     * 来访事由 1:商务交流 2:面试 3:其他
     */
    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他")
    private Integer visitReason;

    /**
     * 来访日期
     */
    @ApiModelProperty(value = "来访日期")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate visitDate;

    /**
     * 被访人姓名
     */
    @ApiModelProperty(value = "被访人姓名")
    private String invitorName;

    /**
     * 被访人电话号码
     */
    @ApiModelProperty(value = "被访人电话号码")
    private String invitorTel;

    /**
     * 访客备注
     */
    @ApiModelProperty(value = "访客备注")
    private String visitorRemark;

    @ApiModelProperty(value = "邀请人备注")
    private String invitorRemark;

    @ApiModelProperty(value = "审批备注")
    private String auditRemark;

    /**
     * 访客车牌号
     */
    @ApiModelProperty(value = "访客车牌号")
    private String carNum;

    /**
     * 流程状态：0待审核/邀请中，1审核不通过，2已取消，3审核通过/邀请成功，4登记/门禁核验，5签出
     */
    @ApiModelProperty(value = "流程状态：0待审核/邀请中，1审核不通过，2已取消，3审核通过/邀请成功，4登记/门禁核验，5签出")
    private Integer processStatus;

    /**
     * 访客来源：1:访客预约 2:访客邀约 3:现场登记
     */
    @ApiModelProperty(value = "访客来源：1:访客预约 2:访客邀约 3:现场登记")
    private Integer visitorSource;

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

    @ApiModelProperty(value = "通行策略ID")
    private String accessPolicyId;

    @ApiModelProperty(value = "通行策略名")
    @Id2objectField(code = Id2objCodeConstant.VISITOR_ACCESS_POLICY_NAME_CODE)
    private String accessPolicyName;

    @ApiModelProperty(value = "设备人员ID")
    private Long devicePersonId;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

}
