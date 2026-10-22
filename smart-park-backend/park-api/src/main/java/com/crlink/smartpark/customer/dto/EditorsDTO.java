package com.crlink.smartpark.customer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EditorsDTO implements Serializable {
    @NotNull
    private String id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "描述，第一行得文字")
    private String description;

    private List<EditorDTO> editors;

    private Map<String, Object> editorResults;
}
