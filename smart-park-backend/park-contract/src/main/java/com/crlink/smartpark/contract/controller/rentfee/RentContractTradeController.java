package com.crlink.smartpark.contract.controller.rentfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.RentContractTradeDTO;
import com.crlink.smartpark.contract.model.query.RentContractTradeQuery;
import com.crlink.smartpark.contract.model.request.ReturnMoneyReqDTO;
import com.crlink.smartpark.contract.service.RentContractTradeService;
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
 * 租赁合同交易明细
 * */
@Api(tags = "租赁合同收费模块-交易明细")
@Slf4j
@RestController
@RequestMapping("/rentContractTrade")
@Validated
public class RentContractTradeController {

    @Autowired
    private RentContractTradeService rentContractTradeService;


    @ApiOperation(value = "租赁合同交易明细-根据ID查询", tags = "租赁合同交易明细")
    @GetMapping("/findById/{id}")
    public ResponseResult<RentContractTradeDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(rentContractTradeService.getRentContractTradeById(id));
    }


    @ApiOperation(value = "租赁合同交易明细-分页查询", tags = "租赁合同交易明细")
    @PostMapping("/listRentContractTradePage")
    public ResponseResult<PageInfo<RentContractTradeDTO>> listRentContractTradePage(@RequestBody @Valid RentContractTradeQuery query) {
        return ResponseResult.success(rentContractTradeService.listRentContractTradePage(query));
    }

    @ApiOperation(value = "租赁合同交易明细-退款", tags = "租赁合同交易明细")
    @PostMapping("/returnMoney")
    public ResponseResult<Boolean> returnMoney(@RequestBody @Valid ReturnMoneyReqDTO dto) {
        dto.setFromTrade(true);
        return ResponseResult.success(rentContractTradeService.returnMoney(dto));
    }

    @ApiOperation(value = "租赁合同交易明细-导出", tags = "租赁合同交易明细")
    @PostMapping("/export")
    public void export(@RequestBody @Valid RentContractTradeQuery query) {
        rentContractTradeService.export(query);
    }

}

