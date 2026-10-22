package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.annotation.InterfacePermission;
import com.crlink.smartpark.customer.controller.request.form.FormSubmitReqDTO;
import com.crlink.smartpark.customer.dto.FormEditorsDTO;
import com.crlink.smartpark.customer.service.FormService;
import com.crlink.smartpark.customer.service.FormSubmissionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.crlink.smartpark.common.constants.enums.HeaderSource.USER_WX_SOURCE_B;

@Slf4j
@RestController
@RequestMapping("/form")
@Validated
@Api(tags = "信息统计-小程序")
@InterfacePermission(source = USER_WX_SOURCE_B)
public class WxFormController {

    @Autowired
    private FormService formService;

    @Autowired
    private FormSubmissionService formSubmissionService;

    @ApiOperation(value = "信息统计表单-信息填报", tags = "信息统计表单")
    @PostMapping("/submitForm")
    public ResponseResult<Boolean> submitForm(@RequestBody @Valid FormSubmitReqDTO reqDTO) {
        return ResponseResult.success(formSubmissionService.submitForm(reqDTO));
    }

    @ApiOperation(value = "信息统计表单-提交详情", tags = "信息统计表单")
    @GetMapping("/getSubmitDetail")
    public ResponseResult<FormEditorsDTO> getSubmitDetail(String formId, String employeeId) {
        FormEditorsDTO result = formSubmissionService.getSubmitDetail(formId, employeeId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "信息统计表单-列表，1信息填报，2我提交的，3我接受的,查询参数param", tags = "信息统计表单")
    @PostMapping("/list/{status}")
    public ResponseResult<PageInfo<FormEditorsDTO>> wxFormList(@PathVariable("status") @NotNull Integer status, @RequestBody BaseQuery<String> query) {
        PageInfo<FormEditorsDTO> result = formService.wxFormList(status, query);
        return ResponseResult.success(result);
    }

}
