package com.crlink.smartpark.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractHouseStatusDTO implements Serializable {
    private String companyName;
    private Integer houseStatus;
}
