package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
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
import java.util.List;

/**
 * <p>
 * 租赁会议室预订
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingReserve查询对象", description="租赁会议室预订")
public class MeetingReserveQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "会议室名称")
    private String meetingName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "申请时间开始")
    private LocalDateTime applicantTimeStart;

    @ApiModelProperty(value = "申请时间结束")
    private LocalDateTime applicantTimeEnd;

    @ApiModelProperty(value = "审核状态 1未审核 2通过 3拒绝 4已取消 |C端 1待审核 2已审核 ")
    private Integer reviewStatus;

//    @ApiModelProperty(value = "会议室所在园区（备份）")
//    private String oldParkOrgId;

//    @ApiModelProperty(value = "会议室名字（备份）")
//    private String oldMeetingName;

    //    @ApiModelProperty(value = "申请单位（当前员工所属企业名称）")
//    private String applicantUnit;
//
//    @ApiModelProperty(value = "联系人（当前员工姓名）")
//    private String applicantName;

}
