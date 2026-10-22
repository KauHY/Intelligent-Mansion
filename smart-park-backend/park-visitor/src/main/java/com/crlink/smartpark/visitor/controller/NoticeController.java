package com.crlink.smartpark.visitor.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.model.dto.NoticeReqDTO;
import com.crlink.smartpark.visitor.model.query.NoticeQuery;
import com.crlink.smartpark.visitor.dto.NoticeDTO;
import com.crlink.smartpark.visitor.service.NoticeService;
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
 * 来访须知
 * */
@Slf4j
@RestController
@RequestMapping("/notice")
@Validated
@Api(tags = "访客模块-访客须知")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ApiOperation(value = "来访须知-新增", tags = "来访须知")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid NoticeReqDTO reqDTO) {
        reqDTO.setId(null);
        return ResponseResult.success(noticeService.saveOrUpdateNotice(reqDTO));
    }

    @ApiOperation(value = "来访须知-删除", tags = "来访须知")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(noticeService.removeById(id));
    }

    @ApiOperation(value = "来访须知-根据ID查询", tags = "来访须知")
    @GetMapping("/findById/{id}")
    public ResponseResult<NoticeDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(noticeService.getNoticeById(id));
    }

    @ApiOperation(value = "来访须知-修改", tags = "来访须知")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody @Valid NoticeReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        return ResponseResult.success(noticeService.saveOrUpdateNotice(reqDTO));
    }

    @ApiOperation(value = "来访须知-分页查询", tags = "来访须知")
    @PostMapping("/listNoticePage")
    public ResponseResult<PageInfo<NoticeDTO>> listNoticePage(@RequestBody @Valid NoticeQuery query) {
        return ResponseResult.success(noticeService.listNoticePage(query));
    }

}

