package com.crlink.smartpark.customer.controller.response;

import com.crlink.smartpark.customer.controller.response.editor.EditorAnalysisDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionnaireAnalysisReportDTO implements Serializable {
    @ApiModelProperty("目标总数")
    private Integer targetCount;

    @ApiModelProperty("实际总数")
    private Integer actualCount;

    @ApiModelProperty("百分比")
    private double percent;

    private List<EditorAnalysisDTO> analysis;
}
