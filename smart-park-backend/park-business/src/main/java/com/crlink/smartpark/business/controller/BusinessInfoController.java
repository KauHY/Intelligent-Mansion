package com.crlink.smartpark.business.controller;

import com.crlink.smartpark.business.dto.BusinessInfoReqDTO;
import com.crlink.smartpark.business.query.BusinessInfoQuery;
import com.crlink.smartpark.business.query.WxBusinessInfoQuery;
import com.crlink.smartpark.business.service.BusinessInfoService;
import com.crlink.smartpark.business.vo.BusinessInfoVO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * park-business
 * */
@Slf4j
@RestController
@Api(tags = "智慧招商-房源招商")
@RequestMapping("/business/business-info")
public class BusinessInfoController {

    @Autowired
    private BusinessInfoService businessInfoService;


    @ApiOperation(value = "新增招商信息", tags = "房源招商")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid BusinessInfoReqDTO dto) {
        dto.setId(null);
        Boolean result = businessInfoService.saveOrUpdateBusinessInfo(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "修改招商信息", tags = "房源招商")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid BusinessInfoReqDTO dto) {
        AssertUtil.requireNotNull(dto.getId(), "10007");
        Boolean result = businessInfoService.saveOrUpdateBusinessInfo(dto);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "删除招商信息", tags = "房源招商")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = businessInfoService.deleteBusinessInfoById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "招商信息ID查询", tags = "房源招商")
    @GetMapping({"/findById/{id}", "/wx/findById/{id}"})
    public ResponseResult<BusinessInfoVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        BusinessInfoVO result = businessInfoService.getBusinessInfoDetailById(id);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "修改招商信息状态", tags = "房源招商")
    @PostMapping("/updateStatus")
    public ResponseResult<Boolean> updateStatus(@RequestBody @Valid UpdateStatusReqDTO statusReqDTO) {
        AssertUtil.requireNotNull(statusReqDTO.getId(), "10007");
        Boolean result = businessInfoService.updateStatus(statusReqDTO);
        return ResponseResult.success(result);
    }


    /**
     * @param businessInfoQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "分页查询招商信息", tags = "")
    @PostMapping("/listBusinessInfoPage")
    public ResponseResult<PageInfo<BusinessInfoVO>> listBusinessInfoPage(@RequestBody BusinessInfoQuery businessInfoQuery) {
        PageInfo<BusinessInfoVO> result = businessInfoService.listBusinessInfoPage(businessInfoQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "分页查询招商信息-C端小程序", tags = "")
    @PostMapping("/wx/wxListBusinessInfoPage")
    public ResponseResult<PageInfo<BusinessInfoVO>> wxListBusinessInfoPage(@RequestBody WxBusinessInfoQuery query) {
        PageInfo<BusinessInfoVO> result = businessInfoService.wxListBusinessInfoPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "筛选园区-C端小程序", tags = "")
    @GetMapping("/wx/listParkDict")
    public ResponseResult<List<DictVO>> listBusinessInfoParkDict() {
        List<DictVO> result = businessInfoService.listBusinessInfoParkDict();
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "房源招商-导出", tags = "房源招商")
    @PostMapping("/export")
    public void export(@RequestBody BusinessInfoQuery businessInfoQuery) {
        businessInfoService.export(businessInfoQuery);
    }

}

