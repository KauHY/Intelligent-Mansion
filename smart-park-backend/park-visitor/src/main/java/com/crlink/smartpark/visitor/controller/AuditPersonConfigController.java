package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.service.AuditPersonConfigService;
import com.crlink.smartpark.visitor.dto.AuditPersonConfigSaveDTO;
import com.crlink.smartpark.visitor.dto.AuditPersonConfigUpdateDTO;
import com.crlink.smartpark.visitor.query.AuditPersonConfigListQuery;
import com.crlink.smartpark.visitor.vo.AuditPersonConfigListVO;
import com.crlink.smartpark.visitor.vo.AuditPersonConfigVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 访客审批人配置控制器
 * */
@RestController
@RequestMapping("/auditPersonConfig")
@Api(tags = "访客模块-访客审批人配置")
public class AuditPersonConfigController {

    @Resource
    private AuditPersonConfigService auditPersonConfigService;

    /**
     * 新增访客审批人配置
     */
    @ApiOperation(value = "新增访客审批人配置", tags = "访客审批人配置")
    @PostMapping
    public ResponseResult save(@RequestBody @Valid AuditPersonConfigSaveDTO dto) {
        auditPersonConfigService.save(dto);
        return ResponseResult.success();
    }

    /**
     * 修改访客审批人配置
     */
    @ApiOperation(value = "修改访客审批人配置", tags = "访客审批人配置")
    @PutMapping
    public ResponseResult updateById(@RequestBody @Valid AuditPersonConfigUpdateDTO dto) {
        auditPersonConfigService.update(dto);
        return ResponseResult.success();
    }

    /**
     * 通过id删除访客审批人配置
     */
    @ApiOperation(value = "通过id删除访客审批人配置", tags = "访客审批人配置")
    @DeleteMapping("/{id}")
    public ResponseResult removeById(@PathVariable("id") String id) {
        auditPersonConfigService.removeAuditPersonConfigById(id);
        return ResponseResult.success();
    }

    /**
     * 通过id查询访客审批人配置
     */
    @ApiOperation(value = "通过id查询访客审批人配置", tags = "访客审批人配置")
    @GetMapping("/{id}")
    public ResponseResult<AuditPersonConfigVO> getById(@PathVariable("id") String id) {
        return ResponseResult.success(auditPersonConfigService.getAuditPersonConfigById(id));
    }

    /**
     * 分页查询访客审批人配置
     */
    @ApiOperation(value = "分页查询访客审批人配置", tags = "访客审批人配置")
    @PostMapping("/page")
    public ResponseResult<PageInfo<AuditPersonConfigListVO>> pageList(@RequestBody @Valid AuditPersonConfigListQuery query) {
        return ResponseResult.success(auditPersonConfigService.pageList(query));
    }


}
