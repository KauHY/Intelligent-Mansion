package com.crlink.smartpark.system.controller.enterprise;

import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.model.CompanyAuditDetailVo;
import com.crlink.smartpark.system.model.query.CompanyInfoAuditDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.model.query.CompanyAuditQuery;
import com.crlink.smartpark.system.model.CompanyAuditReqDTO;
import com.crlink.smartpark.system.dto.CompanyAuditDTO;
import com.crlink.smartpark.system.service.CompanyAuditService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * park-customer
 * 企业入驻审核
 * */
@Api(tags = "企业端-入驻企业")
@Slf4j
@RestController
@RequestMapping("/company-audit")
@Validated
public class CompanyAuditController {

    @Autowired
    private CompanyAuditService companyAuditService;

    @ApiOperation(value = "企业入驻申请-引导完善企业信息", tags = "企业入驻申请")
    @PostMapping("/getFlag")
    public ResponseResult<Boolean> getFlag() {
        return ResponseResult.success(companyAuditService.getFlag());
    }

    @ApiOperation(value = "企业入驻申请-申请入驻", tags = "企业入驻申请")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid CompanyAuditReqDTO reqDTO) {
        return ResponseResult.success(companyAuditService.saveOrUpdateCompanyAudit(reqDTO));
    }

    @ApiOperation(value = "企业入驻申请-取消申请", tags = "企业入驻申请")
    @GetMapping("/cancel/{id}")
    public ResponseResult<Boolean> cancel(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(companyAuditService.cancel(id));
    }

    @ApiOperation(value = "企业入驻审核-根据ID查询", tags = "企业入驻审核")
    @GetMapping("/findById/{id}")
    public ResponseResult<CompanyAuditDetailVo> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(companyAuditService.getCompanyAuditById(id));
    }

    @ApiOperation(value = "企业入驻-是否入驻园区", tags = "企业入驻")
    @GetMapping("/settled")
    public ResponseResult<List<Company>> settled() {
        return ResponseResult.success(companyAuditService.settled());
    }

    @ApiOperation(value = "企业入驻审核-审核", tags = "企业入驻审核")
    @PostMapping("/audit")
    public ResponseResult<Boolean> audit(@RequestBody @Valid CompanyInfoAuditDTO auditDTO) {
        return ResponseResult.success(companyAuditService.audit(auditDTO));
    }


    @ApiOperation(value = "企业入驻审核-分页查询", tags = "企业入驻审核")
    @PostMapping("/listCompanyAuditPage")
    public ResponseResult<PageInfo<CompanyAuditDTO>> listCompanyAuditPage(@RequestBody @Valid CompanyAuditQuery query) {
        return ResponseResult.success(companyAuditService.listCompanyAuditPage(query));
    }

}

