package com.crlink.smartpark.visitor.controller;

import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.annotations.DictTranslate;
import com.crlink.smartpark.common.constants.enums.VisitorRegisterTypeEnum;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.dto.VisitorRecordSubscribeDTO;
import com.crlink.smartpark.visitor.dto.VisitorSignOutDTO;
import com.crlink.smartpark.visitor.query.VisitorRecordListQuery;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import com.crlink.smartpark.visitor.vo.VisitorRecordSubScribeListVO;
import com.crlink.smartpark.visitor.vo.VisitorRecordVO;
import com.crlink.smartpark.visitor.vo.VisitorRegisterListVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 访客记录控制器
 * */
@RestController
@RequestMapping("/visitorRecord")
@Api(tags = "访客模块-访客记录")
@Validated
public class VisitorRecordController {

    @Resource
    private VisitorRecordService visitorRecordService;

    /**
     * 登记
     */
    @ApiOperation(value = "登记", tags = "访客记录")
    @PostMapping("/subscribe")
    public ResponseResult subscribe(@RequestBody @Valid VisitorRecordSubscribeDTO dto) {
        visitorRecordService.subscribe(dto);
        return ResponseResult.success();
    }

    /**
     * 通过id删除访客记录
     */
    @ApiOperation(value = "通过id删除访客记录", tags = "访客记录")
    @DeleteMapping("/{id}")
    public ResponseResult removeById(@PathVariable("id") String id) {
        visitorRecordService.removeVisitorRecordById(id);
        return ResponseResult.success();
    }

    /**
     * 通过id查询访客记录
     */
    @ApiOperation(value = "通过id查询访客记录", tags = "访客记录")
    @GetMapping("/{id}")
    public ResponseResult<VisitorRecordVO> getById(@PathVariable("id") String id) {
        return ResponseResult.success(visitorRecordService.getVisitorRecordById(id));
    }

    /**
     * 分页查询当前访客记录
     */
    @ApiOperation(value = "分页查询当前访客记录", tags = "访客记录")
    @PostMapping("/pageCurrentVisitor")
    @DictTranslate
    public ResponseResult<PageInfo<VisitorRegisterListVO>> pageCurrentVisitor(@RequestBody @Valid VisitorRecordListQuery query) {
        return ResponseResult.success(visitorRecordService.pageListVisitorRegister(query, VisitorRegisterTypeEnum.CURRENT));
    }

    /**
     * 分页查询历史历史记录
     */
    @ApiOperation(value = "分页查询历史访客记录", tags = "访客记录")
    @PostMapping("/pageHistoryVisitor")
    @DictTranslate
    public ResponseResult<PageInfo<VisitorRegisterListVO>> pageListVisitorRegister(@RequestBody @Valid VisitorRecordListQuery query) {
        return ResponseResult.success(visitorRecordService.pageListVisitorRegister(query, VisitorRegisterTypeEnum.HISTORY));
    }

    /**
     * 分页查询访客预约记录
     */
    @ApiOperation(value = "分页查询访客预约记录", tags = "访客记录")
    @PostMapping("/pageVisitorSubscribe")
    public ResponseResult<PageInfo<VisitorRecordSubScribeListVO>> pageListVisitorSubscribe(@RequestBody @Valid VisitorRecordListQuery query) {
        return ResponseResult.success(visitorRecordService.pageListVisitorSubscribe(query));
    }

    @ApiOperation(value = "通过人员设备ID查询访客记录", tags = "访客记录")
    @GetMapping("/getByPersonId/{devicePersonId}")
    public VisitorRecordVO getByPersonId(@PathVariable("devicePersonId") Long devicePersonId) {
        return visitorRecordService.getByPersonId(devicePersonId);
    }

    @ApiOperation(value = "更新门禁核验状态", tags = "访客记录")
    @PutMapping("/{devicePersonId}/updateDoorAccessStatus")
    public void updateDoorAccessStatus(@PathVariable("devicePersonId") Long devicePersonId) {
        visitorRecordService.updateDoorAccessStatus(devicePersonId);
    }

    /**
     * 该访客信息失效，转为历史访客
     * @param signOutDTO
     * @return
     */
    @ApiOperation(value = "签出", tags = "访客记录")
    @PutMapping("/signOut")
    public ResponseResult signOut(@RequestBody VisitorSignOutDTO signOutDTO) {
        AssertUtil.requireTrue(StrUtil.isNotBlank(signOutDTO.getId()), "10007");
        visitorRecordService.signOut(signOutDTO);
        return ResponseResult.success();
    }
}
