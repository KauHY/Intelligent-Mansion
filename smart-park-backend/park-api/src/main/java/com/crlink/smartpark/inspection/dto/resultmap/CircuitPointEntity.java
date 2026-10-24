package com.crlink.smartpark.inspection.dto.resultmap;

import com.crlink.smartpark.inspection.dto.InspectionPointDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CircuitPointEntity implements Serializable {
    private String circuitId;
    private List<InspectionPointDTO> points;
}
