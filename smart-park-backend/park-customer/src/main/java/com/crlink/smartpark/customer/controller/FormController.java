package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.annotation.InterfacePermission;
import com.crlink.smartpark.customer.dto.FormDTO;
import com.crlink.smartpark.customer.model.FormReqDTO;
import com.crlink.smartpark.customer.model.dto.FormUpdateStatusReqDTO;
import com.crlink.smartpark.customer.model.query.FormQuery;
import com.crlink.smartpark.customer.service.FormService;
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
 * 信息统计表单
 * */
@Slf4j
@RestController
@RequestMapping("/form")
@Validated
@Api(tags = "信息统计-表单")
@InterfacePermission
public class FormController {

    @Autowired
    private FormService formService;


    @ApiOperation(value = "信息统计表单-新增", tags = "信息统计表单")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid FormReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(formService.saveOrUpdateForm(reqDTO));
    }


    @ApiOperation(value = "信息统计表单-删除", tags = "信息统计表单")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(formService.removeById(id));
    }


    @ApiOperation(value = "信息统计表单-根据ID查询", tags = "信息统计表单")
    @GetMapping("/findById/{id}")
    public ResponseResult<FormDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(formService.getFormById(id));
    }


    @ApiOperation(value = "信息统计表单-修改", tags = "信息统计表单")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid FormReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(formService.saveOrUpdateForm(reqDTO));
    }


    @ApiOperation(value = "信息统计表单-分页查询", tags = "信息统计表单")
    @PostMapping("/listFormPage")
    public ResponseResult<PageInfo<FormDTO>> listFormPage(@RequestBody @Valid FormQuery query) {
        return ResponseResult.success(formService.listFormPage(query));
    }

    @ApiOperation(value = "信息统计-更改状态,2收集中，3已结束", tags = "问卷")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody @Valid FormUpdateStatusReqDTO reqDTO) {
        return ResponseResult.success(formService.updateStatus(reqDTO));
    }


}

