package com.crlink.smartpark.visitor.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApartmentPerson请求对象", description = "")
public class ApartmentPersonReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区组织Id", required = true)
    @NotBlank(message = "卡号不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "所属企业Id", required = true)
    private String companyId;

    @ApiModelProperty(value = "关联员工Id", required = true)
    private String employeeId;

    @ApiModelProperty(value = "身份证号")
    private String idCardNumber;

    @ApiModelProperty(value = "关联房屋id集合", required = true)
    @NotEmpty(message = "关联房屋不能为空")
    private Set<String> houseIds;

    @ApiModelProperty(value = "公寓门禁设备id集合", required = true)
    @NotEmpty(message = "公寓门禁设备不能为空")
    private Set<String> deviceIds;

    @ApiModelProperty(value = "生效开始日期", required = true)
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "生效结束日期", required = true)
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "备注")
    private String remark;


}
