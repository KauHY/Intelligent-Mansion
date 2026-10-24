package com.crlink.smartpark.system.dto.employee;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Employee对象", description = "入住企业员工信息表")
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "照片")
    private String pics;

    @ApiModelProperty(value = "所属入驻企业ID")
    private String companyId;
    @Id2objectField(idField = "companyId", code = COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "所属园区组织ID")
    private String parkOrgId;
    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    /**
     * 2022.03.23 v12 新增字段
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "员工标签")
    private List<String> tagIds;
    private List<String> tagNames;

    @ApiModelProperty(value = "员工岗位")
    private List<String> jobIds;
    private List<Integer> jobCodes;

    @ApiModelProperty(value = "状态，0：禁用 1：启用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "设备人员ID")
    private Long devicePersonId;

    @ApiModelProperty(value = "人员卡号（用于大华设备推送）")
    private String cardNo;


}
