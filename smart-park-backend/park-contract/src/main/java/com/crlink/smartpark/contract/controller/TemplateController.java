package com.crlink.smartpark.contract.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.model.query.TemplateQuery;
import com.crlink.smartpark.contract.model.TemplateReqDTO;
import com.crlink.smartpark.contract.dto.TemplateDTO;
import com.crlink.smartpark.contract.service.TemplateService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-contract
 * 合同模板管理
 * */
@Slf4j
@RestController
@RequestMapping("/template")
@Validated
@Api(tags = "园区运营平台-合同模板管理")
public class TemplateController {

    @Autowired
    private TemplateService templateService;


    @ApiOperation(value = "合同模板管理-新增", tags = "合同模板管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid TemplateReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(templateService.saveOrUpdateTemplate(reqDTO));
    }


    @ApiOperation(value = "合同模板管理-删除", tags = "合同模板管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(templateService.removeById(id));
    }


//    @ApiOperation(value = "合同模板管理-根据ID查询", tags = "合同模板管理")
//    @GetMapping("/findById/{id}")
//    public ResponseResult<TemplateDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(templateService.getTemplateById(id));
//    }


//    @ApiOperation(value = "合同模板管理-修改", tags = "合同模板管理")
//    @PostMapping("/modify")
//    public ResponseResult<Boolean> modify(@RequestBody @Valid TemplateReqDTO reqDTO) {
//        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
//        return ResponseResult.success(templateService.saveOrUpdateTemplate(reqDTO));
//    }


    @ApiOperation(value = "分页查询", tags = {"合同模板管理","合同下载"})
    @PostMapping("/listTemplatePage")
    public ResponseResult<PageInfo<TemplateDTO>> listTemplatePage(@RequestBody @Valid TemplateQuery query){
        return ResponseResult.success(templateService.listTemplatePage(query));
    }

}

