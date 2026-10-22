package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.crlink.smartpark.core.mphandler.SetTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 访客记录
 *
 */
@Data
@TableName("vis_visitor_record")
public class VisitorRecord implements Serializable {
    private static final long serialVersionUID = -882342765707503444L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "访客名")
    private String visitorName;

    @ApiModelProperty(value = "访客电话号码")
    private String visitorTel;

    @ApiModelProperty(value = "访客身份证号")
    private String visitorIdCard;

    @ApiModelProperty(value = "访客人数")
    private Integer visitorCount;

    @ApiModelProperty(value = "访客人脸图")
    private String visitorFaceImg;

    @ApiModelProperty(value = "车辆id")
    private String carId;

    @ApiModelProperty(value = "访客车牌号")
    private String carNum;

    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> parkingLotCodes;

    @ApiModelProperty(value = "来访日期")
    private LocalDate visitDate;

    @ApiModelProperty(value = "来访事由 1:商务交流 2:面试 3:其他")
    private Integer visitReason;

    @ApiModelProperty(value = "访客备注")
    private String visitorRemark;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "设备人员ID")
    private Long devicePersonId;

    @ApiModelProperty(value = "访客类型ID")
    private String visitorTypeId;

    @ApiModelProperty(value = "访客类型名")
    private String visitorTypeName;

    @ApiModelProperty(value = "通行策略ID")
    private String accessPolicyId;

    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "访问的企业ID,被访人的公司，邀请人的公司")
    private String companyId;

    @ApiModelProperty(value = "邀请人或被访人")
    private String invitorEmployeeId;

    @ApiModelProperty(value = "被访人姓名，邀请人姓名")
    private String invitorName;

    @ApiModelProperty(value = "被访人电话号码，邀请人电话")
    private String invitorTel;

    @ApiModelProperty(value = "邀请人备注")
    private String invitorRemark;

    @ApiModelProperty(value = "访客来源：1:访客预约 2:访客邀约 3:现场登记")
    private Integer visitorSource;

    @ApiModelProperty(value = "流程状态：0待审核/邀请中，1审核不通过，2已取消，3审核通过/邀请成功，4登记/门禁核验，5签出")
    private Integer processStatus;

    @ApiModelProperty(value = "审批时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "审批人")
    private String auditEmployeeId;

    @ApiModelProperty(value = "审批备注")
    private String auditRemark;

    @ApiModelProperty(value = "登记类型:1现场登记,3门禁核验")
    private Integer signInType;

    @ApiModelProperty(value = "登记核验时间")
    private LocalDateTime signInTime;

    @ApiModelProperty(value = "签出时间")
    private LocalDateTime signOutTime;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "设备ids")
    @TableField(exist = false)
    private Set<String> deviceIds;

}
