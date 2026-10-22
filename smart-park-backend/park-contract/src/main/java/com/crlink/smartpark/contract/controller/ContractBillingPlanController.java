//package com.crlink.smartpark.contract.controller;
//
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import com.github.pagehelper.PageInfo;
//
//import com.crlink.smartpark.common.web.ResponseResult;
//import com.crlink.smartpark.contract.model.query.ContractBillingPlanQuery;
//import com.crlink.smartpark.contract.model.request.ContractBillingPlanReqDTO;
//import com.crlink.smartpark.contract.dto.ContractBillingPlanDTO;
//import com.crlink.smartpark.contract.service.ContractBillingPlanService;
//
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.constraints.NotNull;
//
///**
// * park-contract
// * 合同计费信息 前端控制器
// *
////// */
//@Slf4j
//@RestController
//@RequestMapping("/contract-billing-plan")
//public class ContractBillingPlanController {
//
//    /**
//    * 合同计费信息
//    */
//    @Autowired
//    private ContractBillingPlanService contractBillingPlanService;
//
//    /**
//    * 【合同计费信息】新增
//    *
//    * @param contractBillingPlanReqDTO 实体对象
//    * @return 返回保存成功的对象
//    */
//    @ApiOperation(value = "合同计费信息-新增", tags = "合同计费信息")
//    @PostMapping("/create")
//    public ResponseResult<Boolean> create(@RequestBody ContractBillingPlanReqDTO contractBillingPlanReqDTO) {
//        log.info("合同计费信息-新增，请求入参：ContractBillingPlan={}", contractBillingPlanReqDTO);
//        if(contractBillingPlanReqDTO == null) {
//            return ResponseResult.errorMessage("10003");
//        }
//        return ResponseResult.success(contractBillingPlanService.saveOrUpdateContractBillingPlan(contractBillingPlanReqDTO));
//    }
//
//    /**
//     * 【合同计费信息】删除
//     *
//     * @param id 主键
//     * @return 是否成功
//     */
//    @ApiOperation(value = "合同计费信息-删除", tags = "合同计费信息")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
//        return ResponseResult.success(contractBillingPlanService.removeById(id));
//    }
//
//    /**
//    * 【合同计费信息】根据ID查询
//    *
//    * @param id 主键
//    * @return 合同计费信息实体
//    */
//    @ApiOperation(value = "合同计费信息-根据ID查询", tags = "合同计费信息")
//    @GetMapping("/findById/{id}")
//    public ResponseResult<ContractBillingPlanDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
//        return ResponseResult.success(contractBillingPlanService.getContractBillingPlanById(id));
//    }
//
//    /**
//     * 【合同计费信息】修改
//     *
//    * @param contractBillingPlanReqDTO 实体对象
//    * @return 返回保存成功的对象
//    */
//    @ApiOperation(value = "合同计费信息-修改", tags = "合同计费信息")
//    @PostMapping("/modify")
//    public ResponseResult<Boolean> modify(@RequestBody ContractBillingPlanReqDTO contractBillingPlanReqDTO) {
//        log.info("合同计费信息-修改，请求入参：ContractBillingPlan={}", contractBillingPlanReqDTO);
//        if(contractBillingPlanReqDTO == null) {
//            return ResponseResult.errorMessage("10003");
//        }
//        return ResponseResult.success(contractBillingPlanService.saveOrUpdateContractBillingPlan(contractBillingPlanReqDTO));
//    }
//
//    /**
//    * 【合同计费信息】分页查询
//    *
//    * @param contractBillingPlanQuery 查询参数
//    * @return 分页数据
//    */
//    @ApiOperation(value = "合同计费信息-分页查询", tags = "合同计费信息")
//    @PostMapping("/listPage")
//    public ResponseResult<PageInfo<ContractBillingPlanDTO>> findListByPage(@RequestBody ContractBillingPlanQuery contractBillingPlanQuery){
//        log.info("合同计费信息-分页查询，请求入参：contractBillingPlanQuery={}", contractBillingPlanQuery);
//        if(contractBillingPlanQuery == null) {
//            return ResponseResult.errorMessage("10003");
//        }
//        PageInfo<ContractBillingPlanDTO> result = contractBillingPlanService.listPage(contractBillingPlanQuery);
//        log.info("合同计费信息-分页查询，返回值：result={}", result);
//        return ResponseResult.success(result);
//    }
//
//}
//
