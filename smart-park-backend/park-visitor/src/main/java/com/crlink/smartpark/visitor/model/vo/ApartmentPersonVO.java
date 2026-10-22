package com.crlink.smartpark.visitor.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.visitor.model.dto.ApartmentPersonHouseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApartmentPerson请求对象", description = "")
public class ApartmentPersonVO implements Serializable {

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
//    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    private String employeeName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "身份证号")
    private String idCardNumber;

    @ApiModelProperty(value = "照片")
    private String pics;

    @ApiModelProperty(value = "关联房屋集合")
    private List<ApartmentPersonHouseDTO> houseList;

    @JsonIgnore
    private String houseName;

    @JsonIgnore
    private Integer houseFunction;

    @ApiModelProperty(value = "创建日期")
    private LocalDate createDate;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;


}
