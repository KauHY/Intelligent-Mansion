package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.aop.PageView;
import com.crlink.smartpark.customer.service.InformationService;
import com.crlink.smartpark.customer.vo.WxInformationListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_B;
import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_C;

/**
 * <p>
 * 资讯信息表 前端控制器
 * </p>
 * */
@Slf4j
@RestController
@Api(tags = "资讯管理-小程序")
@RequestMapping("/information/wx")
public class WxInformationController {

    @Autowired
    private InformationService informationService;

    @ApiOperation(value = "资讯-首页-C端：param必传 园区id", tags = "资讯管理-小程序")
    @PostMapping("/homepage/c")
    public ResponseResult<PageInfo<WxInformationListVO>> infoHomeListForC(@RequestBody BaseQuery<String> query) {
        query.setPageNum(1);
        query.setPageSize(10);
        PageInfo<WxInformationListVO> result = informationService.listWxInformationPage(query, USER_WX_SOURCE_C);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "资讯-首页-B端:param必传 园区id", tags = "资讯管理-小程序")
    @PostMapping("/homepage/b")
    public ResponseResult<PageInfo<WxInformationListVO>> infoHomeListForB(@RequestBody BaseQuery<String> query) {
        query.setPageNum(1);
        query.setPageSize(10);
        PageInfo<WxInformationListVO> result = informationService.listWxInformationPage(query, USER_WX_SOURCE_B);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "资讯-分页查询:param必传 园区id", tags = "资讯管理-小程序")
    @PostMapping("/listInfoPage")
    public ResponseResult<PageInfo<WxInformationListVO>> infoListByPage(@RequestBody BaseQuery<String> query) {
        PageInfo<WxInformationListVO> result = informationService.listWxInformationPage(query, SystemUtil.getInstance().getSource());
        return ResponseResult.success(result);
    }

    @PageView
    @ApiOperation(value = "资讯信息-根据ID查询", tags = "资讯管理-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<WxInformationListVO> informationFindById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        WxInformationListVO result = informationService.getWxInformationDetailById(id);
        return ResponseResult.success(result);
    }

}

