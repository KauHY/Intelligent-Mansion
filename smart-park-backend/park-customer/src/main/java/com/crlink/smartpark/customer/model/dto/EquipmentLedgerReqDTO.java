package com.crlink.smartpark.customer.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 设备台账
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EquipmentLedger请求对象", description="设备台账")
public class EquipmentLedgerReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "园区id", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "设备名，同一园区下不重复", required = true)
    @NotBlank
    @Length(max = 30)
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "分类id", required = true)
    @NotBlank
    private String categoryId;

    @ApiModelProperty(value = "品牌")
    @Length(max = 30)
    private String brand;

    @ApiModelProperty(value = "楼栋id", required = true)
    @NotBlank
    private String buildingId;

    @ApiModelProperty(value = "楼层数", required = true)
    @NotBlank
    private String floor;

    @ApiModelProperty(value = "详细位置")
    @Length(max = 30)
    private String installationLocation;

    @ApiModelProperty(value = "管理员")
    private String adminId;

    @ApiModelProperty(value = "备注")
    @Length(max = 200)
    private String remark;

    @ApiModelProperty(value = "图片,逗号隔开")
    private String pic;

    @ApiModelProperty(value = "规格型号")
    @Length(max = 30)
    private String specifications;

    @ApiModelProperty(value = "安装日期")
    private LocalDate installationDate;

    @ApiModelProperty(value = "原厂编号")
    @Length(max = 30)
    private String originalNumber;

    @ApiModelProperty(value = "生产厂家")
    @Length(max = 30)
    private String manufacturer;

    @ApiModelProperty(value = "维保单位")
    @Length(max = 30)
    private String manufacturerUnit;

    @ApiModelProperty(value = "维保电话")
    @Length(max = 30)
    private String manufacturerMobile;

    @ApiModelProperty(value = "状态：0-已报废；1-正常")
    private Boolean state;

    @ApiModelProperty(value = "报废日期")
    private LocalDate scrapDate;

}
