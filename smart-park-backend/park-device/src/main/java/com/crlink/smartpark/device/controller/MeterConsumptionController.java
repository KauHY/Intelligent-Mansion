package com.crlink.smartpark.device.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.GetConsumptionByContract;
import com.crlink.smartpark.device.dto.MeterConsumptionDTO;
import com.crlink.smartpark.device.model.ConsumptionModifyReqDTO;
import com.crlink.smartpark.device.model.query.MeterConsumptionQuery;
import com.crlink.smartpark.device.service.MeterConsumptionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * park-device
 * 企业或房屋水电用量
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumption")
@Api(tags = "智慧能源-企业或房屋用电量")
@Validated
public class MeterConsumptionController {

    @Autowired
    private MeterConsumptionService meterConsumptionService;

    @ApiOperation(value = "企业或房屋水电用量-根据id查询", tags = "企业或房屋水电用量")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterConsumptionDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterConsumptionService.getMeterConsumptionById(id));
    }

    @ApiOperation(value = "企业或房屋水电用量-分页查询", tags = "企业或房屋水电用量")
    @PostMapping("/listMeterConsumptionPage")
    public ResponseResult<PageInfo<MeterConsumptionDTO>> listMeterConsumptionPage(@RequestBody @Valid MeterConsumptionQuery query) {
        return ResponseResult.success(meterConsumptionService.listMeterConsumptionPage(query));
    }

    @ApiOperation(value = "企业或房屋水电用量-报表导出", tags = "企业或房屋水电用量")
    @PostMapping("/downloadMeterConsumption")
    public void downloadMeterConsumption(@RequestBody @Valid MeterConsumptionQuery query) {
        meterConsumptionService.downloadMeterConsumption(query);
    }

    @ApiOperation(value = "企业或房屋水电用量-本月分摊用量修改", tags = "企业或房屋水电用量")
    @PostMapping("/modifyShareConsumption")
    public ResponseResult<Boolean> modifyShareConsumption(@RequestBody @Valid ConsumptionModifyReqDTO dto) {
        return ResponseResult.success(meterConsumptionService.modifyShareConsumption(dto));
    }

    @ApiOperation(value = "企业或房屋水电用量-列表查询（费用使用）", tags = "企业或房屋水电用量")
    @PostMapping("/listMeterConsumption")
    public List<MeterConsumptionDTO> listMeterConsumption(@RequestParam("nowMonth") String nowMonth) {
        return meterConsumptionService.listMeterConsumption(nowMonth);
    }

    @ApiOperation(value = "企业或房屋水电用量-根据合同Id或房屋Id获取水电上月用量", tags = "企业或房屋水电用量")
    @PostMapping("/getConsumptionByContract")
    public MeterConsumptionDTO getConsumptionByContract(@RequestBody @Valid GetConsumptionByContract dto) {
        return meterConsumptionService.getConsumptionByContract(dto);
    }

}

