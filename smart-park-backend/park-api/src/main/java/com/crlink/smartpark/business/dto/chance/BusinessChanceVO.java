package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.business.vo.BusinessHouseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusinessChanceVO extends BusinessChanceDTO {
    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "跟进人")
    private String followEmployeeName;

    @ApiModelProperty(value = "意向房源name")
    private List<BusinessHouseVO> businessHouseVO;

}
