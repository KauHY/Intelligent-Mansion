package com.crlink.smartpark.customer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 租赁会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MeetingReserveInsert请求对象", description = "租赁会议室预订")
public class MeetingReserveInsertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议主题", required = true)
    @NotBlank
    @Length(max = 30)
    private String meetingSubject;

    @ApiModelProperty(value = "会议室id", required = true)
    @NotBlank
    private String meetingId;

    @ApiModelProperty(value = "申请单位（默认当前员工所属企业名称）", required = true)
    @NotBlank
    @Length(max = 30)
    private String applicantUnit;

    @ApiModelProperty(value = "联系人（默认当前员工姓名）", required = true)
    @NotBlank
    @Length(max = 30)
    private String applicantName;

    @ApiModelProperty(value = "手机号码（当前员工手机号码）", required = true)
    @NotBlank
    @Length(max = 15)
    private String mobile;

    @ApiModelProperty(value = "预计人数", required = true)
    @Max(value = 100000)
    @NotNull
    private Integer meetingPeopleNum;

    @ApiModelProperty(value = "预计总金额=所选时段租赁金额+已选物资金额；每一物资金额=单价*数量；", required = true)
    private BigDecimal estimatedAmount;

    @ApiModelProperty(value = "预约时间段List", required = true)
    @NotNull
    List<MeetingReserveTimeInsertDTO> reserveTimeList;

    @ApiModelProperty(value = "使用物品List", required = true)
    List<MeetingReserveGoodsInsertDTO> reserveGoodsList;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "会议室所在园区（备份）", required = true)
    @NotBlank
    private String oldParkOrgId;

    @ApiModelProperty(value = "会议室名字（备份）", required = true)
    @NotBlank
    private String oldMeetingName;

    @ApiModelProperty(value = "会议室容纳人数（备份）", required = true)
    @NotNull
    private Integer oldCapacity;

    @ApiModelProperty(value = "封面图片（单张）（备份）", required = true)
    @NotBlank
    private String oldCoverImage;

    @ApiModelProperty(value = "预定须知（备份）", required = true)
    @NotBlank
    private String oldReserveInstruct;

}
