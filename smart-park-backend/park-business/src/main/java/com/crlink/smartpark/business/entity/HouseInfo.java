package com.crlink.smartpark.business.entity;

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
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_house_info")
public class HouseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String parkOrgId;

    private String buildingId;

    private String houseId;

    private Integer stationNum;

    /**
     * 押付方式
     */
    private Integer chargeType;

    /**
     * 使用状态
     */
    //private Integer usageStatus;

    /**
     * 看房时间
     */
    private String visitTime;

    /**
     * 能否分割
     */
    private Boolean canDivided;

    /**
     * 单位租金
     */
    private Double unitRent;

    /**
     * 面议
     */
    private Boolean discussPersonally;

    /**
     * 最早租期
     */
    private LocalDate earliestRentTime;

    /**
     * 最短租期
     */
    private Integer minRentMonth;

    /**
     * 免租期
     */
    private Integer freeRentMonth;

    /**
     * 删除状态
     */
    private Boolean deleted;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;


}
