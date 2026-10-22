package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 园区表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Park对象", description="园区表")
public class ParkQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "园区名")
    private String name;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "建成时间")
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
    private Double coverArea;

    @ApiModelProperty(value = "建筑面积")
    private Double buildingArea;

    @ApiModelProperty(value = "消防审批状态 0：已通过 1：未通过 2：暂未审批")
    private Integer fireExamineStatus;

    @ApiModelProperty(value = "消防责任管理人")
    private String firePerson;

    @ApiModelProperty(value = "消防责任管理人联系电话")
    private String firePersonTel;

    @ApiModelProperty(value = "图片")
    private String pics;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;

    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
