package com.crlink.smartpark.contract.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContractGenerateReqDTO implements Serializable {
    @ApiModelProperty("合同ID")
    private String contractId;
    @ApiModelProperty("合同模板ID")
    private String contractTemplateId;
}
