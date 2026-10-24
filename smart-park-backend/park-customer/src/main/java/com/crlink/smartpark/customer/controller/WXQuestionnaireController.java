package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.annotation.InterfacePermission;
import com.crlink.smartpark.customer.controller.request.QuestionnaireSubmitReqDTO;
import com.crlink.smartpark.customer.dto.QuestionnaireDTO;
import com.crlink.smartpark.customer.service.QuestionnaireService;
import com.crlink.smartpark.customer.service.QuestionnaireSubmissionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crlink.smartpark.common.constants.enums.HeaderSource.USER_WX_SOURCE_B;

@Slf4j
@RestController
@RequestMapping("/wxQuestionnaire")
@Validated
@Api(tags = "问卷-小程序")
@InterfacePermission(source = USER_WX_SOURCE_B)
public class WXQuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionnaireSubmissionService questionnaireSubmissionService;

    @ApiOperation(value = "小程序问卷列表，0，未完成，1已完成，2已结束", tags = "问卷")
    @PostMapping("/listEmployeeQuestionnaires/{answeredStatus}")
    public ResponseResult<PageInfo<QuestionnaireDTO>> listEmployeeQuestionnaires(@PathVariable("answeredStatus") Integer answeredStatus,
                                                                                 @RequestBody @Valid BaseQuery query) {
        PageInfo<QuestionnaireDTO> result = questionnaireService.listEmployeeQuestionnaires(answeredStatus, query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "问卷填报", tags = "问卷")
    @PostMapping("/submitQuestionnaire")
    public ResponseResult<Boolean> submitQuestionnaire(@RequestBody @Valid QuestionnaireSubmitReqDTO dto) {
        Boolean result = questionnaireSubmissionService.submitQuestionnaire(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "问卷填报答案", tags = "问卷")
    @GetMapping("/getQuestionnaireSubmitResult")
    public ResponseResult<Map<String, Object>> getQuestionnaireSubmitResult(@RequestParam String questionnaireId) {
        Map<String, Object> result = questionnaireSubmissionService.getQuestionnaireSubmitResult(questionnaireId);
        return ResponseResult.success(result);
    }
}

