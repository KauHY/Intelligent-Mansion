package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.dto.CustomerInfoWxReqDTO;
import com.crlink.smartpark.business.query.CustomerInfoQuery;
import com.crlink.smartpark.business.service.CustomerInfoService;
import com.crlink.smartpark.business.vo.CustomerInfoWxVO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * park-business
 * 商机管理—客户表 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-客户管理-小程序")
@RequestMapping("/business/customer-info/wx")
public class WxCustomerInfoController {

    @Autowired
    private CustomerInfoService customerInfoService;


    @ApiOperation(value = "新增客户", tags = "智慧招商—客户管理-小程序")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid CustomerInfoWxReqDTO dto) {
        dto.setId(null);
        Boolean result = customerInfoService.saveOrUpdateWxCustomer(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "编辑客户", tags = "智慧招商—客户管理-小程序")
    @PostMapping("/update")
    public ResponseResult<Boolean> update(@RequestBody @Valid CustomerInfoWxReqDTO dto) {
        Boolean result = customerInfoService.saveOrUpdateWxCustomer(dto);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "根据ID查询", tags = "智慧招商—客户管理-小程序")
    @GetMapping("/getCustomerInfoById/{id}")
    public ResponseResult<CustomerInfoWxVO> getCustomerInfoById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        CustomerInfoWxVO result = customerInfoService.getWxCustomerInfoById(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "客户分页查询", tags = "智慧招商—客户管理-小程序")
    @PostMapping("/listCustomerWxPage")
    public ResponseResult<PageInfo<CustomerInfoWxVO>> listCustomerWxPage(@RequestBody CustomerInfoQuery query) {
        PageInfo<CustomerInfoWxVO> result = customerInfoService.listCustomerWxPage(query);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "客户删除", tags = "智慧招商—客户管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = customerInfoService.removeCustomerInfo(id);
        return ResponseResult.success(result);
    }

}

