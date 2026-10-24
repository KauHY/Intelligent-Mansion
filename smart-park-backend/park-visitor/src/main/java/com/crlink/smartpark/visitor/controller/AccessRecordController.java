package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.CurrentUserParkOrgIds;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.dto.AccessRecordDTO;
import com.crlink.smartpark.visitor.model.vo.AccessRecordListVO;
import com.crlink.smartpark.visitor.query.AccessRecordQuery;
import com.crlink.smartpark.visitor.query.AccessSuccessRecordQuery;
import com.crlink.smartpark.visitor.service.AccessRecordService;
import com.crlink.smartpark.visitor.vo.AccessRecordVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 园区运营平台-人行管理
 * */
@Slf4j
@RestController
@RequestMapping("/access-record")
@Api(tags = "人行管理-人脸抓拍记录")
@Validated
public class AccessRecordController {

    /**
     * 抓拍记录
     */
    @Autowired
    private AccessRecordService accessRecordService;

    /**
     * 【抓拍记录】根据ID查询
     *
     * @param id 主键
     * @return 抓拍记录实体
     */
    @ApiOperation(value = "抓拍记录-根据ID查询", tags = "人行管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<AccessRecordVO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        return ResponseResult.success(accessRecordService.getAccessRecordById(id));
    }

    /**
     * 【抓拍记录】分页查询
     *
     * @param accessRecordQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "抓拍记录-分页查询", tags = "人行管理")
    @PostMapping("/listPage")
    @CurrentUserParkOrgIds
    public ResponseResult<PageInfo<AccessRecordListVO>> findListByPage(@RequestBody AccessRecordQuery accessRecordQuery) {
        PageInfo<AccessRecordListVO> result = accessRecordService.listPage(accessRecordQuery);
        return ResponseResult.success(result);
    }

    /**
     * 【抓拍记录】导出
     *
     * @param accessRecordQuery 导出参数
     * @return 分页数据
     */
    @ApiOperation(value = "抓拍记录-导出", tags = "人行管理")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @CurrentUserParkOrgIds
    public void export(@RequestBody AccessRecordQuery accessRecordQuery) {
        accessRecordService.export(accessRecordQuery);
    }


    /**
     * 【通行记录】分页查询
     *
     * @param accessSuccessRecordQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "通行记录-分页查询", tags = "人行管理")
    @PostMapping("/successListPage")
    @CurrentUserParkOrgIds
    public ResponseResult<PageInfo<AccessRecordListVO>> successListPage(@RequestBody AccessSuccessRecordQuery accessSuccessRecordQuery) {
        AccessRecordQuery accessRecordQuery = new AccessRecordQuery();
        BeanUtils.copyProperties(accessSuccessRecordQuery, accessRecordQuery);
        accessRecordQuery.setIsPass(DictConstant.RecognizeResult.SUCCESS.getCode());
        PageInfo<AccessRecordListVO> result = accessRecordService.listPage(accessRecordQuery);
        return ResponseResult.success(result);
    }

    /**
     * 【通行记录】导出
     *
     * @param accessSuccessRecordQuery 导出参数
     * @return 分页数据
     */
    @ApiOperation(value = "通行记录-导出", tags = "人行管理")
    @RequestMapping(value = "/access/export", method = RequestMethod.POST)
    @CurrentUserParkOrgIds
    public void accessExport(@RequestBody AccessSuccessRecordQuery accessSuccessRecordQuery) throws IOException {
        AccessRecordQuery accessRecordQuery = new AccessRecordQuery();
        BeanUtils.copyProperties(accessSuccessRecordQuery, accessRecordQuery);
        accessRecordQuery.setIsPass(DictConstant.RecognizeResult.SUCCESS.getCode());
        accessRecordService.export(accessRecordQuery);
    }

    /**
     * 【通行考勤记录】导出
     *
     * @param accessSuccessRecordQuery 导出参数
     */
    @ApiOperation(value = "通行考勤记录-导出", tags = "人行管理")
    @RequestMapping(value = "/accessAttendance/export", method = RequestMethod.POST)
    @CurrentUserParkOrgIds
    public void accessAttendanceExport(@RequestBody AccessSuccessRecordQuery accessSuccessRecordQuery) throws IOException {
        AccessRecordQuery accessRecordQuery = new AccessRecordQuery();
        BeanUtils.copyProperties(accessSuccessRecordQuery, accessRecordQuery);
        accessRecordQuery.setIsPass(DictConstant.RecognizeResult.SUCCESS.getCode());
        accessRecordService.exportAttendance(accessRecordQuery);
    }

    @ApiOperation(value = "保存访客记录", tags = "访客记录")
    @PostMapping("/saveAccessRecord")
    Boolean saveAccessRecord(@RequestBody AccessRecordDTO accessRecordDTO) {
        return accessRecordService.saveAccessRecord(accessRecordDTO);
    }


    @ApiOperation(value = "处理人行记录历史数据", tags = "访客记录")
    @PostMapping("/buildHistorySceneImageUrl")
    ResponseResult<Boolean> buildHistorySceneImageUrl(@RequestBody BaseQuery query) {
        accessRecordService.buildHistorySceneImageUrl(query.getStartTime(), query.getEndTime());
        return ResponseResult.success();
    }

}

