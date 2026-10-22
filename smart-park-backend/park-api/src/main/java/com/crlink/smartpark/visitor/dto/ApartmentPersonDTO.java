package com.crlink.smartpark.visitor.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 公寓人员管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApartmentPerson响应对象", description = "公寓人员管理")
public class ApartmentPersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "所属企业Id")
    private String companyId;

    @Id2objectField(idField = "companyId", code = COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "关联员工Id")
    private String employeeId;

    @ApiModelProperty(value = "关联员工Id")
    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;

    @ApiModelProperty(value = "身份证号")
    private String idCardNumber;

    @ApiModelProperty(value = "关联房屋id集合")
    private Set<String> houseIds;

    @ApiModelProperty(value = "关联房屋id集合")
    private List<String> houseNames;

    @ApiModelProperty(value = "公寓门禁设备id集合")
    private Set<String> deviceIds;

    @ApiModelProperty(value = "公寓门禁设备id集合")
    private List<String> deviceNames;

    @ApiModelProperty(value = "生效开始日期")
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "生效结束日期")
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;

}
