package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.ProStockManagerQuery;
import com.crlink.smartpark.customer.model.dto.ProStockManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProStockManagerDTO;
import com.crlink.smartpark.customer.service.ProStockManagerService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

/**
 * park-customer
 * 库存管理
 * */
@Slf4j
@RestController
@Api(tags = "资产管理-库存管理")
@RequestMapping("/pro-stock-manager")
@Validated
public class ProStockManagerController {

    @Autowired
    private ProStockManagerService proStockManagerService;


    @ApiOperation(value = "库存管理-物品入库stockMode=1, id = null", tags = "库存管理")
    @PostMapping("/stockIn")
    public ResponseResult<Boolean> stockIn(@RequestBody @Valid ProStockManagerReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(proStockManagerService.saveOrUpdateProStockManager(reqDTO));
    }


//    @ApiOperation(value = "库存管理-删除", tags = "库存管理")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
//        return ResponseResult.success(proStockManagerService.removeById(id));
//    }


    @ApiOperation(value = "库存管理-根据ID查询", tags = "库存管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<ProStockManagerDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proStockManagerService.getProStockManagerById(id));
    }

    @ApiOperation(value = "库存管理-根据物品ID查询详情", tags = "库存管理")
    @GetMapping("/findByGoodsId/{goodsId}")
    public ResponseResult<ProStockManagerDTO> findByGoodsId(@PathVariable("goodsId") @NotEmpty(message = "id不能为空") String goodsId) {
        return ResponseResult.success(proStockManagerService.getProStockManagerByGoodsId(goodsId));
    }


    @ApiOperation(value = "库存管理-出入库,修改库存 stockMode区分,id非空", tags = "库存管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ProStockManagerReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(proStockManagerService.saveOrUpdateProStockManager(reqDTO));
    }


    @ApiOperation(value = "库存管理-分页查询", tags = "库存管理")
    @PostMapping("/listProStockManagerPage")
    public ResponseResult<PageInfo<ProStockManagerDTO>> listProStockManagerPage(@RequestBody @Valid ProStockManagerQuery query){
        return ResponseResult.success(proStockManagerService.listProStockManagerPage(query));
    }

    @ApiOperation(value = "库存管理-导入", tags = "库存管理")
    @PostMapping("/import")
    public ResponseResult<FileImportErrorInfoDTO> importStockIn(@RequestPart("file") MultipartFile file, @RequestParam @NotEmpty(message = "id不能为空") String parkOrgId) {
        return ResponseResult.success(proStockManagerService.importStockIn(file,parkOrgId));
    }

}

