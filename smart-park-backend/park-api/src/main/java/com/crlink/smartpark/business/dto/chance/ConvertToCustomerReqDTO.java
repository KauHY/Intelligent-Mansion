package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.business.dto.CustomerInfoWxReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * */
@Data
@ApiModel(value = "ConvertToCustomerReqDTO", description = "转成客户")
public class ConvertToCustomerReqDTO extends CustomerInfoWxReqDTO {

    @ApiModelProperty(value = "商机id", required = true)
    private String businessId;
}
