package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "HouseRentRenewRecord对象", description = "")
public class HouseRentRenewRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "房屋租赁ID", required = true)
    @NotBlank(message = "房屋租赁ID不能为空")
    private String rentRecordId;

    @JsonIgnore
    private Set<String> parkOrgIds;


}
