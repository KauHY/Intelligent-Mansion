package com.crlink.smartpark.common.controller;

import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.model.ShortMessageReqDTO;
import com.crlink.smartpark.common.service.NumberSeqService;
import com.crlink.smartpark.common.service.ShortMessageService;
import com.crlink.smartpark.common.web.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shortMessage")
@Validated
@Api(tags = "短信")
public class ShortMessageController {

    @Autowired
    private ShortMessageService shortMessageService;

    @ApiOperation(value = "获取短信", tags = "短信")
    @PostMapping(value = "/getSmsCode")
    public ResponseResult<Boolean> getSmsCode(@RequestBody @Validated ShortMessageReqDTO reqDTO) {
        return ResponseResult.success(shortMessageService.getSmsCode(reqDTO));
    }
}
