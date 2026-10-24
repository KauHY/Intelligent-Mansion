package com.crlink.smartpark.enterprise.controller;

import com.crlink.smartpark.industrial.dto.GroupCompanyRelationDTO;
import com.crlink.smartpark.enterprise.model.GroupCompanyRelationDelDTO;
import com.crlink.smartpark.enterprise.model.query.GroupCompanyRelationQuery;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.enterprise.model.GroupCompanyRelationReqDTO;
import com.crlink.smartpark.enterprise.service.GroupCompanyRelationService;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * park-enterprise
 * 公司分组关联表
 * */
@Slf4j
@RestController
@RequestMapping("/group-company-relation")
@Validated
@Api(tags = "产业服务")
public class GroupCompanyRelationController {

    @Autowired
    private GroupCompanyRelationService groupCompanyRelationService;


    @ApiOperation(value = "企业群组-分页查询", tags = "企业产业链")
    @PostMapping("/listGroupCompanyPage")
    public ResponseResult<PageInfo<GroupCompanyRelationDTO>> listGroupCompanyPage(@RequestBody GroupCompanyRelationQuery query){
        return ResponseResult.success(groupCompanyRelationService.listGroupCompanyPage(query));
    }



}

