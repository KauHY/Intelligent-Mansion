package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.dto.FormTemplateDTO;
import com.crlink.smartpark.customer.model.FormTemplateReqDTO;
import com.crlink.smartpark.customer.model.query.FormTemplateQuery;
import com.crlink.smartpark.customer.service.FormTemplateService;
import com.crlink.smartpark.system.vo.DictVO;
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
import java.util.List;

/**
 * park-customer
 * 表单模板
 * */
@Slf4j
@RestController
@RequestMapping("/formTemplate")
@Api(tags = "信息统计-表单模板")
@Validated
public class FormTemplateController {

    @Autowired
    private FormTemplateService formTemplateService;


    @ApiOperation(value = "表单模板-新增", tags = "表单模板")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid FormTemplateReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(formTemplateService.saveOrUpdateFormTemplate(reqDTO));
    }

    @ApiOperation(value = "表单模板-表单编辑", tags = "表单模板")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid FormTemplateReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(formTemplateService.saveOrUpdateFormTemplate(reqDTO));
    }


    @ApiOperation(value = "表单模板-删除", tags = "表单模板")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(formTemplateService.removeFormTemplateById(id));
    }


    @ApiOperation(value = "模板详情", tags = "表单模板")
    @GetMapping("/findById/{id}")
    public ResponseResult<FormTemplateDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(formTemplateService.getFormTemplateById(id));
    }


    @ApiOperation(value = "表单模板-编辑", tags = "表单模板")
    @PostMapping("/edit")
    public ResponseResult<Boolean> modify(@RequestBody @Valid EditorsDTO reqDTO) {
        Boolean result = formTemplateService.editFormTemplate(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "表单模板-分页查询", tags = "表单模板")
    @PostMapping("/listFormTemplatePage")
    public ResponseResult<PageInfo<FormTemplateDTO>> listFormTemplatePage(@RequestBody @Valid FormTemplateQuery query) {
        return ResponseResult.success(formTemplateService.listFormTemplatePage(query));
    }

    @ApiOperation(value = "表单模板-下拉框", tags = "表单模板")
    @GetMapping("/listFormTemplateDict")
    public ResponseResult<List<DictVO>> listFormTemplateDict(@RequestParam String parkOrgId) {
        List<DictVO> result = formTemplateService.listFormTemplateDict(parkOrgId);
        return ResponseResult.success(result);
    }


}

