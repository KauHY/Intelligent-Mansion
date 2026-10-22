package com.crlink.smartpark.contract.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.dto.ContractDTO;
import com.crlink.smartpark.contract.dto.ContractHouseStatusDTO;
import com.crlink.smartpark.contract.model.query.ContractListQuery;
import com.crlink.smartpark.contract.model.query.ContractRentedHouseQuery;
import com.crlink.smartpark.contract.model.request.AuditReqDto;
import com.crlink.smartpark.contract.model.request.ContractReqDTO;
import com.crlink.smartpark.contract.model.request.WithdrawReqDto;
import com.crlink.smartpark.contract.service.ContractService;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 园区运营平台-合同管理
 * */
@Slf4j
@Api(tags = "园区运营平台-合同模块")
@RestController
@RequestMapping("/contract")
@Validated
public class ContractController {

    /**
     * 合同
     */
    @Autowired
    private ContractService contractService;

    /**
     * 【合同】校验编号冲突
     * true：冲突；false: 不冲突
     *
     * @param no 合同编号
     * @param id 合同id
     * @return true：冲突；false: 不冲突
     */
    @ApiOperation(value = "合同-校验编号冲突", tags = "true：冲突；false: 不冲突")
    @GetMapping("/checkNoConflict/{no}")
    public ResponseResult<Boolean> checkNoConflict(@PathVariable(value = "no") @NotEmpty(message = "no不能为空") @ApiParam(value = "合同编号") String no,
                                                   @RequestParam(value = "id", required = false) @ApiParam(value = "合同id") String id) {
        return ResponseResult.success(contractService.checkNoConflict(no, id));
    }

    /**
     * 【合同】新增
     *
     * @param contractReqDTO 实体对象
     * @return true:成功；false：失败
     */
    @ApiOperation(value = "合同-新增", tags = "true:成功；false：失败")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Validated ContractReqDTO contractReqDTO) {
        contractReqDTO.setId(null);
        return ResponseResult.success(contractService.saveOrUpdateContract(contractReqDTO));
    }

    /**
     * 【合同】修改
     *
     * @param contractReqDTO 实体对象
     * @return true:成功；false：失败
     */
    @ApiOperation(value = "合同-修改", tags = "true:成功；false：失败")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Validated ContractReqDTO contractReqDTO) {
        return ResponseResult.success(contractService.saveOrUpdateContract(contractReqDTO));
    }

    /**
     * 【合同】变更
     *
     * @param contractReqDTO 实体对象
     * @return true:成功；false：失败
     */
    @ApiOperation(value = "合同-变更", tags = "true:成功；false：失败")
    @PostMapping("/change")
    public ResponseResult<Boolean> change(@RequestBody @Validated ContractReqDTO contractReqDTO) {
        return ResponseResult.success(contractService.changeContract(contractReqDTO));
    }

    /**
     * 【合同】续约
     *
     * @param contractReqDTO 实体对象
     * @return true:成功；false：失败
     */
    @ApiOperation(value = "合同-续约", tags = "true:成功；false：失败")
    @PostMapping("/extend")
    public ResponseResult<Boolean> extend(@RequestBody @Validated ContractReqDTO contractReqDTO) {
        contractReqDTO.setId(null);
        return ResponseResult.success(contractService.extendContract(contractReqDTO));
    }

    /**
     * 【合同】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "合同-删除", tags = "true:成功；false：失败")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(contractService.removeById(id));
    }

    /**
     * 【合同】作废
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "合同-作废", tags = "true:成功；false：失败")
    @PostMapping("/nullify/{id}")
    public ResponseResult<Boolean> nullify(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(contractService.nullifyById(id));
    }

    /**
     * 【合同】终止
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "合同-终止", tags = "true:成功；false：失败")
    @PostMapping("/termination/{id}")
    public ResponseResult<Boolean> termination(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(contractService.terminationById(id));
    }

    /**
     * 【合同】提交审核
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "合同-提交审核", tags = "true:成功；false：失败")
    @PostMapping("/submitAudit/{id}")
    public ResponseResult<Boolean> submitAudit(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(contractService.submitAudit(id));
    }

    /**
     * 【合同】退约
     *
     * @return 是否成功
     */
    @ApiOperation(value = "合同-退约申请", tags = "true:成功；false：失败")
    @PostMapping("/withdraw")
    public ResponseResult<Boolean> withdraw(@RequestBody @Validated WithdrawReqDto withdrawReqDto) {
        return ResponseResult.success(contractService.withdraw(withdrawReqDto));
    }

    /**
     * 【合同】审核
     *
     * @return 是否成功
     */
    @ApiOperation(value = "合同-审核", tags = "true:成功；false：失败")
    @PostMapping("/audit")
    public ResponseResult<Boolean> audit(@RequestBody @Validated AuditReqDto auditReqDto) {
        return ResponseResult.success(contractService.audit(auditReqDto));
    }


    /**
     * 【合同】根据ID查询
     *
     * @param id 主键
     * @return 合同实体
     */
    @ApiOperation(value = "合同-根据ID查询", tags = "合同")
    @GetMapping("/findById/{id}")
    public ResponseResult<ContractDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(contractService.getContractById(id));
    }

    /**
     * 【合同】分页查询
     *
     * @param contractListQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "合同-分页查询", tags = "合同")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<ContractDTO>> findListByPage(@RequestBody ContractListQuery contractListQuery) {
        PageInfo<ContractDTO> result = contractService.listPage(contractListQuery);
        return ResponseResult.success(result);
    }

    /**
     * 【合同】导出合同记录
     */
    @ApiOperation(value = "合同-导出合同记录", tags = "合同")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@RequestBody ContractListQuery contractListQuery) {
        contractService.export(contractListQuery);
    }

    /**
     * 【合同】获取合同日期内待生效和生效中所有的房屋id
     * 作为房屋查询接口listHousePage查询条件noIds
     *
     * @param contractRentedHouseQuery
     * @return 房屋id队列
     */
    @PostMapping("/getUnavailableHouse")
    @ApiOperation(value = "合同-获取合同日期内待提交、审核中、待生效、生效中所有的房屋id", tags = "作为房屋查询接口listHousePage查询条件noIds")
    ResponseResult<Set<String>> getUnavailableHouse(@RequestBody ContractRentedHouseQuery contractRentedHouseQuery) {
        return ResponseResult.success(contractService.getUnavailableHouse(contractRentedHouseQuery));
    }

    @ApiOperation(value = "合同-根据类型获取编号", tags = "合同", notes = "1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同")
    @GetMapping("/getNoByType")
    public ResponseResult<String> getNoByType(@RequestParam(value = "type") @ApiParam(value = "1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同", required = true) @NotNull int type) {
        return ResponseResult.success(contractService.getNoByType(type));
    }

    @ApiOperation(value = "合同-根据合同类型获取计算标准下拉", tags = "合同")
    @GetMapping("/getCalculationListByFeeType/{type}")
    public ResponseResult<List<DictVO>> getCalculationListByFeeType(@PathVariable("type") @NotNull(message = "type不能为空") int type) {
        return ResponseResult.success(contractService.getCalculationListByType(type));
    }

    @ApiOperation(value = "计费方案-分页查询", tags = "计费方案", notes = "只传园区Id和类型")
    @PostMapping("/listBillingSchemePage")
    public ResponseResult<PageInfo<BillingSchemeDTO>> listBillingSchemePage(
            @RequestBody ContractListQuery contractListQuery
    ) {
        PageInfo<BillingSchemeDTO> result = contractService.listBillingSchemePage(contractListQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "合同-费用模块根据类型获取合同下拉列表", tags = "合同")
    @GetMapping("/getDictListByType")
    public ResponseResult<List<DictVO>> getDictListByType(
            @RequestParam(value = "type") @ApiParam(value = "1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同", required = true) @NotNull(message = "type不能为空") Integer type,
            @RequestParam(value = "parkOrgId", required = false) String parkOrgId,
            @RequestParam(value = "companyId", required = false) String companyId) {
        return ResponseResult.success(contractService.getDictListByType(type, parkOrgId, companyId));
    }

    @ApiOperation(value = "合同-费用模块根据合同Id获取合同所关联的房屋", tags = "合同")
    @GetMapping("/getHouseListByContract/{contractIds}")
    public ResponseResult<List<DictVO>> getHouseListByContract(@PathVariable("contractIds") @NotEmpty(message = "id不能为空") String contractIds) {
        return ResponseResult.success(contractService.getHouseListByContract(contractIds));
    }

    @ApiOperation(value = "合同-获取生效中的合同关联的房屋", tags = "内部接口")
    @GetMapping(value = "/getEffectiveHouseIds")
    public Set<String> getEffectiveHouseIds() {
        Set<String> result = contractService.getEffectiveHouseIds();
        return result;
    }

    @ApiOperation(value = "合同-获取生效中的合同", tags = "内部接口,用于能耗模块")
    @GetMapping(value = "/getEffectiveContracts")
    public List<ContractDTO> getEffectiveContracts() {
        List<ContractDTO> result = contractService.getEffectiveContracts();
        return result;
    }

    @ApiOperation(value = "合同房屋状态", tags = "内部接口,用于运营大屏")
    @PostMapping(value = "/getContractHouseRelationFor3D")
    public Map<String, ContractHouseStatusDTO> getContractHouseRelationFor3D(@RequestBody Collection<String> houseIds) {
        Map<String, ContractHouseStatusDTO> result = contractService.getContractHouseRelationFor3D(houseIds);
        return result;
    }
}

