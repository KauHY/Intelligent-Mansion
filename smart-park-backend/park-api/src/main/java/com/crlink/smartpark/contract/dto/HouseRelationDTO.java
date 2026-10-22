package com.crlink.smartpark.contract.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel(value="HouseRelationDTO对象", description="合同/计费房屋关联")
public class HouseRelationDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "类型为1时合同表主键；类型为2时合同计费信息表主键")
    private String pkId;

    @ApiModelProperty(value = "类型：1-合同关联房屋；2-计费信息应用房源")
    private Integer type;

    @ApiModelProperty(value = "房屋id")
    private String houseId;

}
