package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.customer.model.dto.ProGoodsSettingsReqDTO;
import com.crlink.smartpark.customer.model.query.EquipmentLedgerQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.controller.request.query.ProGoodsManagerQuery;
import com.crlink.smartpark.customer.model.dto.ProGoodsManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProGoodsManagerDTO;
import com.crlink.smartpark.customer.service.ProGoodsManagerService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * park-customer 物品管理
 * */
@Slf4j
@RestController
@RequestMapping("/pro-goods-manager")
@Api(tags = "资产管理-物品管理")
@Validated
public class ProGoodsManagerController {

    @Autowired
    private ProGoodsManagerService proGoodsManagerService;

    @ApiOperation(value = "物品管理-新增", tags = "物品管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ProGoodsManagerReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(proGoodsManagerService.saveOrUpdateProGoodsManager(reqDTO));
    }

    @ApiOperation(value = "物品管理-删除", tags = "物品管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proGoodsManagerService.removeGoodsById(id));
    }

    @ApiOperation(value = "物品管理-根据ID查询", tags = "物品管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<ProGoodsManagerDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(proGoodsManagerService.getProGoodsManagerById(id));
    }

    @ApiOperation(value = "物品管理-修改", tags = "物品管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ProGoodsManagerReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        reqDTO.setParkOrgId(null);
        return ResponseResult.success(proGoodsManagerService.saveOrUpdateProGoodsManager(reqDTO));
    }

    @ApiOperation(value = "物品管理-分页查询", tags = "物品管理")
    @PostMapping("/listProGoodsManagerPage")
    public ResponseResult<PageInfo<ProGoodsManagerDTO>>
        listProGoodsManagerPage(@RequestBody @Valid ProGoodsManagerQuery query) {
        return ResponseResult.success(proGoodsManagerService.listProGoodsManagerPage(query));
    }

    @ApiOperation(value = "物品管理-设置预警值", tags = "物品管理")
    @PostMapping("/settingMinStock")
    public ResponseResult<Boolean> settingMinStock(@RequestBody @Valid ProGoodsSettingsReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(proGoodsManagerService.settingMinStock(reqDTO));
    }

    @ApiOperation(value = "物品管理-导出", tags = "物品管理")
    @PostMapping("/export")
    public void export(@RequestBody ProGoodsManagerQuery query) {
        proGoodsManagerService.export(query);
    }

    @ApiOperation(value = "物品管理-导入", tags = "物品管理")
    @PostMapping("/import")
    public ResponseResult<FileImportErrorInfoDTO> importGoods(@RequestPart("file") MultipartFile file, @RequestParam @NotEmpty(message = "id不能为空") String parkOrgId) {
        return ResponseResult.success(proGoodsManagerService.importGoods(file,parkOrgId));
    }
}
