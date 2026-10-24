package com.crlink.smartpark.device.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MeterReadingTrendVO implements Serializable {
    @ApiModelProperty("时间")
    private Object date;
    @ApiModelProperty("室内用电")
    private BigDecimal eRoom;
    @ApiModelProperty("空调用电")
    private BigDecimal eAir;
    @ApiModelProperty("公共用电")
    private BigDecimal ePublic;
    @ApiModelProperty("用电总数")
    private BigDecimal eTotal;


    @ApiModelProperty("水")
    private BigDecimal water;

    @ApiModelProperty("电环比")
    private BigDecimal eLastMonthPercent;

    @ApiModelProperty("水环比")
    private BigDecimal wLastMonthPercent;

    public MeterReadingTrendVO(LocalDate date, BigDecimal eRoom, BigDecimal eAir, BigDecimal ePublic, BigDecimal water) {
        this.date = date;
        this.eRoom = eRoom;
        this.eAir = eAir;
        this.ePublic = ePublic;
        this.water = water;
    }

}
