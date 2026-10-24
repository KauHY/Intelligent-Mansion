package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.business.dto.HouseInfoDTO;
import com.crlink.smartpark.system.dto.house.HouseVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HouseInfoVO extends HouseInfoDTO {
    private HouseVO houseVO;
    @ApiModelProperty(value = "咨询顾问名")
    private String employeeName;

    @ApiModelProperty(value = "招商时效类型,1:长期有效")
    private Integer effectType;
    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadline;
    @ApiModelProperty(value = "招商状态")
    private Integer businessStatus;
    @JsonIgnore
    private String employeeId;

}
