package com.crlink.smartpark.device.controller;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.model.MeterExchangeReqDTO;
import com.crlink.smartpark.device.model.MeterReqDTO;
import com.crlink.smartpark.device.model.MeterStatusUpdateReqDTO;
import com.crlink.smartpark.device.model.query.MeterQuery;
import com.crlink.smartpark.device.service.MeterService;
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
import javax.validation.constraints.NotEmpty;

/**
 * park-customer
 * 仪表（电表和水表）
 *
  */
@Slf4j
@RestController
@RequestMapping("/meter")
@Api(tags = "智慧能源")
@Validated
public class MeterController {

    @Autowired
    private MeterService meterService;


    @ApiOperation(value = "仪表（1电能耗，2水能耗））-新增", tags = "仪表（电表和水表）")
    @PostMapping("/create")
    public ResponseResult create(@RequestBody @Valid MeterReqDTO reqDTO) {
        reqDTO.setId(null);
        meterService.saveOrUpdateMeter(reqDTO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "换表（1电能耗，2水能耗））-新增", tags = "仪表（电表和水表）")
    @PostMapping("/exchangeMeter")
    public ResponseResult<Boolean> exchangeMeter(@RequestBody @Valid MeterExchangeReqDTO reqDTO) {
        return ResponseResult.success(meterService.exchangeMeter(reqDTO));
    }


    @ApiOperation(value = "仪表（电表和水表）-删除", tags = "仪表（电表和水表）")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterService.removeMeterById(id));
    }


    @ApiOperation(value = "仪表（电表和水表）-根据ID查询", tags = "仪表（电表和水表）")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterService.getMeterById(id));
    }


    @ApiOperation(value = "仪表（电表和水表）-修改", tags = "仪表（电表和水表）")
    @PostMapping("/modify")
    public ResponseResult modify(@RequestBody @Valid MeterReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        meterService.saveOrUpdateMeter(reqDTO);
        return ResponseResult.success();
    }

    @ApiOperation(value = "仪表（电表和水表）-修改状态", tags = "仪表（电表和水表）")
    @PostMapping("/updateMeterStatus")
    public ResponseResult<Boolean> updateMeterStatus(@RequestBody MeterStatusUpdateReqDTO reqDTO) {
        AssertUtil.requireTrue(StrUtil.isNotBlank(reqDTO.getId()), "10007");
        return meterService.updateMeterStatus(reqDTO);
    }


    @ApiOperation(value = "仪表（1电表和2水表）-分页查询", tags = "仪表（电表和水表）")
    @PostMapping("/listMeterPage")
    public ResponseResult<PageInfo<MeterDTO>> listMeterPage(@RequestBody @Valid MeterQuery query) {
        return ResponseResult.success(meterService.listMeterPage(query));
    }

    @ApiOperation(value = "仪表（1电表和2水表）导出", tags = "仪表（电表和水表）")
    @PostMapping("/download")
    public void download(@RequestBody @Valid MeterQuery query) {
        meterService.download(query);
    }

    @ApiOperation(value = "仪表（1电表和2水表）-导入", tags = "仪表（电表和水表")
    @PostMapping("/importMeters/{deviceTye}")
    public ResponseResult<FileImportErrorInfoDTO> importMeters(@RequestPart("file") MultipartFile file, @PathVariable("deviceTye") Integer deviceTye, @RequestParam String parkOrgId) {
        AssertUtil.requireTrue(DictConstant.DeviceType.ELECTRICITY_METER.type == deviceTye || DictConstant.DeviceType.WATER_METER.type == deviceTye, "10003");
        FileImportErrorInfoDTO errorInfoDTO = meterService.importMeters(file, deviceTye, parkOrgId);
        return ResponseResult.success(errorInfoDTO);
    }


}

