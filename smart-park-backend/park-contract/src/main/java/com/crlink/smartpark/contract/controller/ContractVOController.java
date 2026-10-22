package com.crlink.smartpark.contract.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.contract.model.query.ApartmentPeopleHouseQuery;
import com.crlink.smartpark.contract.model.query.ContractVoListQuery;
import com.crlink.smartpark.contract.model.vo.ContractHouseVO;
import com.crlink.smartpark.contract.service.ContractVoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName: ContractVOController
 * @Package com.crlink.smartpark.contract.controller
 * @Description: 园区运营平台-合同视图模块
 * @Version 1.0
 */
@Slf4j
@Api(tags = "园区运营平台-合同视图模块")
@RestController
@RequestMapping("/contract/vo")
@Validated
public class ContractVOController {

    /**
     * 合同
     */
    @Autowired
    private ContractVoService contractVoService;

    /**
     * 合同视图模块
     *
     * @param query:
     * @return com.crlink.smartpark.common.web.ResponseResult<com.github.pagehelper.PageInfo < com.crlink.smartpark.contract.dto.ContractDTO>>
     * @MethodName findListByPage
     */
    @ApiOperation(value = "合同视图模块-分页查询", tags = "合同视图模块")
    @PostMapping("/listPage")
    public ResponseResult<PageInfo<ContractHouseVO>> findListByPage(@RequestBody @Validated ContractVoListQuery query) {
        PageInfo<ContractHouseVO> result = contractVoService.listPageWithHouseVO(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "合同视图模块-公寓人员管理关联房屋使用", tags = "合同视图模块")
    @PostMapping("/findHousePage")
    public ResponseResult<PageInfo<ContractHouseVO>> apartmentPeopleHouse(@RequestBody ApartmentPeopleHouseQuery query) {
        PageInfo<ContractHouseVO> result = contractVoService.apartmentPeopleHouse(query);
        return ResponseResult.success(result);
    }

    /**
     * 【合同】导出合同记录
     */
    @ApiOperation(value = "合同-导出合同记录", tags = "合同")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@RequestBody ContractVoListQuery contractVoListQuery) {
        contractVoService.export(contractVoListQuery);
    }


    @ApiOperation(value = "根据园区获取已租的房屋id集合", tags = "内部接口")
    @GetMapping("/getRentedHouseId")
    public Map<Integer, Set<String>> getRentedHouseId(@RequestParam("parkOrgId") String parkOrgId) {
        AssertUtil.requireNotNull(parkOrgId, "10016");
        Map<Integer, Set<String>> result = contractVoService.getRentedHouseId(parkOrgId);
        return result;
    }

}
