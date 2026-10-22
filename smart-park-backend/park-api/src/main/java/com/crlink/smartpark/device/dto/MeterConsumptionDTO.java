package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.BUILDING_HOUSE_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 企业或房屋水电用量
 * </p>
 * */
@Data
@ApiModel(description = "企业或房屋水电用量")
public class MeterConsumptionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "0房屋用量记录，1企业用量记录")
    private Integer type;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;
    @Id2objectField(code = PARK_ORG_NAME_CODE, idField = "parkOrgId")
    private String parkOrgName;

    @ApiModelProperty(value = "结算月份")
    private String settledMonth;

    @ApiModelProperty(value = "企业Id")
    private String companyId;
    private String companyName;

    @ApiModelProperty(value = "合同类型")
    private Integer contractType;

    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    @ApiModelProperty(value = "房屋id集合")
    @Id2objectField(code = BUILDING_HOUSE_NAME_CODE, targetFieldName = "houseNames")
    private List<String> houseIds;
    private List<String> houseNames;

    @ApiModelProperty(value = "用量")
    private List<Consumption> consumption;

    @ApiModelProperty(value = "用量详情")
    private List<MeterConsumptionDetailDTO> consumptionDetail;

}
