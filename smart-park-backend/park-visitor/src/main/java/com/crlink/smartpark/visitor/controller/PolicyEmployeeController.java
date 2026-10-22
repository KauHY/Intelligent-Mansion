package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.visitor.dto.DeviceIdsAndCardNosDTO;
import com.crlink.smartpark.visitor.service.PolicyEmployeeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * park-visitor
 * 策略员工表
 * */
@Slf4j
@RestController
@RequestMapping("/policy-employee")
@Validated
public class PolicyEmployeeController {

    @Autowired
    private PolicyEmployeeService policyEmployeeService;


    //@ApiOperation(value = "策略员工表-新增", tags = "策略员工表")
    //@PostMapping("/create")
    //public ResponseResult<Boolean> create(@RequestBody @Valid PolicyEmployeeReqDTO reqDTO) {
    //    reqDTO.setId(null);
    //    return ResponseResult.success(policyEmployeeService.saveOrUpdatePolicyEmployee(reqDTO));
    //}
    //
    //
    //@ApiOperation(value = "策略员工表-删除", tags = "策略员工表")
    //@DeleteMapping("/remove/{id}")
    //public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
    //    return ResponseResult.success(policyEmployeeService.removeById(id));
    //}


    @ApiOperation(value = "策略员工表-根据ID查询", tags = "策略员工表")
    @GetMapping("/{personId}/findByPersonId")
    public DeviceIdsAndCardNosDTO findByPersonId(@PathVariable("personId") @NotNull(message = "personId不能为空") Long personId) {
        return policyEmployeeService.getDeviceIdsAndCardNosByPersonId(personId);
    }


    //@ApiOperation(value = "策略员工表-修改", tags = "策略员工表")
    //@PostMapping("/modify")
    //public ResponseResult<Boolean> modify(@RequestBody @Valid PolicyEmployeeReqDTO reqDTO) {
    //    AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
    //    return ResponseResult.success(policyEmployeeService.saveOrUpdatePolicyEmployee(reqDTO));
    //}
    //
    //
    //@ApiOperation(value = "策略员工表-分页查询", tags = "策略员工表")
    //@PostMapping("/listPolicyEmployeePage")
    //public ResponseResult<PageInfo<PolicyEmployeeDTO>> listPolicyEmployeePage(@RequestBody @Valid PolicyEmployeeQuery query){
    //    return ResponseResult.success(policyEmployeeService.listPolicyEmployeePage(query));
    //}

}

