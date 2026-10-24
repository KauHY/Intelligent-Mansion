package com.crlink.smartpark.inspection.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class TaskRotateReqDTO implements Serializable {
    @NotEmpty
    private String taskId;
    @NotEmpty
    private List<String> employeeIds;
}
