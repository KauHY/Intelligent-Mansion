package com.crlink.smartpark.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.business.dto.AddressDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "bus_business_info", autoResultMap = true)
public class BusinessInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String parkOrgId;

    /**
     * 标题
     */
    private String title;

    /**
     * 招商时效类型,1:长期有效
     */
    private Integer effectType;

    private LocalDateTime deadline;

    /**
     * 租售属性，1出租，2出售，3出租出售
     */
    private Integer rentOrSale;

//    /**
//     * 亮点描述
//     */
//    private String highlight;
    /**
     * 亮点描述
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> highlight;


    /**
     * 咨询顾问（员工）id
     */
    private String employeeId;

    /**
     * 咨询电话
     */
    private String employeePhone;

    /**
     * 标题图片
     */
    private String titlePicture;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 地址
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private AddressDTO address;

    /**
     * 发布状态,0未上架，1招商中，2已下架
     */
    private Integer status;

    /**
     * 删除状态，0未删除，1已删除
     */
    private Boolean deleted;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;


}
