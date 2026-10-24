package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Set;

import com.crlink.smartpark.core.mphandler.SetTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 车辆信息
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "vis_car", autoResultMap = true)
public class Car implements Serializable {
    private static final long serialVersionUID = -3471280701452662818L;

    //@TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String carCode;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    private Integer parkType;

    /**
     * 车辆品牌:0未识别
     */
    private Integer carBrand;
    /**
     * 车辆类型:0未识别
     */
    private Integer carType;
    /**
     * 车辆颜色:100其他
     */
    private Integer carColor;
    /**
     * 车牌颜色:100其他
     */
    private Integer carNumColor;
    /**
     * 黑白名单:0普通,2黑名单
     */
    private Integer blackList;

    /**
     * 授权开始时间
     */
    private LocalDateTime authStartTime;

    /**
     * 授权结束时间
     */
    private LocalDateTime authEndTime;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 企业ID
     */
    private String companyId;

    private String employeeId;

    /**
     * 车主姓名
     */
    private String carOwnerName;

    /**
     * 电话号码
     */
    private String mobile;

    /**
     * 停车场ID
     */
    @TableField(typeHandler = SetTypeHandler.class)
    private Set<String> parkingLotIds;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;

    /**
     * 状态：0-未生效；1-已生效；2-已失效
     */
    private Integer status;

}
