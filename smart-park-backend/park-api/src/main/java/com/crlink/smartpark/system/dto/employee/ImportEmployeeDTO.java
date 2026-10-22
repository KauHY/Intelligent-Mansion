package com.crlink.smartpark.system.dto.employee;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 *
 * </p>
 * */
@Data
public class ImportEmployeeDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "time")
    private String time;

    @ApiModelProperty(value = "所属入驻企业ID")
    private String companyId;

    @ApiModelProperty(value = "所属园区组织ID")
    private String parkOrgId;
}
