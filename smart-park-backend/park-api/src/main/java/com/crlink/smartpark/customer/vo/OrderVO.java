package com.crlink.smartpark.customer.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 报事投诉、报修服务查询表
 * </p>
 * */
@Data
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "报事报修工单编号")
    private String orderNo;

    @ApiModelProperty(value = "园区编码")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "报事报修反馈人")
    private String orderOwnerName;

    @ApiModelProperty(value = "报事报修人电话")
    private String orderOwnerMobile;

    @ApiModelProperty(value = "报事报修类型：1-报事投诉、2-报修服务")
    private Integer orderType;

    @ApiModelProperty(value = "报事投诉类型 字典 ORDERTYPE-1：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他；" +
            "报修服务类型 字典ORDERTYPE-2：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他")
    private Integer orderTypeItem;

    @ApiModelProperty(value = "报事报修内容")
    private String orderContent;

    @ApiModelProperty(value = "报事报修创建时间")
    @JsonFormat(pattern = Constants.TIME_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "处理状态：字典 ORDER_STATE 1待派单,2待处理,8已完成,9已撤单,10转办中")
    private Integer orderState;

    @ApiModelProperty(value = "图片uri，多个时逗号分隔")
    private String orderUrl;

    @ApiModelProperty(value = "报事报修详细地址")
    private String orderAddress;

    @ApiModelProperty(value = "当前处理人ID")
    private String currentStaffId;

    @ApiModelProperty(value = "发布人")
    private String createByName;

    @ApiModelProperty(value = "处理流程")
    private List<OrderProcessRecordVO> recordVOList;

    @ApiModelProperty(value = "是否为当前用户创建：true 是，false 否")
    private Boolean isCreateByCurrentUser;


}
