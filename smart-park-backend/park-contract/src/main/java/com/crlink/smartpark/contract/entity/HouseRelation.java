package com.crlink.smartpark.contract.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同/计费房屋关联
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("con_house_relation")
@ApiModel(value="HouseRelation对象", description="合同/计费房屋关联")
public class HouseRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 类型为1时合同表主键；类型为2时合同计费信息表主键
     */
    private String pkId;

    /**
     * 类型：1-合同关联房屋；2-计费信息应用房源
     */
    private Integer type;

    /**
     * 房屋id
     */
    private String houseId;

    /**
     * 删除状态
     */
    private Boolean deleted;

    public HouseRelation() {
    }

    public HouseRelation(String pkId, String houseId, Integer type) {
        this.pkId = pkId;
        this.type = type;
        this.houseId = houseId;
    }
}
