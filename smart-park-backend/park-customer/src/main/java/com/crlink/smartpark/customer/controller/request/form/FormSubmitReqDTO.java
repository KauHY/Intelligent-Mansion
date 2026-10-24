package com.crlink.smartpark.customer.controller.request.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class FormSubmitReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问卷id", required = true)
    @NotBlank
    private String formId;

    @ApiModelProperty(value = "问卷提交结果", required = true)
    private String submission;

    @ApiModelProperty(value = "接收人", required = true)
    private List<String> receivedEmployeeIds;

}