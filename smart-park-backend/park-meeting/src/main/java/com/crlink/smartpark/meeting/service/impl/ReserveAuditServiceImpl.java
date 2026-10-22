package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.meeting.common.Constant;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.model.dto.ReserveAuditReqDTO;
import com.crlink.smartpark.meeting.controller.request.query.ReserveAuditQuery;
import com.crlink.smartpark.meeting.dto.ReserveAuditDTO;
import com.crlink.smartpark.meeting.entity.MeetingReserve;
import com.crlink.smartpark.meeting.entity.MeetingReview;
import com.crlink.smartpark.meeting.entity.ReserveAudit;
import com.crlink.smartpark.meeting.mapper.ReserveAuditMapper;
import com.crlink.smartpark.meeting.model.dto.*;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.meeting.service.MeetingReserveService;
import com.crlink.smartpark.meeting.service.MeetingReviewService;
import com.crlink.smartpark.meeting.service.ReserveAuditService;
import com.crlink.smartpark.meeting.vo.MeetingReserveDetailVO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 会议室预订审核 服务实现类
 * */
@Service
public class ReserveAuditServiceImpl extends ServiceImpl<ReserveAuditMapper, ReserveAudit> implements ReserveAuditService {

    @Autowired
    private MeetingReserveService meetingReserveService;

    @Autowired
    private MeetingReviewService meetingReviewService;

    @Resource
    private EmployeeProvider employeeProvider;

    @Autowired
    private MeetingManageService meetingManageService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ReserveAudit, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ReserveAudit> lambdaQuery = this.lambdaQuery().in(ReserveAudit::getId, ids);
        if (flag) {
            lambdaQuery.select(ReserveAudit::getId, sValue);
        }
        Stream<ReserveAudit> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ReserveAudit::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ReserveAudit::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateReserveAudit(ReserveAuditReqDTO reqDTO) {
        ReserveAudit entity = MyBeanUtils.copy(reqDTO, ReserveAudit::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean modifyTime(ReserveAuditUpdateDTO reqDTO) {
        ReserveAudit reserveAudit = Optional.ofNullable(lambdaQuery().eq(ReserveAudit::getMeetingReserveId, reqDTO.getMeetingReserveId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingReserve meetingReserve = Optional.ofNullable(meetingReserveService.getById(reqDTO.getMeetingReserveId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingManage meetingManage = meetingManageService.getById(reserveAudit.getMeetingId());
        if (meetingManage.getNeedReview().equals(Constant.ONE)) {
            if (!reserveAudit.getMeetingStatus().equals(Constant.ReserveStatusEnum.RESERVE_1.type)) {
                throw new ResolinkRuntimeException("400006");
            }
        }
        int count = meetingReserveService.lambdaQuery().eq(MeetingReserve::getMeetingId, meetingReserve.getMeetingId())
                .and(condition -> condition
                        .gt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeStart()).lt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeEnd())
                        .or().gt(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeStart()).lt(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeEnd())
                        .or().ge(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeEnd()).lt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeStart())).count();
        if (count > Constant.ZERO) {
            throw new ResolinkRuntimeException("400004");
        }
        meetingReserve.setMeetingTimeStart(reqDTO.getMeetingTimeStart());
        meetingReserve.setMeetingTimeEnd(reqDTO.getMeetingTimeEnd());
        meetingReserveService.updateById(meetingReserve);
        reserveAudit.setStartTime(DateUtil.format(reqDTO.getMeetingTimeStart(), "HH:mm"));
        reserveAudit.setEndTime(DateUtil.format(reqDTO.getMeetingTimeEnd(), "HH:mm"));
        return this.updateById(reserveAudit);
    }

    @Override
    public Boolean cancelReserve(ReserveAuditCancelDTO reqDTO) {

        ReserveAudit reserveAudit = Optional.ofNullable(lambdaQuery().eq(ReserveAudit::getMeetingReserveId, reqDTO.getMeetingReserveId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingManage meetingManage = meetingManageService.getById(reserveAudit.getMeetingId());
        MeetingReserve meetingReserve = Optional.ofNullable(meetingReserveService.getById(reqDTO.getMeetingReserveId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (meetingManage.getNeedReview().equals(Constant.ONE)) {
            if (!reserveAudit.getMeetingStatus().equals(Constant.ReserveStatusEnum.RESERVE_1.type)) {
                throw new ResolinkRuntimeException("400007");
            }
        }
        if (meetingReserve.getType().equals(Constant.ReserveTypeEnum.RESERVE.type())) {
            reserveAudit.setMeetingStatus(Constant.ReserveStateEnum.RESERVE_YES.type());
        } else {
            reserveAudit.setMeetingStatus(Constant.ReserveStateEnum.CANDIDATE_YES.type());
        }
        reserveAudit.setMeetingStatus(Constant.ReserveStatusEnum.CANDIDATE_5.type);
        reserveAudit.setMeetingCancelTime(LocalDateTime.now());
        return this.updateById(reserveAudit);
    }

    @Override
    public Boolean addSummary(ReserveSummaryDTO reqDTO) {
        MeetingReserve meetingReserve = Optional.ofNullable(meetingReserveService.getById(reqDTO.getMeetingReserveId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        ReserveAudit reserveAudit = Optional.ofNullable(lambdaQuery().eq(ReserveAudit::getMeetingReserveId, reqDTO.getMeetingReserveId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (reserveAudit.getMeetingStatus().equals(Constant.ReserveStatusEnum.RESERVE_1.type())) {
            throw new ResolinkRuntimeException("400005");
        }
        meetingReserve.setMeetingSummary(reqDTO.getMeetingSummary());
        meetingReserve.setMeetingSummaryFile(reqDTO.getMeetingSummaryFile());
        return meetingReserveService.updateById(meetingReserve);
    }

    @Override
    public Boolean review(ReserveReviewDTO reqDTO) {
        ReserveAudit reserveAudit = Optional.ofNullable(lambdaQuery().eq(ReserveAudit::getMeetingReserveId, reqDTO.getMeetingReserveId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingReserve meetingReserve = Optional.ofNullable(meetingReserveService.getById(reqDTO.getMeetingReserveId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        reserveAudit.setReviewId(SystemUtil.getInstance().getEmployeeId());
        reserveAudit.setReviewStatus(Constant.ReviewStatusEnum.REVIEW_YES.type());
        reserveAudit.setReviewMsg(reqDTO.getReviewMsg());
        reserveAudit.setReviewTime(LocalDateTime.now());
        if (meetingReserve.getType().equals(Constant.ReserveTypeEnum.RESERVE.type())) {
            if (reqDTO.getMeetingStatus().equals(Constant.ReserveStatusEnum.RESERVE_2.type())) {
                reserveAudit.setMeetingStatus(Constant.ReserveStatusEnum.RESERVE_2.type());
            } else if (reqDTO.getMeetingStatus().equals(Constant.ReserveStatusEnum.RESERVE_7.type())) {
                reserveAudit.setMeetingStatus(Constant.ReserveStatusEnum.RESERVE_7.type());
                reserveAudit.setMeetingState(Constant.ReserveStateEnum.RESERVE_YES.type());
            }
        }
        //候补逻辑
//        if (meetingReserve.getType().equals(Constant.ReserveTypeEnum.CANDIDATE.type())) {
//            if (reqDTO.getReviewStatus().equals(Constant.ReviewStatusEnum.REVIEW_YES.type())) {
//                reserveAudit.setMeetingStatus(Constant.ReserveStateEnum.RESERVE_OVER.type());
//            }
//            if (reqDTO.getReviewStatus().equals(Constant.ReviewStatusEnum.REVIEW_NO.type())) {
//                reserveAudit.setMeetingStatus(Constant.ReserveStateEnum.CANDIDATE_YES.type());
//            }
//        }
        return this.updateById(reserveAudit);
    }

    @Override
    @Id2objHandle
    public MeetingReserveDetailVO getReviewDetail(ReserveAuditDetailDTO query) {
        ReserveAudit reserveAudit = Optional.ofNullable(lambdaQuery().eq(ReserveAudit::getMeetingReserveId, query.getMeetingReserveId()).one()).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingReserve meetingReserve = Optional.ofNullable(meetingReserveService.getById(query.getMeetingReserveId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingManage meetingManage = Optional.ofNullable(meetingManageService.getById(meetingReserve.getMeetingId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingReserveDetailVO meetingReserveDetailVO = new MeetingReserveDetailVO();
        BeanUtils.copyProperties(meetingReserve, meetingReserveDetailVO);
        meetingReserveDetailVO.setApplyTime(reserveAudit.getApplyTime());
        meetingReserveDetailVO.setReviewTime(reserveAudit.getReviewTime());
        meetingReserveDetailVO.setReviewMsg(reserveAudit.getReviewMsg());
        meetingReserveDetailVO.setMeetingCancelTime(reserveAudit.getMeetingCancelTime());
        meetingReserveDetailVO.setMeetingStatus(reserveAudit.getMeetingStatus());
        meetingReserveDetailVO.setNeedReview(meetingManage.getNeedReview());
        if (query.getType().equals(Constant.ZERO)) {
            meetingReserveDetailVO.setMeetingSummary("");
            meetingReserveDetailVO.setMeetingSummaryFile("");
        }
        //查询与会人list姓名
        if (StringUtils.isNotBlank(meetingReserve.getAttendees())) {
            String[] employeeIds = meetingReserve.getAttendees().split(",");
            Map<String, String> map = employeeProvider.findEmployeeNamesByIds(employeeIds);
            meetingReserveDetailVO.setAttendeesName(StringUtils.join(map.values().toArray(), ","));
        }
        //审核人姓名
        if (StringUtils.isNotBlank(reserveAudit.getReviewId())) {
            String[] employeeIds = new String[]{reserveAudit.getReviewId()};
            Map<String, String> map = employeeProvider.findEmployeeNamesByIds(employeeIds);
            meetingReserveDetailVO.setReviewName(map.get(reserveAudit.getReviewId()));
        }
        return meetingReserveDetailVO;

    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<ReserveAuditDTO> listReserveAuditPage(ReserveAuditQuery query) {
        query.startPage();
        query.setEmployeeId(SystemUtil.getInstance().getEmployeeId());
        List<ReserveAuditDTO> result = baseMapper.listPage(query);
        return query.createPageInfo(result);
    }

    @Override
    public PageInfo<ReserveAuditDTO> myAuditPage(ReserveAuditQuery query) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        Set<String> meetingSet = meetingReviewService.lambdaQuery().eq(MeetingReview::getEmployeeId, employeeId).list().parallelStream().map(MeetingReview::getMeetingId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(meetingSet)) {
            return new PageInfo<>();
        }
        query.startPage();
        query.setMeetingSet(meetingSet);
        List<ReserveAuditDTO> result = baseMapper.listAuditPage(query);
        return query.createPageInfo(result);
    }

    @Override
    public int autoCancelReserve() {
        return baseMapper.autoCancelReserve();
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ReserveAudit> getConditionWrapper(ReserveAuditQuery query) {
        LambdaQueryWrapper<ReserveAudit> queryWrapper = new LambdaQueryWrapper<ReserveAudit>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ReserveAuditDTO> entitiesToDTOs(List<ReserveAudit> entities) {
        return MyBeanUtils.copyList(entities, ReserveAuditDTO::new, (s, t) -> {

        });
    }
}
