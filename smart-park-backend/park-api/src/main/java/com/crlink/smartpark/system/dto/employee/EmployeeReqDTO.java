package com.crlink.smartpark.system.dto.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

import static com.crlink.smartpark.common.constants.Constants.REGEX_MOBILE_EXACT;

/**
 * <p>
 * 入住企业员工信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Employee对象", description = "入住企业员工信息表")
public class EmployeeReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工姓名")
    @Length(min = 1, max = 20)
    private String name;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = REGEX_MOBILE_EXACT, message = "手机号不对")
    private String mobile;

    @ApiModelProperty(value = "照片（英文逗号分隔）")
    @NotNull
    private String pics;

    @ApiModelProperty(value = "所属园区组织ID")
    @NotNull
    private String parkOrgId;

    @ApiModelProperty(value = "所属入驻企业ID")
    @NotNull
    private String companyId;

    /*
    2022.03.23 v12 新增字段
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "标签")
    private List<String> tagIds;

    @ApiModelProperty(value = "岗位")
    private List<String> jobIds;

}
