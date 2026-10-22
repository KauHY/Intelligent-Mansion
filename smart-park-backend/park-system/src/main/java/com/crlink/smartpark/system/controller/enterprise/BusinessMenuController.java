package com.crlink.smartpark.system.controller.enterprise;

import com.crlink.smartpark.system.entity.BusinessMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import com.crlink.smartpark.common.utils.AssertUtil;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.model.query.BusinessMenuQuery;
import com.crlink.smartpark.system.model.BusinessMenuReqDTO;
import com.crlink.smartpark.system.dto.BusinessMenuDTO;
import com.crlink.smartpark.system.service.BusinessMenuService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * park-customer
 * 企业端菜单/按钮表
 * */
@Slf4j
@RestController
@Api(tags = "企业端-企业端菜单/按钮")
@RequestMapping("/business-menu")
@Validated
public class BusinessMenuController {

    @Autowired
    private BusinessMenuService businessMenuService;

    @ApiOperation(value = "企业端菜单/按钮-获取配置菜单", tags = "企业端菜单/按钮")
    @PostMapping("/listBusinessMenu")
    public ResponseResult<List<BusinessMenu>> listBusinessMenuPage(){
        return ResponseResult.success(businessMenuService.listBusinessMenu());
    }

}

