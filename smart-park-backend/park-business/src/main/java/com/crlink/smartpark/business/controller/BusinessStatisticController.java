package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.dto.chance.AnalysisOverview;
import com.crlink.smartpark.business.dto.chance.BusinessStatisticQuery;
import com.crlink.smartpark.business.service.BusinessChanceService;
import com.crlink.smartpark.business.service.CustomerInfoService;
import com.crlink.smartpark.common.web.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@Api(tags = "报表分析-商机分析")
@RequestMapping("/statement/businessStatistic")
public class BusinessStatisticController {

    @Autowired
    private BusinessChanceService businessChanceService;

    @Autowired
    private CustomerInfoService customerInfoService;


    @ApiOperation(value = "商机概览(key对应的字典BUSINESS_CHANCE_STAGE)", tags = "商机分析")
    @GetMapping("/getChanceOverview")
    public ResponseResult<AnalysisOverview> getChanceOverview(@Valid BusinessStatisticQuery query) {
        AnalysisOverview result = businessChanceService.getChanceOverview(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "线索来源分析(key对应的字典THREAD_SOURCE)", tags = "商机分析")
    @GetMapping("/getThreadSource")
    public ResponseResult<AnalysisOverview> getThreadSource(@Valid BusinessStatisticQuery query) {
        AnalysisOverview result = businessChanceService.getThreadSource(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "客户行业分析(key对应的字典INDUSTRY_FIELD)", tags = "商机分析")
    @GetMapping("/getCustomerIndustry")
    public ResponseResult<AnalysisOverview> getCustomerIndustry(@Valid BusinessStatisticQuery query) {
        AnalysisOverview result = customerInfoService.getCustomerIndustry(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "商机转换率：开始阶段 转化至 结束阶段 的百分比(key对应的字典BUSINESS_CHANCE_STAGE)", tags = "商机分析")
    @GetMapping("/getTransitionRate")
    public ResponseResult<AnalysisOverview> getTransitionRate(@Valid BusinessStatisticQuery query) {
        AnalysisOverview result = businessChanceService.getTransitionRate(query);
        return ResponseResult.success(result);
    }
}

