package com.crlink.smartpark.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报事报修处理流程表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderProcessRecord对象", description = "报事报修处理流程表")
public class OrderProcessRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "报修no")
    private String orderNo;

    @ApiModelProperty(value = "上一节点处理NO")
    private String preProNo;

    @ApiModelProperty(value = "处理状态：1待派单,2待处理,8已完成,9已撤销")
    private Integer orderState;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty(value = "处理备注")
    private String remark;

    @ApiModelProperty(value = "当前处理员工")
    private String employeeId;

    @ApiModelProperty(value = "当前处理员工名称")
    private String employeeName;

    @ApiModelProperty(value = "当前处理员工手机号")
    private String employeeMobile;

    @ApiModelProperty(value = "上一节点处理员工")
    private String preStaffId;

    @ApiModelProperty(value = "上一节点处理员工名称")
    private String preStaffName;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "处理状态")
    private Integer state;


}
