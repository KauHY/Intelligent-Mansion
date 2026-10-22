package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.model.dto.ProcessConfigReqDTO;
import com.crlink.smartpark.visitor.model.dto.ProcessConfigStatusReqDTO;
import com.crlink.smartpark.visitor.model.query.ProcessConfigQuery;
import com.crlink.smartpark.visitor.dto.ProcessConfigDTO;
import com.crlink.smartpark.visitor.service.ProcessConfigService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * park-visitor
 * 访客流程配置
 * */
@Slf4j
@RestController
@RequestMapping("/process-config")
@Validated
@Api(tags = "访客模块-访客流程配置")
public class ProcessConfigController {

    @Autowired
    private ProcessConfigService processConfigService;

    @ApiOperation(value = "访客流程配置-新增", tags = "访客流程配置")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid ProcessConfigReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = processConfigService.saveOrUpdateProcessConfig(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "访客流程配置-删除", tags = "访客流程配置")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(processConfigService.removeById(id));
    }

    @ApiOperation(value = "访客流程配置-根据ID查询", tags = "访客流程配置")
    @GetMapping("/findById/{id}")
    public ResponseResult<ProcessConfigDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(processConfigService.getProcessConfigById(id));
    }

    @ApiOperation(value = "访客流程配置-修改", tags = "访客流程配置")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid ProcessConfigReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = processConfigService.saveOrUpdateProcessConfig(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "访客流程配置-分页查询", tags = "访客流程配置")
    @PostMapping("/listProcessConfigPage")
    public ResponseResult<PageInfo<ProcessConfigDTO>> listProcessConfigPage(@RequestBody ProcessConfigQuery query) {
        PageInfo<ProcessConfigDTO> result = processConfigService.listProcessConfigPage(query);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "访客流程配置-启用/禁用", tags = "访客流程配置")
    @PostMapping("/modifyStatus")
    public ResponseResult<Boolean> modifyStatus(@RequestBody @Valid ProcessConfigStatusReqDTO dto) {
        Boolean result = processConfigService.modifyStatus(dto);
        return ResponseResult.success(result);
    }

}

