package com.crlink.smartpark.inspection.dto.resultmap;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlanCircuitEntity implements Serializable {
    private String planId;
    private String circuitId;
    private String circuitName;
    private Boolean orderly;
}
