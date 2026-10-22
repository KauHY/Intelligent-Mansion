package com.crlink.smartpark.contract.controller.meterfee;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.LateFeeDetail;
import com.crlink.smartpark.contract.model.query.CompanyPortBillQuery;
import com.crlink.smartpark.contract.model.query.RentContractBillQuery;
import com.crlink.smartpark.contract.model.request.BillUploadFileReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillCheckoutReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillDeductionFeeDTO;
import com.crlink.smartpark.contract.model.request.RentBillUpdateReqDTO;
import com.crlink.smartpark.contract.model.vo.CompanyPortBillListVO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionBillListVO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionBillVO;
import com.crlink.smartpark.contract.service.MeterConsumptionBillService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * park-device
 * 企业水电账单管理
 * */
@Slf4j
@RestController
@RequestMapping("/meterConsumptionBill")
@Api(tags = "企业水电账单管理")
@Validated
public class MeterConsumptionBillController {

    @Autowired
    private MeterConsumptionBillService meterConsumptionBillService;


    @ApiOperation(value = "企业水电账单管理-根据ID查询", tags = "企业水电账单管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<MeterConsumptionBillVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(meterConsumptionBillService.getMeterConsumptionBillById(id));
    }


    @ApiOperation(value = "企业水电账单管理-减免", tags = "企业水电账单管理")
    @PostMapping("/deductionFee")
    public ResponseResult<Boolean> deductionFee(@RequestBody @Valid RentBillDeductionFeeDTO dto) {
        return ResponseResult.success(meterConsumptionBillService.deductionFee(dto));
    }

    @ApiOperation(value = "企业水电账单管理-编辑", tags = "企业水电账单管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid RentBillUpdateReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionBillService.updateMeterConsumptionBill(reqDTO));
    }


    @ApiOperation(value = "企业水电账单管理-分页查询", tags = "企业水电账单管理")
    @PostMapping("/listMeterConsumptionBillPage")
    public ResponseResult<PageInfo<MeterConsumptionBillListVO>> listMeterConsumptionBillPage(@RequestBody @Valid RentContractBillQuery query) {
        return ResponseResult.success(meterConsumptionBillService.listMeterConsumptionBillPage(query));
    }

    @ApiOperation(value = "企业水电账单管理-导出", tags = "企业水电账单管理")
    @PostMapping("/export")
    public void export(@RequestBody @Valid RentContractBillQuery query) {
        meterConsumptionBillService.export(query);
    }

    @ApiOperation(value = "企业水电账单管理-收款", tags = "企业水电账单管理")
    @PostMapping("/checkout")
    public ResponseResult<Boolean> checkout(@RequestBody @Valid RentBillCheckoutReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionBillService.checkout(reqDTO));
    }

    @ApiOperation(value = "企业水电账单管理-根据账单ID查询滞纳金", tags = "企业水电账单管理")
    @GetMapping("/findLateFeeByBillId")
    public ResponseResult<LateFeeDetail> findLateFeeByBillId(@RequestParam("id") String id) {
        return ResponseResult.success(meterConsumptionBillService.findLateFeeByBillId(id));
    }

    @ApiOperation(value = "企业水电账单管理-根据账单ID查询数据(批量)", tags = "企业水电账单管理")
    @GetMapping("/findLateFeeByBillIds")
    public ResponseResult<LateFeeDetail> findLateFeeByBillIds(@RequestParam("ids") Set<String> ids) {
        return ResponseResult.success(meterConsumptionBillService.findLateFeeByBillIds(ids));
    }

    @ApiOperation(value = "企业端账单-分页查询", tags = "企业水电账单管理")
    @PostMapping("/company/listCompanyPortBillPage")
    public ResponseResult<PageInfo<CompanyPortBillListVO>> listCompanyPortBillPage(@RequestBody @Valid CompanyPortBillQuery query) {
        return ResponseResult.success(meterConsumptionBillService.listCompanyPortBillPage(query));
    }

    @ApiOperation(value = "企业端账单-上传凭证", tags = "企业水电账单管理")
    @PostMapping("/company/uploadFile")
    public ResponseResult<Boolean> uploadFiles(@RequestBody @Valid BillUploadFileReqDTO reqDTO) {
        return ResponseResult.success(meterConsumptionBillService.uploadFiles(reqDTO));
    }

}

