package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.dto.EquipmentLedgerDTO;
import com.crlink.smartpark.customer.model.dto.EquipmentLedgerReqDTO;
import com.crlink.smartpark.customer.model.query.EquipmentLedgerQuery;
import com.crlink.smartpark.customer.service.EquipmentLedgerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * park-customer
 * 设备台账
 * */
@Slf4j
@RestController
@RequestMapping("/equipment-ledger")
@Api(tags = "设备台账")
@Validated
public class EquipmentLedgerController {

    @Autowired
    private EquipmentLedgerService equipmentLedgerService;


    @ApiOperation(value = "设备台账-新增", tags = "设备台账")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid EquipmentLedgerReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(equipmentLedgerService.saveOrUpdateEquipmentLedger(reqDTO));
    }


    @ApiOperation(value = "设备台账-删除", tags = "设备台账")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(equipmentLedgerService.removeById(id));
    }


    @ApiOperation(value = "设备台账-根据ID查询", tags = "设备台账")
    @GetMapping("/findById/{id}")
    public ResponseResult<EquipmentLedgerDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(equipmentLedgerService.getEquipmentLedgerById(id));
    }


    @ApiOperation(value = "设备台账-修改", tags = "设备台账")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid EquipmentLedgerReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(equipmentLedgerService.saveOrUpdateEquipmentLedger(reqDTO));
    }

    @ApiOperation(value = "设备台账-报废", tags = "设备台账")
    @PostMapping("/scrap")
    public ResponseResult<Boolean> scrap(@RequestParam @NotBlank String id) {
        return ResponseResult.success(equipmentLedgerService.scrap(id));
    }


    @ApiOperation(value = "设备台账-分页查询", tags = "设备台账")
    @PostMapping("/listEquipmentLedgerPage")
    public ResponseResult<PageInfo<EquipmentLedgerDTO>> listEquipmentLedgerPage(@RequestBody EquipmentLedgerQuery query) {
        return ResponseResult.success(equipmentLedgerService.listEquipmentLedgerPage(query));
    }

    @ApiOperation(value = "设备台账-导出", tags = "设备台账")
    @PostMapping("/export")
    public void export(@RequestBody EquipmentLedgerQuery query) {
        equipmentLedgerService.export(query);
    }

    @ApiOperation(value = "设备台账-导入", tags = "设备台账")
    @PostMapping("/import")
    public ResponseResult<FileImportErrorInfoDTO> importEquipment(@RequestPart("file") MultipartFile file, @RequestParam("parkOrgId") String parkOrgId) {
        return ResponseResult.success(equipmentLedgerService.importEquipment(file, parkOrgId));
    }

    @ApiOperation(value = "设备台账-根据分类Id查询是否被关联", tags = "设备台账", notes = "true-已被关联；false-未被关联")
    @GetMapping("/hasAssociate")
    public ResponseResult<Boolean> hasAssociate(@RequestParam("categoryId") @NotEmpty(message = "id不能为空") String categoryId) {
        return ResponseResult.success(equipmentLedgerService.hasAssociate(categoryId));
    }
}

