package com.crlink.smartpark.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.crlink.smartpark.core.mphandler.ConsumptionListTypeHandler;
import com.crlink.smartpark.device.dto.Consumption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 企业或房屋水电用量
 * </p>
 * */
@Data
@TableName(value = "dev_meter_consumption", autoResultMap = true)
@ApiModel(value = "MeterConsumption数据库对象", description = "企业或房屋水电用量")
public class MeterConsumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    @ApiModelProperty(value = "合同Id")
    private String contractId;

    @ApiModelProperty(value = "0房屋用量记录，1企业用量记录")
    private Integer type;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "企业Id")
    private String companyId;
    private String companyName;

    @ApiModelProperty(value = "合同类型")
    private Integer contractType;

    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    @ApiModelProperty(value = "房屋id集合")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> houseIds;

    @ApiModelProperty(value = "用量")
    @TableField(typeHandler = ConsumptionListTypeHandler.class)
    private List<Consumption> consumption;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
