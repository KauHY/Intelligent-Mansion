package com.crlink.smartpark.device.dto;

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
 * 水电表合同房屋关系
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeterContractHouseRelation响应对象", description="水电表合同房屋关系")
public class MeterContractHouseRelationDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "对应dev_device_info表主键")
    private String meterId;

    @ApiModelProperty(value = "合同房屋id")
    private String contractHouseId;

    private Boolean deleted;


}
