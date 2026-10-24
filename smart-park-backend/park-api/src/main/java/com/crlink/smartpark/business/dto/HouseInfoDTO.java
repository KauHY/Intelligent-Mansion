package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "HouseInfo对象", description = "")
public class HouseInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String parkOrgId;

    private String houseId;

    @ApiModelProperty(value = "工位数")
    private Integer stationNum;

    @ApiModelProperty(value = "押付方式")
    private Integer chargeType;

    @ApiModelProperty(value = "使用状态")
    private Integer usageStatus;

    @ApiModelProperty(value = "看房时间")
    private String visitTime;

    @ApiModelProperty(value = "能否分割")
    private Boolean canDivided;

    @ApiModelProperty(value = "单位租金")
    private String unitRent;

    @ApiModelProperty(value = "面议")
    private Boolean discussPersonally;

    @ApiModelProperty(value = "最早租期")
    private LocalDate earliestRentTime;

    @ApiModelProperty(value = "最短租期")
    private Integer minRentMonth;

    @ApiModelProperty(value = "免租期")
    private Integer freeRentMonth;


    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;


}
