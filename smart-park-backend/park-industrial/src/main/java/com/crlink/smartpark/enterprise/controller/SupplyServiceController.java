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
import com.crlink.smartpark.enterprise.model.query.SupplyServiceQuery;
import com.crlink.smartpark.enterprise.model.SupplyServiceReqDTO;
import com.crlink.smartpark.industrial.dto.SupplyServiceDTO;
import com.crlink.smartpark.enterprise.service.SupplyServiceService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-enterprise
 * 供应服务
 * */
@Slf4j
@RestController
@RequestMapping("/supply-service")
@Validated
@Api(tags = "产业服务")
public class SupplyServiceController {

    @Autowired
    private SupplyServiceService supplyServiceService;


    @ApiOperation(value = "供应服务-新增", tags = "供应服务")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid SupplyServiceReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(supplyServiceService.saveOrUpdateSupplyService(reqDTO));
    }


    @ApiOperation(value = "供应服务-删除", tags = "供应服务")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(supplyServiceService.removeById(id));
    }

    @ApiOperation(value = "企业服务-状态变更", tags = "企业服务")
    @PostMapping("/changeState")
    public ResponseResult<Boolean> changeState(@RequestBody @Valid StateReqDTO reqDTO) {
        return ResponseResult.success(supplyServiceService.changeState(reqDTO));
    }

    @ApiOperation(value = "供应服务-根据ID查询", tags = "供应服务")
    @GetMapping("/findById/{id}")
    public ResponseResult<SupplyServiceDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(supplyServiceService.getSupplyServiceById(id));
    }


    @ApiOperation(value = "供应服务-修改", tags = "供应服务")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid SupplyServiceReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(supplyServiceService.saveOrUpdateSupplyService(reqDTO));
    }


    @ApiOperation(value = "供应服务-分页查询", tags = "供应服务")
    @PostMapping("/listSupplyServicePage")
    public ResponseResult<PageInfo<SupplyServiceDTO>> listSupplyServicePage(@RequestBody @Valid SupplyServiceQuery query){
        return ResponseResult.success(supplyServiceService.listSupplyServicePage(query));
    }

}

