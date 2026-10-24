package com.crlink.smartpark.fee.controller;

import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.fee.controller.request.CalculationReqDTO;
import com.crlink.smartpark.fee.controller.request.StatusReqDTO;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.controller.request.BillingSchemeReqDTO;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.service.BillingSchemeService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * park-contract
 * 计费方案
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-付费模块")
@RequestMapping("/billing-scheme")
@Validated
public class BillingSchemeController {

    @Autowired
    private BillingSchemeService billingSchemeService;


    @ApiOperation(value = "计费方案-新增", tags = "计费方案")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Validated BillingSchemeReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(billingSchemeService.saveOrUpdateBillingScheme(reqDTO));
    }


    @ApiOperation(value = "计费方案-删除", tags = "计费方案")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(billingSchemeService.removeById(id));
    }


    @ApiOperation(value = "计费方案-根据ID查询", tags = "计费方案")
    @GetMapping("/findById/{id}")
    public ResponseResult<BillingSchemeDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(billingSchemeService.getBillingSchemeById(id));
    }


    @ApiOperation(value = "计费方案-修改", tags = "计费方案")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Validated BillingSchemeReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(billingSchemeService.saveOrUpdateBillingScheme(reqDTO));
    }


    @ApiOperation(value = "计费方案-分页查询", tags = "计费方案")
    @PostMapping("/listBillingSchemePage")
    public ResponseResult<PageInfo<BillingSchemeDTO>> listBillingSchemePage(@RequestBody BillingSchemeQuery query) {
        PageInfo<BillingSchemeDTO> result = billingSchemeService.listBillingSchemePage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "计费方案-设置状态", tags = "计费方案")
    @PostMapping("/setStatus")
    public ResponseResult<Boolean> setStatus(@RequestBody @Validated StatusReqDTO reqDTO) {
        BillingScheme billingScheme = MyBeanUtils.copy(reqDTO, BillingScheme::new);
        return ResponseResult.success(billingSchemeService.updateById(billingScheme));
    }

    @ApiOperation(value = "计费方案-数值转换", tags = "计费方案")
    @GetMapping("/digitTransform")
    public ResponseResult<String> digitTransform(
            @RequestParam(value = "value") @NotNull(message = "待转换数值不能为空") @ApiParam(value = "待转换数值") BigDecimal originValue,
            @RequestParam(value = "reservedDigits") @NotNull(message = "保留位数不能为空") @ApiParam(value = "保留位数：0-取整数；1-保留一位小数；2-保留两位小数") Integer reservedDigits,
            @RequestParam(value = "carryMode") @NotNull(message = "进位方式不能为空") @ApiParam(value = "进位方式：4-四舍五入；0-向上取整；1-向下取整") Integer carryMode) {
        BigDecimal result = originValue.setScale(reservedDigits, carryMode);
        return ResponseResult.success(result.toString());
    }

    @ApiOperation(value = "计费方案-计算标准计算", tags = "计费方案")
    @PostMapping("/calculation")
    public ResponseResult<String> calculation(@RequestBody @Validated CalculationReqDTO calculationReqDTO) {
        return digitTransform(billingSchemeService.calculation(calculationReqDTO), calculationReqDTO.getReservedDigits(), calculationReqDTO.getCarryMode());
    }

    @ApiOperation(value = "计费方案-根据收费类型获取计算标准下拉", tags = "计费方案", notes =
            "1:房屋租金-建筑面积*月单价、套内面积*月单价<br>" +
                    "4:房屋租金-建筑面积*月单价<br>" +
                    "17、18、19:公寓租金-单间、标间、套间-天单价<br>" +
                    "20、21、22:公寓押金-单间、标间、套间-房间数*单价<br>" +
                    "9、10、11、12:电表、水表、公摊电费、公摊水费-用量*单价<br>" +
                    "其他返回全部")
    @GetMapping("/getCalculationListByFeeType")
    public ResponseResult<List<DictVO>> getCalculationListByFeeType(@RequestParam("type") @NotNull(message = "type不能为空") int type) {
        return ResponseResult.success(billingSchemeService.getCalculationListByFeeType(type));
    }


    @ApiOperation(value = "计费方案-根据收费项id获取计算标准下拉", tags = "计费方案", notes =
            "1:房屋租金-建筑面积*月单价、套内面积*月单价<br>" +
                    "4:房屋租金-建筑面积*月单价<br>" +
                    "17、18、19:公寓租金-单间、标间、套间-天单价<br>" +
                    "20、21、22:公寓押金-单间、标间、套间-房间数*单价<br>" +
                    "9、10、11、12:电表、水表、公摊电费、公摊水费-用量*单价<br>" +
                    "其他返回全部")
    @GetMapping("/getCalculationListByFeeId")
    public ResponseResult<List<DictVO>> getCalculationListByFeeId(@RequestParam("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(billingSchemeService.getCalculationListByFeeId(id));
    }
}

