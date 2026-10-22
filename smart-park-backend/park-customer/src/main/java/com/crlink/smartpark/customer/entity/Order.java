package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报事投诉、报修服务表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_order")
@NoArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 报事报修工单编号
     */
    private String orderNo;

    /**
     * 园区编码
     */
    private String parkOrgId;

    /**
     * 报事报修反馈人
     */
    private String orderOwnerName;

    /**
     * 报事报修人电话
     */
    private String orderOwnerMobile;

    /**
     * 报事报修类型：1-报事投诉、2-报修服务
     */
    private Integer orderType;

    /**
     * 报事投诉类型 ORDERTYPE-1：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他	报修服务类型 ORDERTYPE-2：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他
     */
    private Integer orderTypeItem;

    /**
     * 报事报修内容
     */
    private String orderContent;

    /**
     * 报事报修创建时间
     */
    private LocalDateTime orderTime;

    /**
     * 处理状态：1待派单,2待处理,8已完成,9已撤单
     */
    private Integer orderState;

    /**
     * 图片uri，多个时逗号分隔
     */
    private String orderUrl;

    /**
     * 报事报修详细地址
     */
    private String orderAddress;

    /**
     * 当前处理人ID
     */
    private String employeeId;

    /**
     * 转办员对应employeeId
     */
    private String transferMan;

    /**
     * 是否删除0，否，1是
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private String updateBy;


    public Order(String id, Integer orderState) {
        this.id = id;
        this.orderState = orderState;
    }
}
