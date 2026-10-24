package com.crlink.smartpark.customer.controller;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.aop.PageView;
import com.crlink.smartpark.customer.service.NoticeService;
import com.crlink.smartpark.customer.vo.NoticeWxListVO;
import com.crlink.smartpark.customer.vo.WxNoticeVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_B;
import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_C;

/**
 * park-customer
 * 公告信息表
 * */
@Slf4j
@RestController
@Api(tags = "公告管理-小程序")
@RequestMapping("/notice/wx")
@Validated
public class WxNoticeController {

    @Autowired
    private NoticeService noticeService;


    @ApiOperation(value = "公告-分页查询:param必传 园区id", tags = "公告管理-小程序")
    @PostMapping("/listNoticePage")
    public ResponseResult<PageInfo<NoticeWxListVO>> noticeListByPage(@RequestBody BaseQuery<String> query) {
        AssertUtil.requireNotNull(query.getParam(),"10016");
        PageInfo<NoticeWxListVO> result = noticeService.listWxNoticePage(query, SystemUtil.getInstance().getSource(), false);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "公告-首页-C端:param必传 园区id", tags = "公告管理-小程序")
    @PostMapping("/homepage/c")
    public ResponseResult<PageInfo<NoticeWxListVO>> noticeHomeListForC(@RequestBody BaseQuery<String> query) {
        AssertUtil.requireNotNull(query.getParam(),"10016");
        query.setPageNum(1);
        query.setPageSize(5);
        PageInfo<NoticeWxListVO> result = noticeService.listWxNoticePage(query, USER_WX_SOURCE_C, true);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "公告-首页-B端：param必传 园区id", tags = "公告管理-小程序")
    @PostMapping("/homepage/b")
    public ResponseResult<PageInfo<NoticeWxListVO>> noticeHomeListForB(@RequestBody BaseQuery<String> query) {
        AssertUtil.requireNotNull(query.getParam(),"10016");
        query.setPageNum(1);
        query.setPageSize(5);
        PageInfo<NoticeWxListVO> result = noticeService.listWxNoticePage(query, USER_WX_SOURCE_B, true);
        return ResponseResult.success(result);
    }

    @PageView
    @ApiOperation(value = "公告-根据ID查询", tags = "公告管理-小程序")
    @GetMapping("/findById/{id}")
    public ResponseResult<WxNoticeVO> noticeFindById(@PathVariable("id") @NotEmpty(message = "id不能为空") String id) {
        return ResponseResult.success(noticeService.getWxNoticeDetailById(id));
    }

}

