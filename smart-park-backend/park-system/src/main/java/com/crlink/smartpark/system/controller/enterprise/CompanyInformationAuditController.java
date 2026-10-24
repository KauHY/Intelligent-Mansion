package com.crlink.smartpark.system.controller.enterprise;

import com.crlink.smartpark.system.entity.CompanyInformation;
import com.crlink.smartpark.system.entity.CompanyInformationAudit;
import com.crlink.smartpark.system.model.query.CompanyInfoAuditDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.model.query.CompanyInformationAuditQuery;
import com.crlink.smartpark.system.model.CompanyInformationAuditReqDTO;
import com.crlink.smartpark.system.dto.CompanyInformationAuditDTO;
import com.crlink.smartpark.system.service.CompanyInformationAuditService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

/**
 * park-customer
 * 企业信息审核
 * */
@Api(tags = "企业端-企业信息")
@Slf4j
@RestController
@RequestMapping("/company-information-audit")
@Validated
public class CompanyInformationAuditController {

    @Autowired
    private CompanyInformationAuditService companyInformationAuditService;

    @ApiOperation(value = "企业信息-获取当前最新企业审核记录", tags = "企业信息")
    @PostMapping("/getLastAudit")
    public ResponseResult<CompanyInformationAudit> getLastAudit() {
        return ResponseResult.success(companyInformationAuditService.getLastAudit());
    }

    @ApiOperation(value = "企业信息-获取当前用户的企业详情", tags = "企业信息")
    @PostMapping("/getDetail")
    public ResponseResult<CompanyInformation> getDetail() {
        return ResponseResult.success(companyInformationAuditService.getDetail());
    }

    @ApiOperation(value = "企业信息-完善信息", tags = "企业信息")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid CompanyInformationAuditReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(companyInformationAuditService.saveOrUpdateCompanyInformationAudit(reqDTO));
    }

    @ApiOperation(value = "企业信息-取消申请", tags = "企业信息")
    @GetMapping("/cancel/{id}")
    public ResponseResult<Boolean> cancel(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(companyInformationAuditService.cancel(id));
    }

    @ApiOperation(value = "企业信息-根据ID查询", tags = "企业信息")
    @GetMapping("/findById/{id}")
    public ResponseResult<CompanyInformationAuditDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(companyInformationAuditService.getCompanyInformationAuditById(id));
    }

    @ApiOperation(value = "企业信息-重新提交", tags = "企业信息")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid CompanyInformationAuditReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(companyInformationAuditService.saveOrUpdateCompanyInformationAudit(reqDTO));
    }


    @ApiOperation(value = "企业信息审核-分页查询", tags = "企业信息审核")
    @PostMapping("/listCompanyInformationAuditPage")
    public ResponseResult<PageInfo<CompanyInformationAuditDTO>> listCompanyInformationAuditPage(@RequestBody @Valid CompanyInformationAuditQuery query) {
        return ResponseResult.success(companyInformationAuditService.listCompanyInformationAuditPage(query));
    }


    @ApiOperation(value = "企业信息审核-审核", tags = "企业信息审核")
    @PostMapping("/audit")
    public ResponseResult<Boolean> audit(@RequestBody @Valid CompanyInfoAuditDTO auditDTO) {
        return ResponseResult.success(companyInformationAuditService.audit(auditDTO));
    }

}

