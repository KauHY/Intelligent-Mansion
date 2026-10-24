package com.crlink.smartpark.attendance.controller.request.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 外勤申请记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FieldworkApplyRecord查询对象", description="外勤申请记录")
public class FieldworkApplyRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "公司")
    private String companyId;

    @ApiModelProperty(value = "申请人姓名")
    private String employeeName;

    @ApiModelProperty(value = "申请开始时间")
    private LocalDateTime applyStartTime;

    @ApiModelProperty(value = "申请结束时间")
    private LocalDateTime applyEndTime;

    @ApiModelProperty(value = "外勤开始时间")
    private LocalDateTime fieldworkStartTime;

    @ApiModelProperty(value = "外勤结束时间")
    private LocalDateTime fieldworkEndTime;

    @ApiModelProperty(value = "状态：1-待审批；2-审核通过；3-审批不通过；4-已取消")
    private Integer state;

    @ApiModelProperty(value = "小程序专用：全部不传；0-待审批；1-已审批")
    private Integer audited;



}
