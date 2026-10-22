package com.crlink.smartpark.visitor.controller.mobile;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.model.dto.AppointmentApplyDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorInfoCompleteDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorRecordQuery;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordListVO;
import com.crlink.smartpark.visitor.service.mobile.VisitorUserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/mobile/visitor/user")
@Api(tags = "移动端访客--普通用户")
public class VisitorUserMobileController {

    @Resource
    private VisitorUserService visitorUserService;

    /**
     * 用户预约
     *
     * @param dto
     */
    @PostMapping("/makeAppointment")
    @ApiOperation(value = "用户预约")
    public ResponseResult makeAppointment(@RequestBody @Valid AppointmentApplyDTO dto) {
        visitorUserService.makeAppointment(dto);
        return ResponseResult.success();
    }

    /**
     * 获取预约列表
     *
     * @param query
     * @return
     */
    @PostMapping("/listAppointments")
    @ApiOperation(value = "获取预约列表")
    public ResponseResult<PageInfo<MobileVisitorRecordListVO>> listAppointments(@RequestBody @Valid VisitorRecordQuery query) {
        PageInfo<MobileVisitorRecordListVO> pageInfo = visitorUserService.listAppointments(query);
        return ResponseResult.success(pageInfo);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "我的预约详情")
    public ResponseResult<MobileVisitorRecordListVO> detail(@PathVariable String id) {
        return ResponseResult.success(visitorUserService.detail(id));
    }

    /**
     * 取消预约
     *
     * @param id
     */
    @PutMapping("/cancelAppointment/{id}")
    @ApiOperation(value = "取消预约")
    public ResponseResult cancelAppointment(@PathVariable String id) {
        visitorUserService.cancelAppointment(id);
        return ResponseResult.success();
    }

    /**
     * @param id
     */
    @PutMapping("/completeVisitorInfo/{id}")
    @ApiOperation(value = "邀请访客补充信息")
    public ResponseResult completeVisitorInfo(@PathVariable String id, @RequestBody @Valid VisitorInfoCompleteDTO dto) {
        visitorUserService.completeVisitorInfo(id, dto);
        return ResponseResult.success();
    }

}
