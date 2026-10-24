package com.crlink.smartpark.system.vo;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HouseBindRenewListVO {

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "楼层房号")
    private String houseName;

    @ApiModelProperty(value = "楼栋单元名")
    private String buildingName;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "起租时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate rentStartTime;

    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate rentEndTime;

    @ApiModelProperty(value = "续租到期时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate renewEndTime;

    @ApiModelProperty(value = "登记时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDateTime createTime;

}
