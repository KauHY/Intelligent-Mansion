package com.crlink.smartpark.device.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.model.query.ClientDeviceQuery;
import com.crlink.smartpark.device.model.ClientDeviceReqDTO;
import com.crlink.smartpark.device.dto.ClientDeviceDTO;
import com.crlink.smartpark.device.service.ClientDeviceService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-device
 * 设备管理
 * */
@Slf4j
@RestController
@RequestMapping("/client-device")
@Validated
public class ClientDeviceController {

    @Autowired
    private ClientDeviceService clientDeviceService;


    @ApiOperation(value = "设备管理-新增", tags = "设备管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ClientDeviceReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(clientDeviceService.saveOrUpdateClientDevice(reqDTO));
    }


    @ApiOperation(value = "设备管理-删除", tags = "设备管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(clientDeviceService.removeById(id));
    }


    @ApiOperation(value = "设备管理-根据ID查询", tags = "设备管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<ClientDeviceDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(clientDeviceService.getClientDeviceById(id));
    }


    @ApiOperation(value = "设备管理-修改", tags = "设备管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ClientDeviceReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(clientDeviceService.saveOrUpdateClientDevice(reqDTO));
    }


    @ApiOperation(value = "设备管理-分页查询", tags = "设备管理")
    @PostMapping("/listClientDevicePage")
    public ResponseResult<PageInfo<ClientDeviceDTO>> listClientDevicePage(@RequestBody @Valid ClientDeviceQuery query){
        return ResponseResult.success(clientDeviceService.listClientDevicePage(query));
    }

}

