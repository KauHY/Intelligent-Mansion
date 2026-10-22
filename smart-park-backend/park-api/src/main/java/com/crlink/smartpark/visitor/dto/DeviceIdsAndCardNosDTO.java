package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 卡号和设备的DTO
 * */
@Data
public class DeviceIdsAndCardNosDTO implements Serializable {
    private static final long serialVersionUID = -7042026279615735182L;

    @ApiModelProperty(value = "卡号集合")
    private Set<String> cardNos;

    @ApiModelProperty(value = "设备ID集合")
    private Set<String> deviceIds;

}
