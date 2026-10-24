package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.annotation.InterfacePermission;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.dto.QuestionnaireDTO;
import com.crlink.smartpark.customer.model.QuestionnaireReqDTO;
import com.crlink.smartpark.customer.model.dto.QuestionnaireUpdateStatusReqDTO;
import com.crlink.smartpark.customer.model.query.QuestionnaireQuery;
import com.crlink.smartpark.customer.model.vo.QuestionnaireVO;
import com.crlink.smartpark.customer.service.QuestionnaireService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * park-customer
 * 问卷
 * */
@Slf4j
@RestController
@RequestMapping("/questionnaire")
@Api(tags = "问卷")
@Validated
@InterfacePermission
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;


    @ApiOperation(value = "问卷-新增", tags = "问卷")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid QuestionnaireReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(questionnaireService.saveOrUpdateQuestionnaire(reqDTO));
    }

    @ApiOperation(value = "问卷-修改", tags = "问卷")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid QuestionnaireReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(questionnaireService.saveOrUpdateQuestionnaire(reqDTO));
    }


    @ApiOperation(value = "问卷-删除", tags = "问卷")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(questionnaireService.removeQuestionnaireById(id));
    }

    @ApiOperation(value = "问卷题目新增或编辑", tags = "表单模板")
    @PostMapping("/edit")
    public ResponseResult<Boolean> edit(@RequestBody @Valid EditorsDTO reqDTO) {
        Boolean result = questionnaireService.editQuestionnaire(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "问卷-根据ID查询", tags = "问卷")
    @GetMapping("/findById/{id}")
    public ResponseResult<QuestionnaireDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(questionnaireService.getQuestionnaireById(id));
    }

    @ApiOperation(value = "问卷-复制", tags = "问卷")
    @PostMapping("/copy/{id}")
    public ResponseResult<Boolean> copy(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(questionnaireService.copy(id));
    }


    @ApiOperation(value = "问卷-分页查询", tags = "问卷")
    @PostMapping("/listQuestionnairePage")
    public ResponseResult<PageInfo<QuestionnaireVO>> listQuestionnairePage(@RequestBody @Valid QuestionnaireQuery query) {
        return ResponseResult.success(questionnaireService.listQuestionnairePage(query));
    }

    @ApiOperation(value = "问卷-更改状态,2收集中，3已结束", tags = "问卷")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody @Valid QuestionnaireUpdateStatusReqDTO reqDTO) {
        return ResponseResult.success(questionnaireService.updateStatus(reqDTO));
    }


}

