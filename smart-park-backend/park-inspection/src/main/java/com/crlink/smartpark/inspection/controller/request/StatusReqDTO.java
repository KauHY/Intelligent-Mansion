package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class StatusReqDTO implements Serializable {
    @NotEmpty
    private String id;
    @CustomValid({"0", "1"})
    private Integer status;
}
