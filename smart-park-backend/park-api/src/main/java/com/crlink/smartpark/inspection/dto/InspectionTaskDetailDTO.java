package com.crlink.smartpark.inspection.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionTaskDetailDTO implements Serializable {
    private InspectionTaskDTO taskDTO;
}
