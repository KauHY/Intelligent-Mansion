package com.crlink.smartpark.fee.dto;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 计费方案
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BillingScheme对象", description = "计费方案")
public class BillingSchemeQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区Id")
    private Set<String> parkOrgIds;

    @ApiModelProperty(value = "方案名称")
    private String schemeName;

    @ApiModelProperty(value = "方案名称")
    private Integer feeType;

    @ApiModelProperty(value = "状态：0-关闭；1-开启")
    private Integer status;

    @ApiModelProperty(value = "方案名称")
    private Set<Integer> feeTypes;


}
