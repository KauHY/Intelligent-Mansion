package com.crlink.smartpark.visitor.controller.mobile;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.visitor.model.dto.EmployeeAuditDTO;
import com.crlink.smartpark.visitor.model.dto.QueryProcessNodeDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorRecordQuery;
import com.crlink.smartpark.visitor.model.dto.VisitorInviteDTO;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordListVO;
import com.crlink.smartpark.visitor.service.mobile.VisitorEmployeeService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mobile/visitor/employee")
@Api(tags = "移动端访客--员工")
public class VisitorEmployeeMobileController {

    @Resource
    private VisitorEmployeeService visitorEmployeeService;

    /**
     * 预约审核
     *
     * @param dto
     */
    @PostMapping("/audit/{id}")
    @ApiOperation(value = "预约审核")
    public ResponseResult audit(@PathVariable String id, @RequestBody @Valid EmployeeAuditDTO dto) {
        visitorEmployeeService.audit(id, dto);
        return ResponseResult.success();
    }

    /**
     * 获取预约待审核/邀约的记录
     *
     * @param query
     * @return
     */
    @PostMapping("/listRecord")
    @ApiOperation(value = "获取预约待审核/邀约的记录")
    public ResponseResult<PageInfo<MobileVisitorRecordListVO>> listRecords(@RequestBody @Valid VisitorRecordQuery query) {
        PageInfo<MobileVisitorRecordListVO> pageInfo = visitorEmployeeService.listRecord(query);
        return ResponseResult.success(pageInfo);
    }

    /**
     * 邀请访客
     *
     * @param dto
     * @return
     */
    @PostMapping("/inviteVisitor")
    @ApiOperation(value = "邀请访客")
    public ResponseResult inviteVisitor(@RequestBody @Valid VisitorInviteDTO dto) {
        return ResponseResult.success(visitorEmployeeService.inviteVisitor(dto));
    }

    /**
     * 通过类型id获取邀约流程节点
     *
     * @param queryDTO
     * @return
     */
    @PostMapping("/getInviteProcessNodes")
    @ApiOperation(value = "通过类型id获取邀约流程节点")
    public ResponseResult<List> getInviteProcessNodes(@RequestBody @Valid QueryProcessNodeDTO queryDTO) {
        return ResponseResult.success(visitorEmployeeService.getInviteProcessNodes(queryDTO));
    }

}
