package com.crlink.smartpark.contract.controller.meterfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.MeterConsumptionTradeDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionTradeQuery;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionTradeListVO;
import com.crlink.smartpark.contract.service.MeterConsumptionTradeService;
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
 * park-device
 * 租赁合同交易明细
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumptionTrade")
@Validated
@Api(tags = "水电费交易明细")
public class MeterConsumptionTradeController {

    @Autowired
    private MeterConsumptionTradeService meterConsumptionTradeService;


    @ApiOperation(value = "水电费交易明细-根据ID查询", tags = "水电费交易明细")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterConsumptionTradeDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterConsumptionTradeService.getMeterConsumptionTradeById(id));
    }


    @ApiOperation(value = "水电费交易明细-分页查询", tags = "水电费交易明细")
    @PostMapping("/listMeterConsumptionPage")
    public ResponseResult<PageInfo<MeterConsumptionTradeListVO>> listMeterConsumptionPage(@RequestBody @Valid MeterConsumptionTradeQuery query) {
        return ResponseResult.success(meterConsumptionTradeService.listMeterConsumptionTradePage(query));
    }

    @ApiOperation(value = "水电费交易明细-退款", tags = "水电费交易明细")
    @PostMapping("/returnMoney")
    public ResponseResult<Boolean> returnMoney(@RequestBody @Valid ReturnMoneyReqDTO dto) {
        return ResponseResult.success(meterConsumptionTradeService.returnMoney(dto));
    }

    @ApiOperation(value = "水电费交易明细-导出", tags = "水电费交易明细")
    @PostMapping("/export")
    public void export(@RequestBody @Valid MeterConsumptionTradeQuery query) {
        meterConsumptionTradeService.export(query);
    }


}

