package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 房屋入驻记录表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_house_rent_record")
public class HouseRentRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 房屋ID
     */
    private String houseId;

    /**
     * 企业ID
     */
    private String companyId;

    /**
     * 起租时间
     */
    private LocalDate rentStartTime;

    /**
     * 到期时间
     */
    private LocalDate rentEndTime;

    /**
     * 绑定状态：1：已绑定 0：已解绑
     */
    private Boolean bind;

    /**
     * 解绑时间
     */
    private LocalDate unbindTime;

    /**
     * 初始到期时间
     */
    private LocalDate rentFirstEndTime;

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


}
