package com.crlink.smartpark.customer.controller.response.editor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EditorChoiceItemDTO implements Serializable {

    @ApiModelProperty("选项名")
    private String label;
    @ApiModelProperty("选项数")
    private Integer count;
    private BigDecimal percent;

    public EditorChoiceItemDTO(String label) {
        this.label = label;
    }
}
