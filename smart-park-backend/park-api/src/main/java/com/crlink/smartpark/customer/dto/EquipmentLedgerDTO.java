package com.crlink.smartpark.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.EMPLOYEE_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EquipmentLedger响应对象", description="设备台账")
public class EquipmentLedgerDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "管理员id")
    @Id2objectField(code = EMPLOYEE_NAME_CODE, targetFieldName = "adminName")
    private String adminId;

    @ApiModelProperty(value = "管理员")
    private String adminName;

    @ApiModelProperty(value = "设备名，同一园区下不重复")
    private String name;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "分类id")
    private String categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    @ApiModelProperty(value = "楼栋名")
    @Id2objectField(code = Id2objCodeConstant.BUILDING_NAME_CODE)
    private String buildingName;

    @ApiModelProperty(value = "楼层数")
    private String floor;

    @ApiModelProperty(value = "安装位置")
    private String installationLocation;

    @ApiModelProperty(value = "规格型号")
    private String specifications;

    @ApiModelProperty(value = "安装日期")
    private LocalDate installationDate;

    @ApiModelProperty(value = "原厂编号")
    private String originalNumber;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "维保单位")
    private String manufacturerUnit;

    @ApiModelProperty(value = "维保电话")
    private String manufacturerMobile;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "图片,逗号隔开")
    private String pic;

    @ApiModelProperty(value = "状态：0-已报废；1-正常")
    private Integer state;

    @ApiModelProperty(value = "报废日期")
    private LocalDate scrapDate;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;

}
