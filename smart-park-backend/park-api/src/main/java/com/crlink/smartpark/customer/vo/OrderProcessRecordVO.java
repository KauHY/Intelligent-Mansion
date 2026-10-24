package com.crlink.smartpark.customer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 流程查询表
 * </p>
 * */
@Data
public class OrderProcessRecordVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "orderId")
    private String orderId;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "1待派单,2待处理,8已完成,9已撤销'")
    private Integer orderState;

    @ApiModelProperty(value = "工单记录状态(详情流程)：1创建,2派单,3退单,4结单,5撤单,6转办,7转办处理")
    private Integer state;

    @ApiModelProperty(value = "流程no")
    private String processNo;

    @ApiModelProperty(value = "处理备注")
    private String remark;

    @ApiModelProperty(value = "转办处理结果")
    private String result;

    @ApiModelProperty(value = "当前处理员工名称")
    private String employeeName;


    @ApiModelProperty(value = "当前处理员工手机号")
    private String mobile;

    @ApiModelProperty(value = "撤单原因:1 问题已自行解决，2 误会了，没有这个问题，3已有人现场处理，4 其他原因")
    private Integer cancelReason;

    @ApiModelProperty(value = "退单原因")
    private String returnOrderReason;

    @ApiModelProperty(value = "要求完成时间")
    private LocalDate askFinishTime;

    @ApiModelProperty(value = "结束时间")
    private String finishTime;

    @ApiModelProperty(value = "园区编码")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "报事报修反馈人")
    private String orderOwnerName;

    @ApiModelProperty(value = "报事报修人电话")
    private String orderOwnerMobile;

    @ApiModelProperty(value = "报事报修详细地址")
    private String orderAddress;

    @ApiModelProperty(value = "报事报修类型：1-报事投诉、2-报修服务")
    private Integer orderType;

    @ApiModelProperty(value = "报事投诉类型 字典 ORDERTYPE-1：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他；" +
            "报修服务类型 字典ORDERTYPE-2：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他")
    private Integer orderTypeItem;

    @ApiModelProperty(value = "报事报修内容")
    private String orderContent;

    @ApiModelProperty(value = "图片uri，多个时逗号分隔")
    private String orderUrl;

}
