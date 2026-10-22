package com.crlink.smartpark.enterprise.controller;

import com.crlink.smartpark.enterprise.model.StateReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.enterprise.model.query.EnterpriseServiceQuery;
import com.crlink.smartpark.enterprise.model.EnterpriseServiceReqDTO;
import com.crlink.smartpark.industrial.dto.EnterpriseServiceDTO;
import com.crlink.smartpark.enterprise.service.EnterpriseServiceService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * park-enterprise
 * 企业服务
 * */
@Slf4j
@RestController
@RequestMapping("/enterprise-service")
@Validated
@Api(tags = "产业服务")
public class EnterpriseServiceController {

    @Autowired
    private EnterpriseServiceService enterpriseServiceService;


    @ApiOperation(value = "企业服务-新增", tags = "企业服务")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid EnterpriseServiceReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(enterpriseServiceService.saveOrUpdateEnterpriseService(reqDTO));
    }


    @ApiOperation(value = "企业服务-删除", tags = "企业服务")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(enterpriseServiceService.removeById(id));
    }


    @ApiOperation(value = "企业服务-根据ID查询", tags = "企业服务")
    @GetMapping("/findById/{id}")
    public ResponseResult<EnterpriseServiceDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(enterpriseServiceService.getEnterpriseServiceById(id));
    }


    @ApiOperation(value = "企业服务-修改", tags = "企业服务")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid EnterpriseServiceReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        reqDTO.setUpdateTime(LocalDateTime.now());
        return ResponseResult.success(enterpriseServiceService.saveOrUpdateEnterpriseService(reqDTO));
    }

    @ApiOperation(value = "企业服务-状态变更", tags = "企业服务")
    @PostMapping("/changeState")
    public ResponseResult<Boolean> changeState(@RequestBody @Valid StateReqDTO reqDTO) {
        return ResponseResult.success(enterpriseServiceService.changeState(reqDTO));
    }


    @ApiOperation(value = "企业服务-分页查询", tags = "企业服务")
    @PostMapping("/listEnterpriseServicePage")
    public ResponseResult<PageInfo<EnterpriseServiceDTO>> listEnterpriseServicePage(@RequestBody @Valid EnterpriseServiceQuery query){
        return ResponseResult.success(enterpriseServiceService.listEnterpriseServicePage(query));
    }

}

