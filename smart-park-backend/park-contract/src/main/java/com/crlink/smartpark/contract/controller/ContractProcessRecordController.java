//package com.crlink.smartpark.contract.controller;
//
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import com.github.pagehelper.PageInfo;
//
//import com.crlink.smartpark.common.web.ResponseResult;
//import com.crlink.smartpark.contract.model.query.ContractProcessRecordQuery;
//import com.crlink.smartpark.contract.model.request.ContractProcessRecordReqDTO;
//import com.crlink.smartpark.contract.dto.ContractProcessRecordDTO;
//import com.crlink.smartpark.contract.service.ContractProcessRecordService;
//
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.constraints.NotNull;
//
///**
// * park-contract
// * 合同操作记录 前端控制器
// *
////// */
//@Slf4j
//@RestController
//@RequestMapping("/contract-process-record")
//public class ContractProcessRecordController {
//
//    /**
//    * 合同操作记录
//    */
//    @Autowired
//    private ContractProcessRecordService contractProcessRecordService;
//
//    /**
//    * 【合同操作记录】新增
//    *
//    * @param contractProcessRecordReqDTO 实体对象
//    * @return 返回保存成功的对象
//    */
//    @ApiOperation(value = "合同操作记录-新增", tags = "合同操作记录")
//    @PostMapping("/create")
//    public ResponseResult<Boolean> create(@RequestBody ContractProcessRecordReqDTO contractProcessRecordReqDTO) {
//        log.info("合同操作记录-新增，请求入参：ContractProcessRecord={}", contractProcessRecordReqDTO);
//        if(contractProcessRecordReqDTO == null) {
//            return ResponseResult.errorMessage("10003");
//        }
//        return ResponseResult.success(contractProcessRecordService.saveOrUpdateContractProcessRecord(contractProcessRecordReqDTO));
//    }
//
//    /**
//     * 【合同操作记录】删除
//     *
//     * @param id 主键
//     * @return 是否成功
//     */
//    @ApiOperation(value = "合同操作记录-删除", tags = "合同操作记录")
//    @DeleteMapping("/remove/{id}")
//    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
//        return ResponseResult.success(contractProcessRecordService.removeById(id));
//    }
//
//    /**
//    * 【合同操作记录】根据ID查询
//    *
//    * @param id 主键
//    * @return 合同操作记录实体
//    */
//    @ApiOperation(value = "合同操作记录-根据ID查询", tags = "合同操作记录")
//    @GetMapping("/findById/{id}")
//    public ResponseResult<ContractProcessRecordDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
//        return ResponseResult.success(contractProcessRecordService.getContractProcessRecordById(id));
//    }
//
//    /**
//     * 【合同操作记录】修改
//     *
//    * @param contractProcessRecordReqDTO 实体对象
//    * @return 返回保存成功的对象
//    */
//    @ApiOperation(value = "合同操作记录-修改", tags = "合同操作记录")
//    @PostMapping("/modify")
//    public ResponseResult<Boolean> modify(@RequestBody ContractProcessRecordReqDTO contractProcessRecordReqDTO) {
//        log.info("合同操作记录-修改，请求入参：ContractProcessRecord={}", contractProcessRecordReqDTO);
//        if(contractProcessRecordReqDTO == null) {
//            return ResponseResult.errorMessage("10003");
//        }
//        return ResponseResult.success(contractProcessRecordService.saveOrUpdateContractProcessRecord(contractProcessRecordReqDTO));
//    }
//
//    /**
//    * 【合同操作记录】分页查询
//    *
//    * @param contractProcessRecordQuery 查询参数
//    * @return 分页数据
//    */
//    @ApiOperation(value = "合同操作记录-分页查询", tags = "合同操作记录")
//    @PostMapping("/listPage")
//    public ResponseResult<PageInfo<ContractProcessRecordDTO>> findListByPage(@RequestBody ContractProcessRecordQuery contractProcessRecordQuery){
//        log.info("合同操作记录-分页查询，请求入参：contractProcessRecordQuery={}", contractProcessRecordQuery);
//        if(contractProcessRecordQuery == null) {
//            return ResponseResult.errorMessage("10003");
//        }
//        PageInfo<ContractProcessRecordDTO> result = contractProcessRecordService.listPage(contractProcessRecordQuery);
//        log.info("合同操作记录-分页查询，返回值：result={}", result);
//        return ResponseResult.success(result);
//    }
//
//}
//
