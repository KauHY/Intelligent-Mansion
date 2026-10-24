package com.crlink.smartpark.contract.model.query;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ApartmentPeopleHouseQuery对象", description = "")
public class ApartmentPeopleHouseQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "园区ID")
    @NotBlank(message = "园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    @ApiModelProperty(value = "房屋ID")
    private String houseId;
}
