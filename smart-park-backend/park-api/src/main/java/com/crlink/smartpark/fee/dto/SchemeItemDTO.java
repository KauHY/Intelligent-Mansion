package com.crlink.smartpark.fee.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemeItemDTO {

    private String schemeName;
    private String itemName;
    private BigDecimal taxRate;

}
