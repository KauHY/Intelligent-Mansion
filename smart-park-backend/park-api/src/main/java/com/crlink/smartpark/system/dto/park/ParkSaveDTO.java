package com.crlink.smartpark.system.dto.park;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ParkSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "园区orgId")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    @NotBlank(message = "园区名不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    /*
    *以下为副表信息*/

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "建成时间")
    @JsonFormat(pattern = Constants.DATE_PATTERN, timezone = Constants.TIMEZONE)
    private LocalDate buildTime;

    @ApiModelProperty(value = "园区属性 1：社会大型园区 2：政府园区 3：社区集体园区 4：区属国企物业 5：社会零散物业")
    private Integer parkType;

    @ApiModelProperty(value = "功能用途 1：研发办公 2：工业生产 3：物流仓储 4：商业配套 5：其他")
    private Integer parkFunction;

    @ApiModelProperty(value = "产权属性 1：无 2：红本 3：绿本 4：其他 ")
    private Integer property;

    @ApiModelProperty(value = "权属单位")
    private String companyName;

    @ApiModelProperty(value = "权属单位联系人")
    private String companyContacts;

    @ApiModelProperty(value = "权属单位联系电话")
    private String companyTel;

    @ApiModelProperty(value = "管理机构")
    private String managementName;

    @ApiModelProperty(value = "管理机构联系人")
    private String managementContacts;

    @ApiModelProperty(value = "管理机构联系电话")
    private String managementTel;

    @ApiModelProperty(value = "占地面积")
    @Min(value = 0,message = "占地面积最小只能为0")
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    @Min(value = 0,message = "建筑面积最小只能为0")
    private Double buildingArea;

    @ApiModelProperty(value = "消防审批状态 0：已通过 1：未通过 2：暂未审批")
    private Integer fireExamineStatus;

    @ApiModelProperty(value = "消防责任管理人")
    private String firePerson;

    @ApiModelProperty(value = "消防责任管理人联系电话")
    private String firePersonTel;

    @ApiModelProperty(value = "图片")
    private String pics;

    @ApiModelProperty(value = "创建人")
    @JsonIgnore
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonIgnore
    private String updateBy;

}
