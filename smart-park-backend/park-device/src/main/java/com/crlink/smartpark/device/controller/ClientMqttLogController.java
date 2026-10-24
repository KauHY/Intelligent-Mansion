package com.crlink.smartpark.device.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.model.query.ClientMqttLogQuery;
import com.crlink.smartpark.device.model.ClientMqttLogReqDTO;
import com.crlink.smartpark.device.dto.ClientMqttLogDTO;
import com.crlink.smartpark.device.service.ClientMqttLogService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-device
 * mqtt日志记录
 * */
@Slf4j
@RestController
@RequestMapping("/client-mqtt-log")
@Validated
public class ClientMqttLogController {

    @Autowired
    private ClientMqttLogService clientMqttLogService;


    @ApiOperation(value = "mqtt日志记录-新增", tags = "mqtt日志记录")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ClientMqttLogReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(clientMqttLogService.saveOrUpdateClientMqttLog(reqDTO));
    }


    @ApiOperation(value = "mqtt日志记录-删除", tags = "mqtt日志记录")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(clientMqttLogService.removeById(id));
    }


    @ApiOperation(value = "mqtt日志记录-根据ID查询", tags = "mqtt日志记录")
    @GetMapping("/findById/{id}")
    public ResponseResult<ClientMqttLogDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(clientMqttLogService.getClientMqttLogById(id));
    }


    @ApiOperation(value = "mqtt日志记录-修改", tags = "mqtt日志记录")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ClientMqttLogReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(clientMqttLogService.saveOrUpdateClientMqttLog(reqDTO));
    }


    @ApiOperation(value = "mqtt日志记录-分页查询", tags = "mqtt日志记录")
    @PostMapping("/listClientMqttLogPage")
    public ResponseResult<PageInfo<ClientMqttLogDTO>> listClientMqttLogPage(@RequestBody @Valid ClientMqttLogQuery query){
        return ResponseResult.success(clientMqttLogService.listClientMqttLogPage(query));
    }

}

