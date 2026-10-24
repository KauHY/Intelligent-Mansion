package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.MeterReadingRecordDTO;
import com.crlink.smartpark.device.model.MeterReadingReqDTO;
import com.crlink.smartpark.device.model.query.MeterReadingRecordQuery;
import com.crlink.smartpark.device.service.MeterReadingRecordService;
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
 * 每天抄表记录
 * */
@Slf4j
@RestController/**/
@RequestMapping("/meter-reading-record")
@Api(tags = "智慧能源")
@Validated
public class MeterReadingRecordController {

    @Autowired
    private MeterReadingRecordService meterReadingRecordService;


    @ApiOperation(value = "修正读数-修改", tags = "抄表记录")
    @PostMapping("/modifyMeterReading")
    public ResponseResult<Boolean> modifyMeterReading(@RequestBody @Valid MeterReadingReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(meterReadingRecordService.modifyMeterReading(reqDTO));
    }


    @ApiOperation(value = "每天抄表记录-删除", tags = "每天抄表记录")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterReadingRecordService.removeById(id));
    }

    @ApiOperation(value = "抄表记录-分页查询", tags = "抄表记录")
    @PostMapping("/listMeterReadingRecordPage")
    public ResponseResult<PageInfo<MeterReadingRecordDTO>> listMeterReadingRecordPage(@RequestBody @Valid MeterReadingRecordQuery query) {
        return ResponseResult.success(meterReadingRecordService.listMeterReadingRecordPage(query));
    }

    @ApiOperation(value = "抄表记录-导出", tags = "抄表记录")
    @PostMapping("/readingRecordPage/download")
    public void readingRecordPageDownload(@RequestBody @Valid MeterReadingRecordQuery query) {
        meterReadingRecordService.readingRecordPageDownload(query);
    }

    @ApiOperation(value = "抄表记录-导入读数", tags = "仪表（电表和水表")
    @PostMapping("/importMeterReadingRecord")
    public ResponseResult<FileImportErrorInfoDTO> importMeterReadingRecord(@RequestPart("file") MultipartFile file, @RequestParam String parkOrgId) {
        FileImportErrorInfoDTO errorInfoDTO = meterReadingRecordService.importMeterReadingRecord(file, parkOrgId);
        return ResponseResult.success(errorInfoDTO);
    }


}

