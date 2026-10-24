package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 水电表合同房屋关系
 * </p>
 * */
@Data
@NoArgsConstructor
@TableName("dev_meter_contract_house_relation")
@ApiModel(value = "MeterContractHouseRelation数据库对象", description = "水电表合同房屋关系")
public class MeterContractHouseRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;


    @ApiModelProperty(value = "对应dev_device_info表主键")
    private String meterId;

    @ApiModelProperty(value = "合同房屋id")
    private String contractHouseId;

    private Boolean deleted;

    public MeterContractHouseRelation(String settledMonth, String meterId, String contractHouseId) {
        this.settledMonth = settledMonth;
        this.meterId = meterId;
        this.contractHouseId = contractHouseId;
    }
}
