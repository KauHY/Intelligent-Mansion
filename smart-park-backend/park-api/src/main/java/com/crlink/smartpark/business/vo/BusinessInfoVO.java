package com.crlink.smartpark.business.vo;

import com.crlink.smartpark.business.dto.BusinessInfoDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusinessInfoVO extends BusinessInfoDTO {
    @ApiModelProperty(value = "园区名")
    private String parkOrgName;
    @ApiModelProperty(value = "招商数量")
    private Integer amount;
    @ApiModelProperty(value = "面积")
    private Double area;
    @ApiModelProperty(value = "楼层")
    private Integer minFloor;
    @ApiModelProperty(value = "租金")
    private String unitRent;

    @ApiModelProperty(value = "咨询顾问（员工）")
    private String employeeName;

    private List<BusinessHouseVO> businessHouseVO;
}
