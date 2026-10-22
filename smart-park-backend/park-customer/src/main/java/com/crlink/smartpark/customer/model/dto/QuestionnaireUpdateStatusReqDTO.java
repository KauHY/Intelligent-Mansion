package com.crlink.smartpark.customer.model.dto;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class QuestionnaireUpdateStatusReqDTO implements Serializable {
    @CustomValid({"2", "3"})
    private Integer status;

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private String id;
}

