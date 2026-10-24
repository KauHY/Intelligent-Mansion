package com.crlink.smartpark.customer.entity;

import java.math.BigDecimal;
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
 * 租赁会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_meeting_reserve")
@ApiModel(value="MeetingReserve数据库对象", description="租赁会议室预订")
public class MeetingReserve implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "申请人当前园区")
    private String parkOrgId;

    @ApiModelProperty(value = "申请人id")
    private String employeeId;

    @ApiModelProperty(value = "申请单号")
    private String reserveNo;

    @ApiModelProperty(value = "会议主题")
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id")
    private String meetingId;

    @ApiModelProperty(value = "申请单位（当前员工所属企业名称）")
    private String applicantUnit;

    @ApiModelProperty(value = "联系人（当前员工姓名）")
    private String applicantName;

    @ApiModelProperty(value = "手机号码（当前员工手机号码）")
    private String mobile;

    @ApiModelProperty(value = "预计人数")
    private Integer meetingPeopleNum;

    @ApiModelProperty(value = "预计总金额=所选时段租赁金额+已选物资金额；每一物资金额=单价*数量；")
    private BigDecimal estimatedAmount;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applicantTime;

    @ApiModelProperty(value = "状态变更时间（审核或取消时间）")
    private LocalDateTime stateChangeTime;

    @ApiModelProperty(value = "审核状态 1未审核 2通过 3拒绝 4已取消")
    private Integer reviewStatus;

    @ApiModelProperty(value = "审核（实际金额）")
    private BigDecimal reviewAmount;

    @ApiModelProperty(value = "审批意见")
    private String reviewMsg;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "会议室所在园区（备份）")
    private String oldParkOrgId;

    @ApiModelProperty(value = "会议室名字（备份）")
    private String oldMeetingName;

    @ApiModelProperty(value = "会议室容纳人数（备份）")
    private Integer oldCapacity;

    @ApiModelProperty(value = "封面图片（单张）（备份）")
    private String oldCoverImage;

    @ApiModelProperty(value = "预定须知（备份）")
    private String oldReserveInstruct;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除 1是 0否")
    private Boolean deleted;


}
