package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.aop.PageView;
import com.crlink.smartpark.customer.dto.InfoDTO;
import com.crlink.smartpark.customer.dto.InformationDTO;
import com.crlink.smartpark.customer.model.query.InformationQuery;
import com.crlink.smartpark.customer.service.InformationService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 资讯信息表 前端控制器
 * </p>
 * */
@Slf4j
@RestController
@Api(tags = "资讯管理")
@RequestMapping("/information")
public class InformationController {


    @Autowired
    private InformationService informationService;

    @ApiOperation(value = "资讯-新增", tags = "资讯管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> createInfo(@RequestBody @Valid InfoDTO infoDTO) {
        infoDTO.setId(null);
        return ResponseResult.success(informationService.saveOrUpdateInformation(infoDTO));
    }

    @ApiOperation(value = "资讯-编辑", tags = "资讯管理")
    @PostMapping("/update")
    public ResponseResult<Boolean> updateInfo(@RequestBody @Valid InfoDTO infoDTO) {
        return ResponseResult.success(informationService.saveOrUpdateInformation(infoDTO));
    }


    /**
     * 【资讯信息表】删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @ApiOperation(value = "资讯-删除", tags = "资讯管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        boolean result = informationService.removeInfoById(id);
        return ResponseResult.success(result);
    }

    @PageView
    @ApiOperation(value = "资讯根据ID查询", tags = "资讯管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<InformationDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        InformationDTO result = informationService.getInformationDetailById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "资讯-分页查询", tags = "资讯管理")
    @PostMapping("/listInfoPage")
    public ResponseResult<PageInfo<InformationDTO>> infoListByPage(@RequestBody InformationQuery informationQuery) {
        PageInfo<InformationDTO> result = informationService.listInformationPage(informationQuery);
        return ResponseResult.success(result);
    }

}

