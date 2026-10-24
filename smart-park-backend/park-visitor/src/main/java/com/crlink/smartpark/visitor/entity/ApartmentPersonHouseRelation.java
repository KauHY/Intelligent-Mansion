package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 公寓人员管理房屋关系表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vis_apartment_person_house_relation")
@ApiModel(value = "ApartmentPersonHouseRelation数据库对象", description = "公寓人员管理房屋关系表")
public class ApartmentPersonHouseRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "公寓人员Id")
    private String apartmentPersonId;

    @ApiModelProperty(value = "关联员工Id")
    private String employeeId;

    @ApiModelProperty(value = "关联房屋id")
    private String houseId;

    @ApiModelProperty(value = "删除状态，0未删除，1已删除")
    private Boolean deleted;


}
