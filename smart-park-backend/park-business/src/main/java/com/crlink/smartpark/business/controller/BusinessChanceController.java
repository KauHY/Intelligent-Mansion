package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.controller.request.BusinessChanceOperationDTO;
import com.crlink.smartpark.business.dto.chance.*;
import com.crlink.smartpark.business.service.BusinessChanceService;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.crlink.smartpark.business.common.Constant.BUSINESS_SOURCE_FROM_CREATE;


@Slf4j
@RestController
@Api(tags = "智慧招商-商机管理")
@RequestMapping("/business/business-chance")
public class BusinessChanceController {

    @Autowired
    private BusinessChanceService businessChanceService;


    @ApiOperation(value = "商机-新增", tags = "商机管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid BusinessChanceReqDTO dto) {
        //商机来源：1 商机录入
        dto.setBusinessSource(BUSINESS_SOURCE_FROM_CREATE);
        dto.setId(null);
        Boolean result = businessChanceService.saveOrUpdateBusinessChance(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "商机-编辑", tags = "商机管理")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody BusinessChanceReqDTO dto) {
        AssertUtil.requireNotNull(dto.getId(), "10007");
//        dto.setChanceStage(null);
        Boolean result = businessChanceService.saveOrUpdateBusinessChance(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "商机-删除", tags = "商机管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = businessChanceService.removeBusinessChanceById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "商机-根据ID查询商机信息", tags = "商机管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<BusinessChanceVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        BusinessChanceVO result = businessChanceService.getBusinessChanceById(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "商机-根据ID查询跟进记录", tags = "商机管理")
    @PostMapping("/getFollowRecordsById/{id}")
    public ResponseResult<PageInfo<FollowBusinessChanceVO>> getFollowRecordsById(@PathVariable("id") @NotNull(message = "id不能为空") String id, @RequestBody BaseQuery<String> query) {
        PageInfo<FollowBusinessChanceVO> result = businessChanceService.getFollowRecordsById(id, query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "商机-根据ID查询阶段流程", tags = "商机管理")
    @GetMapping("/getStageProcessById/{id}")
    public ResponseResult<List<BusinessChanceRecordVO>> getStageProcessById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        List<BusinessChanceRecordVO> result = businessChanceService.getStageProcessById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "分页查询招商信息:type,0全部商机，1公共商机，2我的商机", tags = "")
    @PostMapping("/listBusinessInfoPage/{type}")
    public ResponseResult<PageInfo<BusinessChanceVO>> listBusinessChancePage(@PathVariable("type") Integer type, @RequestBody BusinessChanceQuery query) {
        PageInfo<BusinessChanceVO> result = businessChanceService.listBusinessChancePage(query, type);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "操作商机:操作类型：2分配商机、3领取商机、7转移商机、8释放商机", tags = "商机管理")
    @PostMapping("/operate")
    public ResponseResult<Boolean> operateBusinessChance(@RequestBody @Valid BusinessChanceOperationDTO dto) {
        Boolean result = businessChanceService.operateBusinessChance(dto, true);
        return ResponseResult.success(result);
    }

    /**
     * 【商机】提醒
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "商机-提醒", tags = "商机管理")
    @PostMapping("/remind")
    public ResponseResult<Boolean> remind(@RequestBody @Valid RemindBusinessChanceDTO dto) {
        Boolean result = businessChanceService.remindBusinessChance(dto);
        return ResponseResult.success(result);
    }

    /**
     * 【商机】跟进
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "商机-跟进", tags = "商机管理")
    @PostMapping("/follow")
    public ResponseResult<Boolean> follow(@RequestBody @Valid FollowBusinessChanceDTO dto) {
        Boolean result = businessChanceService.followBusinessChance(dto);
        return ResponseResult.success(result);
    }

    /**
     * 【商机】转成客户
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "商机-转成客户", tags = "商机管理")
    @PostMapping("/convertToCustomer")
    public ResponseResult<Boolean> convertToCustomer(@RequestBody ConvertToCustomerReqDTO reqDTO) {
        Boolean result = businessChanceService.convertToCustomer(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "商机-导出:type,0全部商机，1公共商机，2我的商机", tags = "商机管理")
    @PostMapping("/export/{type}")
    public void export(@PathVariable("type") Integer type, @RequestBody BusinessChanceQuery query) {
        businessChanceService.export(query, type);
    }

    @ApiOperation(value = "商机-根据ID查询商机信息", tags = "内部接口")
    @GetMapping("/getChanceCount")
    public Integer getChanceCount(@RequestParam("parkOrgId") String parkOrgId) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        Integer result = businessChanceService.getChanceCount(parkOrgId);
        return result;
    }
}

