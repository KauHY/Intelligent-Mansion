package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.annotation.InterfacePermission;
import com.crlink.smartpark.customer.controller.request.EditorResultQuery;
import com.crlink.smartpark.customer.controller.response.QuestionnaireAnalysisReportDTO;
import com.crlink.smartpark.customer.controller.response.QuestionnaireDataVO;
import com.crlink.smartpark.customer.controller.response.editor.EditorAnalysisDetailDTO;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.service.QuestionnaireService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/questionnaire/result")
@Api(tags = "问卷")
@Validated
@InterfacePermission
public class QuestionnaireResultController {


    @Autowired
    private QuestionnaireService questionnaireService;

    @ApiOperation(value = "问卷结果-数据统计-列表", tags = "问卷")
    @PostMapping("/statistics/list")
    public ResponseResult<PageInfo<QuestionnaireDataVO>> queryStatisticsByQuestionnaireId(@RequestBody BaseQuery<String> baseQuery) {
        PageInfo<QuestionnaireDataVO> list = questionnaireService.statistics(baseQuery);
        return ResponseResult.success(list);
    }

    @ApiOperation(value = "问卷结果-数据统计-数据提交详情", tags = "问卷")
    @GetMapping("/questionnaireSubmission/detail")
    public ResponseResult<EditorsDTO> getQuestionnaireSubmitDetail(@RequestParam("questionnaireSubmissionId") String questionnaireSubmissionId) {
        EditorsDTO result = questionnaireService.getQuestionnaireSubmitDetail(questionnaireSubmissionId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "问卷结果-分析报告", tags = "问卷")
    @GetMapping("/getQuestionnaireAnalysisReport")
    public ResponseResult<QuestionnaireAnalysisReportDTO> getQuestionnaireAnalysisReport(@RequestParam String questionnaireId) {
        QuestionnaireAnalysisReportDTO result = questionnaireService.getQuestionnaireAnalysisReport(questionnaireId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "问卷结果-答案详情", tags = "问卷")
    @PostMapping("/getQuestionnaireAnalysisDetail")
    public ResponseResult<PageInfo<EditorAnalysisDetailDTO>> getQuestionnaireAnalysisDetail(@RequestBody @Valid EditorResultQuery query) {
        PageInfo<EditorAnalysisDetailDTO> result = questionnaireService.getQuestionnaireAnalysisDetail(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "问卷结果-答案详情导出", tags = "问卷")
    @PostMapping("/analysisDetail/download")
    public void analysisDetailDownload(@RequestBody @Valid EditorResultQuery query) {
        questionnaireService.analysisDetailDownload(query);
    }
}
