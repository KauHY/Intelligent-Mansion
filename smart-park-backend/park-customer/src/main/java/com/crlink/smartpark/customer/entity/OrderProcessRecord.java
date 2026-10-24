package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 报事报修处理流程表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cus_order_process_record")
public class OrderProcessRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 报修orderId
     */
    private String orderId;

//    /**
//     * 流程no
//     */
//    private String processNo;

    /**
     * 上一节点处理NO
     */
    private String preProNo;

    /**
     * 处理状态：1待派单,2待处理,8已完成,9已撤销
     */
    private Integer orderState;


    /**
     * 处理备注
     */
    private String remark;

    /**
     * 当前处理员工
     */
    private String employeeId;

    /**
     * 当前处理员工手机号
     */
    private String employeeMobile;

    /**
     * 所属物业公司Id
     */
    private String companyId;

    /**
     * 上一节点处理员工
     */
    private String preEmployeeId;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 要求完成时间
     */
    private LocalDate askFinishTime;

    /**
     * 撤单理由:1 问题已自行解决，2 误会了，没有这个问题，3已有人现场处理，4 其他原因
     */
    private Integer cancelReason;

    /**
     * 退单原因
     */
    private String returnOrderReason;

    /**
     * 转办处理结果
     */
    private String result;

    /**
     * 是否删除：0-否(默认),1-是
     */
    private Integer deleted;

    /**
     * 创建者
     */
    private String createBy;


    /**
     * 修改者
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 工单记录状态：1创建,2派单,3退单,4结单,5撤单
     */
    private Integer state;


}
