package com.crlink.smartpark.contract.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.model.request.ContractGenerateReqDTO;
import com.crlink.smartpark.contract.service.ContractDownloadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ContractDownloadController
 * @Package com.crlink.smartpark.contract.controller
 * @Description: 合同下载
 * @Version 1.0
 */
@Slf4j
@Api(tags = "园区运营平台-合同生成")
@RestController
@RequestMapping("/contract")
@Validated
public class ContractDownloadController {

    @Autowired
    private ContractDownloadService contractDownloadService;
    //
    ///**
    // * @return ResponseResult<Boolean>
    // * @MethodName checkNoConflict
    // */
    //@ApiOperation(value = "合同-下载", tags = "合同下载")
    //@GetMapping(value = "/exportContract")
    //public void exportContract(@RequestParam("contractTemplateId") String contractTemplateId) {
    //    //contractDownloadService.exportContract(contractTemplateId);
    //}


    @ApiOperation(value = "生成合同", tags = "合同下载")
    @RequestMapping(value = "/generateContract", method = RequestMethod.POST)
    public ResponseResult generateContract(@RequestBody ContractGenerateReqDTO reqDTO) {
        Boolean result = contractDownloadService.generateContract(reqDTO);
        return ResponseResult.success(result);
    }
}
