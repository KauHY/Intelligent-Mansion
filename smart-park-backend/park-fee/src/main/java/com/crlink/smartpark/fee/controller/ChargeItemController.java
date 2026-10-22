package com.crlink.smartpark.fee.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.fee.controller.request.ChargeItemReqDTO;
import com.crlink.smartpark.fee.controller.request.StatusReqDTO;
import com.crlink.smartpark.fee.controller.request.query.ChargeItemQuery;
import com.crlink.smartpark.fee.dto.ChargeItemDTO;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.crlink.smartpark.fee.service.BillingSchemeService;
import com.crlink.smartpark.fee.service.ChargeItemService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * park-contract
 * 收费项
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-付费模块")
@RequestMapping("/charge-item")
@Validated
public class ChargeItemController {

    @Autowired
    private ChargeItemService chargeItemService;
    @Autowired
    private BillingSchemeService billingSchemeService;


    @ApiOperation(value = "收费项-新增", tags = "收费项")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Validated ChargeItemReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = chargeItemService.saveOrUpdateChargeItem(reqDTO);
        return ResponseResult.success(result);
    }

    //@ApiOperation(value = "收费项-是否被关联", tags = "true-被关联;false-未被关联")
    //@GetMapping("/checkRelated/{id}")
    //public ResponseResult<Boolean> checkRelated(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
    //    billingSchemeService.lambdaQuery().eq(BillingScheme::getChargeItemId, id).count() > 0
    //    return ResponseResult.success();
    //}

    @ApiOperation(value = "收费项-删除", tags = "收费项")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        AssertUtil.requireTrue(billingSchemeService.lambdaQuery().eq(BillingScheme::getChargeItemId, id).count() <= 0, "310004");
        return ResponseResult.success(chargeItemService.removeById(id));
    }


    @ApiOperation(value = "收费项-根据ID查询", tags = "收费项")
    @GetMapping("/findById/{id}")
    public ResponseResult<ChargeItemDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(chargeItemService.getChargeItemById(id));
    }


    @ApiOperation(value = "收费项-修改", tags = "收费项")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Validated ChargeItemReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(chargeItemService.saveOrUpdateChargeItem(reqDTO));
    }


    @ApiOperation(value = "收费项-分页查询", tags = "收费项")
    @PostMapping("/listChargeItemPage")
    public ResponseResult<PageInfo<ChargeItemDTO>> listChargeItemPage(@RequestBody ChargeItemQuery query) {
        return ResponseResult.success(chargeItemService.listChargeItemPage(query));
    }

    @ApiOperation(value = "收费项-设置状态", tags = "收费项")
    @PostMapping("/setStatus")
    public ResponseResult<Boolean> setStatus(@RequestBody @Validated StatusReqDTO reqDTO) {
        return ResponseResult.success(chargeItemService.updateStatusById(reqDTO));
    }

    @ApiOperation(value = "收费项-收费项下拉列表", tags = "收费项")
    @GetMapping("/selectChargeItemDict")
    public ResponseResult<List<DictVO>> selectChargeItemDict(@RequestParam("parkOrgId") String parkOrgId) {
        List<DictVO> result = chargeItemService.selectChargeItemDict(parkOrgId);
        return ResponseResult.success(result);
    }


    //@ApiOperation(value = "test", tags = "收费项")
    //@GetMapping("/test")
    //public ResponseResult<Map<String, ?>> test(String id) {
    //    Map<String, String> stringMap = (Map<String, String>) chargeItemService.id2ValueMap(Sets.newHashSet(id), ChargeItem::getChargeName);
    //    return ResponseResult.success(stringMap);
    //}


}

