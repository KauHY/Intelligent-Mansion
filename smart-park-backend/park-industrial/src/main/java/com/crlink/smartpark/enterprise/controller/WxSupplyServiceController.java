package com.crlink.smartpark.enterprise.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.industrial.dto.SupplyServiceDTO;
import com.crlink.smartpark.enterprise.model.query.SupplyServiceQuery;
import com.crlink.smartpark.enterprise.service.SupplyServiceService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * park-enterprise
 * 供应服务
 * */
@Slf4j
@RestController
@RequestMapping("/wx/supply-service")
@Validated
@Api(tags = "产业服务")
public class WxSupplyServiceController {

    @Autowired
    private SupplyServiceService supplyServiceService;



    @ApiOperation(value = "供应服务-小程序-根据ID查询", tags = "供应服务")
    @GetMapping("/findById/{id}")
    public ResponseResult<SupplyServiceDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(supplyServiceService.getSupplyServiceById(id));
    }

    @ApiOperation(value = "供应服务-小程序-分页查询", tags = "供应服务")
    @PostMapping("/listSupplyServicePage")
    public ResponseResult<PageInfo<SupplyServiceDTO>> listSupplyServicePage(@RequestBody @Valid SupplyServiceQuery query){
        return ResponseResult.success(supplyServiceService.listSupplyServicePage(query));
    }

}

