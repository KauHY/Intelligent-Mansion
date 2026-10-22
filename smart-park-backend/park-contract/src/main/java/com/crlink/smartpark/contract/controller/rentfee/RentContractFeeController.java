package com.crlink.smartpark.contract.controller.rentfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.RentContractFeeDTO;
import com.crlink.smartpark.contract.model.query.RentContractFeeQuery;
import com.crlink.smartpark.contract.model.request.RentContractFeeCashDTO;
import com.crlink.smartpark.contract.model.request.RentContractFeeReqDTO;
import com.crlink.smartpark.contract.model.vo.RentContractFeeVO;
import com.crlink.smartpark.contract.service.RentContractFeeService;
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
 * park-customer
 * 租赁合同费用
 * */
@Api(tags = "租赁合同收费模块-租赁、押金收费")
@Slf4j
@RestController
@RequestMapping("/rentContractFee")
@Validated
public class RentContractFeeController {

    @Autowired
    private RentContractFeeService rentContractFeeService;

    @ApiOperation(value = "租赁合同费用-批量修改收款日期", tags = "租赁合同费用")
    @PostMapping("/modifyDate")
    public ResponseResult<Boolean> modifyDate(@RequestBody @Valid RentContractFeeReqDTO reqDTO) {
        return ResponseResult.success(rentContractFeeService.modifyDate(reqDTO));
    }

    @ApiOperation(value = "租赁合同费用-根据ID查询", tags = "租赁合同费用")
    @GetMapping("/findById/{id}")
    public ResponseResult<RentContractFeeVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(rentContractFeeService.getRentContractFeeById(id));
    }

    @ApiOperation(value = "租赁合同费用-批量转为已出", tags = "租赁合同费用")
    @PostMapping("/transform")
    public ResponseResult<Boolean> transform(@RequestBody @Valid RentContractFeeReqDTO reqDTO) {
        return ResponseResult.success(rentContractFeeService.transform(reqDTO));
    }

    @ApiOperation(value = "租赁合同费用-分页查询", tags = "租赁合同费用")
    @PostMapping("/listRentContractFeePage")
    public ResponseResult<PageInfo<RentContractFeeDTO>> listRentContractFeePage(@RequestBody @Valid RentContractFeeQuery query) {
        return ResponseResult.success(rentContractFeeService.listRentContractFeePage(query));
    }

    @ApiOperation(value = "租赁合同费用-退还押金", tags = "租赁合同费用")
    @PostMapping("/refund")
    public ResponseResult<Boolean> refund(@RequestBody @Valid RentContractFeeCashDTO reqDTO) {
        return ResponseResult.success(rentContractFeeService.refund(reqDTO));
    }

    @ApiOperation(value = "租赁合同费用-导出", tags = "租赁合同费用")
    @PostMapping("/export")
    public void exportCashFee(@RequestBody @Valid RentContractFeeQuery reqDTO) {
        rentContractFeeService.exportFee(reqDTO);
    }
    
    @ApiOperation(value = "租赁合同费用-生成滞纳金", tags = "租赁合同费用")
    @PostMapping("/createLate")
    public void createLate() {
        rentContractFeeService.createLate();
    }


}

