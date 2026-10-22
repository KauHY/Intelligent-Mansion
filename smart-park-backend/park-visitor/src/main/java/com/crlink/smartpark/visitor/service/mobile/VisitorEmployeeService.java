package com.crlink.smartpark.visitor.service.mobile;

import com.crlink.smartpark.visitor.model.dto.EmployeeAuditDTO;
import com.crlink.smartpark.visitor.model.dto.QueryProcessNodeDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorRecordQuery;
import com.crlink.smartpark.visitor.model.dto.VisitorInviteDTO;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordListVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface VisitorEmployeeService {

    /**
     * 预约审核
     *
     * @param dto
     */
    void audit(String id, EmployeeAuditDTO dto);

    /**
     * 获取预约待审核/邀约的记录
     *
     * @param query
     * @return
     */
    PageInfo<MobileVisitorRecordListVO> listRecord(VisitorRecordQuery query);

    /**
     * 邀请访客
     *
     * @param dto
     */
    String inviteVisitor(VisitorInviteDTO dto);

    /**
     * 通过类型id获取邀约流程节点
     *
     * @param queryDTO
     * @return
     */
    List<String> getInviteProcessNodes(QueryProcessNodeDTO queryDTO);

}
