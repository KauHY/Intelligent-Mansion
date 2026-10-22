package com.crlink.smartpark.common.controller;

import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.service.NumberSeqService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/number")
public class NumberController {

    @Autowired
    private NumberSeqService numberSeqService;

    @ApiOperation(value = "获取编号", tags = "后期编号")
    @GetMapping(value = "/getNumberSeq")
    public NumberSeqDTO getNumberSeq(String numberName) {
        NumberSeqDTO numberSeq = numberSeqService.getNumberSeq(numberName);
        log.info("获取编号，{}", numberSeq);
        return numberSeq;
    }
}
