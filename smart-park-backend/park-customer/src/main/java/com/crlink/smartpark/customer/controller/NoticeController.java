package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.customer.dto.NoticeDTO;
import com.crlink.smartpark.customer.model.NoticeReqDTO;
import com.crlink.smartpark.customer.model.query.NoticeQuery;
import com.crlink.smartpark.customer.service.NoticeService;
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
 * park-customer
 * 公告信息表
 * */
@Slf4j
@RestController
@Api(tags = "公告管理")
@RequestMapping("/notice")
@Validated
public class NoticeController {

    @Autowired
    private NoticeService noticeService;


    @ApiOperation(value = "公告-新增", tags = "公告管理")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Valid NoticeReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = noticeService.saveOrUpdateNotice(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "公告-删除", tags = "公告管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        Boolean result = noticeService.removeNoticeById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "公告-根据ID查询", tags = "公告管理")
    @GetMapping("/findById/{id}")
    public ResponseResult<NoticeDTO> findById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        NoticeDTO result = noticeService.getNoticeById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "公告-修改", tags = "公告管理")
    @PostMapping("/update")
    public ResponseResult<Boolean> modify(@RequestBody @Valid NoticeReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = noticeService.saveOrUpdateNotice(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "公告-分页查询", tags = "公告管理")
    @PostMapping("/listNoticePage")
    public ResponseResult<PageInfo<NoticeDTO>> listNoticePage(@RequestBody NoticeQuery query) {
        PageInfo<NoticeDTO> result = noticeService.listNoticePage(query);
        return ResponseResult.success(result);
    }

}

