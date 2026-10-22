package com.crlink.smartpark.meeting.service;

import com.crlink.smartpark.meeting.entity.ReserveAudit;
import com.crlink.smartpark.meeting.controller.request.query.ReserveAuditQuery;
import com.crlink.smartpark.meeting.model.dto.ReserveAuditReqDTO;
import com.crlink.smartpark.meeting.dto.ReserveAuditDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.meeting.model.dto.*;
import com.crlink.smartpark.meeting.vo.MeetingReserveDetailVO;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 会议室预订审核 服务类
 * </p>
 * */
public interface ReserveAuditService extends IService<ReserveAudit> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ReserveAudit, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateReserveAudit(ReserveAuditReqDTO reqDTO);

    /**
     * 变更会议时间
     *
     * @return 实体对象
     */
    Boolean modifyTime(ReserveAuditUpdateDTO reqDTO);

    /**
     * 取消预定
     *
     * @return 实体对象
     */
    Boolean cancelReserve(ReserveAuditCancelDTO reqDTO);

    /**
     * 取消预定
     *
     * @return 实体对象
     */
    Boolean addSummary(ReserveSummaryDTO reqDTO);

    /**
     * 预定审核
     *
     * @return 实体对象
     */
    Boolean review(ReserveReviewDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param query 查询条件
     * @return   实体对象
     */
    MeetingReserveDetailVO getReviewDetail(ReserveAuditDetailDTO query);


    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ReserveAuditDTO> listReserveAuditPage(ReserveAuditQuery query);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ReserveAuditDTO> myAuditPage(ReserveAuditQuery query);

    /**
     * 定时任务调用 - 超时自动取消

     * @return  int
     * @MethodName autoCancelReserve
     */
    int autoCancelReserve();
}
