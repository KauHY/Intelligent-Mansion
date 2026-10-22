package com.crlink.smartpark.customer.controller.response.editor;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditorAnalysisDetailDTO extends EditorAnalysisDTO {
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    private Integer number;
    @ApiModelProperty("文本")
    @ExcelProperty("答案文本")
    private String text;
    @ApiModelProperty("文件的key")
    private String key;


    public EditorAnalysisDetailDTO(String elementType, Integer number, String text) {
        super(elementType);
        this.number = number;
        this.text = text;
    }
}
