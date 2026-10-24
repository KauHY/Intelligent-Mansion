package com.crlink.smartpark.customer.controller.response.editor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class EditorAnalysisDTO implements Serializable {
    @ApiModelProperty("题目id")
    private String id;
    @ApiModelProperty("题目名")
    private String name;
    @ApiModelProperty("题目类型")
    private String elementType;

    public EditorAnalysisDTO(String elementType) {
        this.elementType = elementType;
    }

    public EditorAnalysisDTO(String id, String name, String elementType) {
        this.id = id;
        this.name = name;
        this.elementType = elementType;
    }
}
