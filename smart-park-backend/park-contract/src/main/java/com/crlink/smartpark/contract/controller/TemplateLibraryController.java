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
import com.crlink.smartpark.contract.model.query.TemplateLibraryQuery;
import com.crlink.smartpark.contract.model.TemplateLibraryReqDTO;
import com.crlink.smartpark.contract.dto.TemplateLibraryDTO;
import com.crlink.smartpark.contract.service.TemplateLibraryService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * park-contract
 * 模板字段表
 * */
@Slf4j
@RestController
@RequestMapping("/template-library")
@Validated
@Api(tags = "园区运营平台-合同模板字段管理")
public class TemplateLibraryController {

    @Autowired
    private TemplateLibraryService templateLibraryService;

    @ApiOperation(value = "模板字段表-根据Type查询", tags = "模板字段表")
    @GetMapping("/findByType/{type}")
    public ResponseResult<Map<String, Object>> findByType(@PathVariable("type") @NotEmpty(message = "type不能为空") String type) {
        return ResponseResult.success(templateLibraryService.findByType(type));
    }

//    @ApiOperation(value = "模板字段表-新增", tags = "模板字段表")
//    @PostMapping("/create")
//    public ResponseResult<Boolean> create(@RequestBody @Valid TemplateLibraryReqDTO reqDTO) {
//        reqDTO.setId(null);
//        return ResponseResult.success(templateLibraryService.saveOrUpdateTemplateLibrary(reqDTO));
//    }
//
//
//    @ApiOperation(value = "模板字段表-删除", tags = "模板字段表")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(templateLibraryService.removeById(id));
//    }
//
//
//    @ApiOperation(value = "模板字段表-根据ID查询", tags = "模板字段表")
//    @GetMapping("/findById/{id}")
//    public ResponseResult<TemplateLibraryDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(templateLibraryService.getTemplateLibraryById(id));
//    }
//
//
//    @ApiOperation(value = "模板字段表-修改", tags = "模板字段表")
//    @PostMapping("/modify")
//    public ResponseResult<Boolean> modify(@RequestBody @Valid TemplateLibraryReqDTO reqDTO) {
//        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
//        return ResponseResult.success(templateLibraryService.saveOrUpdateTemplateLibrary(reqDTO));
//    }
//
//
//    @ApiOperation(value = "模板字段表-分页查询", tags = "模板字段表")
//    @PostMapping("/listTemplateLibraryPage")
//    public ResponseResult<PageInfo<TemplateLibraryDTO>> listTemplateLibraryPage(@RequestBody @Valid TemplateLibraryQuery query){
//        return ResponseResult.success(templateLibraryService.listTemplateLibraryPage(query));
//    }

}

