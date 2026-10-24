package com.crlink.smartpark.contract.controller.rentfee;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.ApartmentDataForScreen;
import com.crlink.smartpark.contract.dto.BillForScreen;
import com.crlink.smartpark.contract.dto.BillListForScreen;
import com.crlink.smartpark.contract.dto.LateFeeDetail;
import com.crlink.smartpark.contract.model.query.CompanyPortBillQuery;
import com.crlink.smartpark.contract.model.query.RentContractBillQuery;
import com.crlink.smartpark.contract.model.request.BillUploadFileReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillCheckoutReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillDeductionFeeDTO;
import com.crlink.smartpark.contract.model.request.RentBillUpdateReqDTO;
import com.crlink.smartpark.contract.model.vo.CompanyPortBillListVO;
import com.crlink.smartpark.contract.model.vo.RentContractBillListVO;
import com.crlink.smartpark.contract.model.vo.RentContractBillVO;
import com.crlink.smartpark.contract.service.RentContractBillService;
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
 * 租赁合同账单
 * */
@Api(tags = "租赁合同收费模块-租赁账单")
@Slf4j
@RestController
@RequestMapping("/rentContractBill")
@Validated
public class RentContractBillController {

    @Autowired
    private RentContractBillService rentContractBillService;


    @ApiOperation(value = "租赁合同账单-根据ID查询", tags = "租赁合同账单")
    @GetMapping("/findById/{id}")
    public ResponseResult<RentContractBillVO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(rentContractBillService.getRentContractBillById(id));
    }


    @ApiOperation(value = "租赁合同账单-减免", tags = "租赁合同账单")
    @PostMapping("/deductionFee")
    public ResponseResult<Boolean> deductionFee(@RequestBody @Valid RentBillDeductionFeeDTO dto) {
        return ResponseResult.success(rentContractBillService.deductionFee(dto));
    }

    @ApiOperation(value = "租赁合同账单-编辑", tags = "租赁合同账单")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid RentBillUpdateReqDTO reqDTO) {
        return ResponseResult.success(rentContractBillService.updateRentContractBill(reqDTO));
    }


    @ApiOperation(value = "租赁合同账单-分页查询", tags = "租赁合同账单")
    @PostMapping("/listRentContractBillPage")
    public ResponseResult<PageInfo<RentContractBillListVO>> listRentContractBillPage(@RequestBody @Valid RentContractBillQuery query) {
        PageInfo<RentContractBillListVO> result = rentContractBillService.listRentContractBillPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "租赁合同账单-导出", tags = "租赁合同账单")
    @PostMapping("/export")
    public void export(@RequestBody @Valid RentContractBillQuery query) {
        rentContractBillService.export(query);
    }

    @ApiOperation(value = "租赁合同账单-收款", tags = "租赁合同账单")
    @PostMapping("/checkout")
    public ResponseResult<Boolean> checkout(@RequestBody @Valid RentBillCheckoutReqDTO reqDTO) {
        reqDTO.setFromLeave(false);
        return ResponseResult.success(rentContractBillService.checkout(reqDTO));
    }

    @ApiOperation(value = "租赁合同账单-根据账单ID查询滞纳金", tags = "租赁合同账单")
    @GetMapping("/findLateFeeByBillId")
    public ResponseResult<LateFeeDetail> findLateFeeByBillId(@RequestParam("id") String id) {
        return ResponseResult.success(rentContractBillService.findLateFeeByBillId(id));
    }

    @ApiOperation(value = "租赁合同账单-根据账单ID查询数据(批量)", tags = "租赁合同账单")
    @GetMapping("/findLateFeeByBillIds")
    public ResponseResult<LateFeeDetail> findLateFeeByBillIds(@RequestParam("ids") String[] ids) {
        return ResponseResult.success(rentContractBillService.findLateFeeByBillIds(ids));
    }

    @ApiOperation(value = "企业端账单-分页查询", tags = "租赁合同账单")
    @PostMapping("/company/listCompanyPortBillPage")
    public ResponseResult<PageInfo<CompanyPortBillListVO>> listCompanyPortBillPage(@RequestBody @Valid CompanyPortBillQuery query) {
        return ResponseResult.success(rentContractBillService.listCompanyPortBillPage(query));
    }

    @ApiOperation(value = "企业端账单-上传凭证", tags = "租赁合同账单")
    @PostMapping("/company/uploadFile")
    public ResponseResult<Boolean> uploadFiles(@RequestBody @Valid BillUploadFileReqDTO reqDTO) {
        return ResponseResult.success(rentContractBillService.uploadFiles(reqDTO));
    }

    @ApiOperation(value = "收费管理", tags = "运营大屏")
    @GetMapping("/getBillForScreen")
    public ResponseResult<BillForScreen> getBillForScreen(@RequestParam("parkOrgId") String parkOrgId) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        BillForScreen result = rentContractBillService.getBillForScreen(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "收费管理-柱状图", tags = "运营大屏")
    @GetMapping("/getBillListForScreen")
    public ResponseResult<List<BillListForScreen>> getBillListForScreen(@RequestParam("parkOrgId") String parkOrgId) {
        return ResponseResult.success(rentContractBillService.getBillListForScreen(parkOrgId));
    }

    @ApiOperation(value = "公寓管理", tags = "运营大屏")
    @GetMapping("/getApartmentDataForScreen")
    public ResponseResult<ApartmentDataForScreen> getApartmentDataForScreen(@RequestParam("parkOrgId") String parkOrgId) {
        return ResponseResult.success(rentContractBillService.getApartmentDataForScreen(parkOrgId));
    }
}

