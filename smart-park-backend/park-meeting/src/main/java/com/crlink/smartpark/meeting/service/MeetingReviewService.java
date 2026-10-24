package com.crlink.smartpark.meeting.service;

import com.crlink.smartpark.meeting.entity.MeetingReview;
import com.crlink.smartpark.meeting.controller.request.query.MeetingReviewQuery;
import com.crlink.smartpark.meeting.model.dto.MeetingReviewReqDTO;
import com.crlink.smartpark.meeting.dto.MeetingReviewDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 会议室审核人关联 服务类
 * </p>
 * */
public interface MeetingReviewService extends IService<MeetingReview> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReview, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateMeetingReview(MeetingReviewReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MeetingReviewDTO getMeetingReviewById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeetingReviewDTO> listMeetingReviewPage(MeetingReviewQuery query);

    /**
     * 根据会议室ID查询会议室审批关联表
     * @param meetingId:
     * @return  java.util.List<com.crlink.smartpark.meeting.dto.MeetingReviewDTO>
     * @MethodName findMeetingReviewByMeetingId
     */
    List<MeetingReviewDTO> findMeetingReviewByMeetingId(String meetingId);

    /**
     * 根据会议室ID移除相关审批权限
     * @param meetingId:
     * @return  java.lang.Boolean
     * @MethodName removeMeetingReviewByMeetingId
     */
    Boolean removeMeetingReviewByMeetingId(String meetingId);

    /**
     * 根据会议室ID增量更新审批权限
     * @param reviewList:
     * @param meetingId:
     * @return  java.lang.Boolean
     * @MethodName updateMeetingReviewByMeetingId
     */
    Boolean updateMeetingReviewByMeetingId(Set<String> reviewList,String meetingId);
}
