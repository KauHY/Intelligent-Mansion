package com.crlink.smartpark.meeting.controller.request.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 会议室预订审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReserveAudit查询对象", description="会议室预订审核")
public class ReserveAuditQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "申请人")
    private String employeeId;

    @ApiModelProperty(value = "当前日期")
    private LocalDate nowDate;

    @ApiModelProperty(value = "会议预定状态：1不可预定 2可预订 3已预定 4可候补")
    private Integer meetingState;

    @ApiModelProperty(value = "会议审核状态 1预定待审核、2预定成功、3候补待审核、4候补中、5手动取消 6超时取消 7审核不通过")
    private Integer meetingStatus;

    @ApiModelProperty(value = "审核状态 0未审核 1/2已审核 ")
    private Integer auditStatus;

    private Set<String> meetingSet;


}
