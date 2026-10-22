package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.dto.CustomerInfoDTO;
import com.crlink.smartpark.business.query.CustomerInfoQuery;
import com.crlink.smartpark.business.service.CustomerInfoService;
import com.crlink.smartpark.business.vo.CustomerInfoListVO;
import com.crlink.smartpark.business.vo.CustomerInfoVO;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * park-business
 * 商机管理—客户表 前端控制器
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-客户管理")
@RequestMapping("/business/customer-info")
public class CustomerInfoController {

    @Autowired
    private CustomerInfoService customerInfoService;


    @ApiOperation(value = "新增客户", tags = "智慧招商—客户管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid CustomerInfoDTO customerInfoDTO) {
        customerInfoDTO.setId(null);
        Boolean result = customerInfoService.saveOrUpdateCustomer(customerInfoDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "客户删除", tags = "智慧招商—客户管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = customerInfoService.removeCustomerInfo(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "根据ID查询", tags = "智慧招商—客户管理")
    @GetMapping("/getCustomerInfoById/{id}")
    public ResponseResult<CustomerInfoVO> getCustomerInfoById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        CustomerInfoVO result = customerInfoService.getCustomerInfoById(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "编辑客户", tags = "智慧招商—客户管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid CustomerInfoDTO customerInfoDTO) {
        Boolean result = customerInfoService.saveOrUpdateCustomer(customerInfoDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "客户分页查询", tags = "智慧招商—客户管理")
    @PostMapping("/listCustomerPage")
    public ResponseResult<PageInfo<CustomerInfoListVO>> listCustomerPage(@RequestBody CustomerInfoQuery customerInfoQuery) {
        PageInfo<CustomerInfoListVO> result = customerInfoService.listCustomerPage(customerInfoQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "客户-导出", tags = "智慧招商—客户管理")
    @PostMapping("/export")
    public void export(@RequestBody CustomerInfoQuery customerInfoQuery) {
        customerInfoService.export(customerInfoQuery);
    }

    @ApiOperation(value = "客户-导入", tags = "智慧招商—客户管理")
    @PostMapping("/import")
    public ResponseResult<FileImportErrorInfoDTO> importGoods(@RequestPart("file") MultipartFile file, @RequestParam @NotEmpty(message = "id不能为空") String parkOrgId) {
        return ResponseResult.success(customerInfoService.importCustomerInfo(file, parkOrgId));
    }

}

