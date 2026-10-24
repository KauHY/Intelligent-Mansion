package com.crlink.smartpark.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ApartmentDataForScreen {

    @ApiModelProperty(value = "房间总数")
    private Integer totalHouseCount = 0;

    @ApiModelProperty(value = "待租房间数")
    private Integer rentingHouseCount = 0;

    @ApiModelProperty(value = "已租房间数")
    private Integer rentedHouseCount = 0;

    @ApiModelProperty(value = "费用收入数据")
    private Map<String, ApartmentBillForScreen> amountData;

}
