package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_FORMAT;

@Data
public class HouseInfoReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    @ApiModelProperty(value = "园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "房屋Id")
    private String houseId;

    @ApiModelProperty(value = "工位")
    private Integer stationNum;

    @ApiModelProperty(value = "押付方式")
    private Integer chargeType;

    //@ApiModelProperty(value = "使用状态")
    //private Integer usageStatus;

    @ApiModelProperty(value = "看房时间")
    private String visitTime;

    @ApiModelProperty(value = "能否分割")
    private Boolean canDivided;

    @ApiModelProperty(value = "单位租金")
    private String unitRent;

    @ApiModelProperty(value = "面议")
    private Boolean discussPersonally;

    @ApiModelProperty(value = "最早租期")
    @DateTimeFormat(pattern = DEFAULT_DATE_FORMAT)
    private LocalDate earliestRentTime;

    @ApiModelProperty(value = "最短租期")
    private Integer minRentMonth;

    @ApiModelProperty(value = "免租期")
    private Integer freeRentMonth;

    //@ApiModelProperty(value = "0已经下架，1招租")
    //private Integer status;

}
