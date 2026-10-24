package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.meeting.common.Constant;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.entity.MeetingPermission;
import com.crlink.smartpark.meeting.entity.MeetingReserve;
import com.crlink.smartpark.meeting.entity.ReserveAudit;
import com.crlink.smartpark.meeting.model.dto.MeetingReserveAddDTO;
import com.crlink.smartpark.meeting.dto.MeetingReserveDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingReserveGetDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingTimeGetDTO;
import com.crlink.smartpark.meeting.model.query.MeetingReserveQuery;
import com.crlink.smartpark.meeting.model.vo.MeetingByBuildingVO;
import com.crlink.smartpark.meeting.model.vo.MeetingReserveVO;
import com.crlink.smartpark.meeting.model.vo.ReserveDetailVO;
import com.crlink.smartpark.meeting.model.vo.ReserveTimeVO;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.meeting.service.MeetingPermissionService;
import com.crlink.smartpark.meeting.service.MeetingReserveService;
import com.crlink.smartpark.meeting.mapper.MeetingReserveMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.meeting.service.ReserveAuditService;
import com.crlink.smartpark.system.vo.DictVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 会议室预订表 服务实现类
 * */
@Service
public class MeetingReserveServiceImpl extends ServiceImpl<MeetingReserveMapper, MeetingReserve> implements MeetingReserveService {


    @Autowired
    private MeetingManageService meetingManageService;

    @Autowired
    private MeetingPermissionService meetingPermissionService;

    @Autowired
    private ReserveAuditService reserveAuditService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReserve, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingReserve> lambdaQuery = this.lambdaQuery().in(MeetingReserve::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingReserve::getId, sValue);
        }
        Stream<MeetingReserve> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingReserve::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingReserve::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Object saveOrUpdateMeetingReserve(MeetingReserveAddDTO reqDTO) {
        if (!meetingPermissionService.noPermission(reqDTO.getMeetingId(), SystemUtil.getInstance().getEmployeeId(), SystemUtil.getInstance().getEmployeeCompanyId())) {
            throw new ResolinkRuntimeException("400001");
        }
//        int count = this.lambdaQuery().eq(MeetingReserve::getMeetingId, reqDTO.getMeetingId())
//                .and(condition -> condition
//                        .gt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeStart()).lt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeEnd())
//                        .or().gt(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeStart()).lt(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeEnd())
//                        .or().ge(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeEnd()).lt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeStart())).count();
//        if (count > Constant.ZERO) {
//            throw new ResolinkRuntimeException("400004");
//        }
        MeetingReserve entity = MyBeanUtils.copy(reqDTO, MeetingReserve::new);
        EntityUserUtil.setAllDynamic(entity);
        entity.setEmployeeId(SystemUtil.getInstance().getEmployeeId());
        this.saveOrUpdate(entity);
        ReserveAudit reserveAudit = MyBeanUtils.copy(entity, ReserveAudit::new);
        reserveAudit.setMeetingReserveId(entity.getId());
        reserveAudit.setNowDate(entity.getMeetingTimeStart().toLocalDate());
        reserveAudit.setStartTime(DateUtil.format(entity.getMeetingTimeStart(), "HH:mm"));
        reserveAudit.setEndTime(DateUtil.format(entity.getMeetingTimeEnd(), "HH:mm"));
        reserveAudit.setApplyTime(LocalDateTime.now());
        if (reqDTO.getType().equals(Constant.ReserveTypeEnum.RESERVE.type())) {
            reserveAudit.setMeetingState(Constant.ReserveStateEnum.RESERVE_OVER.type());
            MeetingManage meetingManage = meetingManageService.getById(reqDTO.getMeetingId());
            if (meetingManage.getNeedReview().equals(DictUtil.getValue(DictConstant.YesOrNo.YES))) {
                reserveAudit.setMeetingStatus(Constant.ReserveStatusEnum.RESERVE_1.type());
                reserveAudit.setReviewStatus(Constant.ReviewStatusEnum.RESERVE_NOT.type());
            } else {
                reserveAudit.setMeetingStatus(Constant.ReserveStatusEnum.RESERVE_2.type());
            }
        }
        reserveAuditService.saveOrUpdate(reserveAudit);
        return entity;
    }

    @Override
    public List<MeetingByBuildingVO> getMeeting(MeetingReserveGetDTO reqDTO) {
        Set<String> setMeetings = this.lambdaQuery().and(condition -> condition
                .gt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeStart()).lt(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeEnd())
                .or().gt(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeStart()).lt(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeEnd())
                .or().ge(MeetingReserve::getMeetingTimeEnd, reqDTO.getMeetingTimeEnd()).le(MeetingReserve::getMeetingTimeStart, reqDTO.getMeetingTimeStart()))
                .list().parallelStream().map(MeetingReserve::getMeetingId).collect(Collectors.toSet());
        List<MeetingManage> manageList = meetingManageService.lambdaQuery()
                .notIn(CollectionUtil.isNotEmpty(setMeetings), MeetingManage::getId, setMeetings)
                .select(MeetingManage::getId, MeetingManage::getMeetingName, MeetingManage::getCapacity, MeetingManage::getMeetingEquipment)
                .like(StringUtils.hasText(reqDTO.getMeetingName()), MeetingManage::getMeetingName, reqDTO.getMeetingName())
                .eq(StringUtils.hasText(reqDTO.getParkOrgId()), MeetingManage::getParkOrgId, reqDTO.getParkOrgId())
                .eq(StringUtils.hasText(reqDTO.getBuildingId()), MeetingManage::getBuildingId, reqDTO.getBuildingId())
                .orderByAsc(MeetingManage::getMeetingName).list();
        if (CollectionUtil.isNotEmpty(manageList)) {
            List<MeetingByBuildingVO> list = MyBeanUtils.copyList(manageList, MeetingByBuildingVO::new);
            return list;
        } else {
            return null;
        }

    }

    @Override
    public List<ReserveTimeVO> getMeetingTime(MeetingTimeGetDTO reqDTO) {
        List<ReserveTimeVO> timeVOList = reserveAuditService.lambdaQuery().eq(ReserveAudit::getMeetingId, reqDTO.getMeetingId())
                .notIn(ReserveAudit::getMeetingStatus, Constant.ReserveStatusEnum.CANDIDATE_5.type(), Constant.ReserveStatusEnum.CANDIDATE_6.type(), Constant.ReserveStatusEnum.RESERVE_7.type())
                .eq(ReserveAudit::getNowDate, reqDTO.getNowDate())
                .orderByAsc(ReserveAudit::getStartTime).list().parallelStream()
                .map(t -> new ReserveTimeVO(t.getStartTime(), t.getEndTime())).collect(Collectors.toList());
        return timeVOList;
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeetingReserve getMeetingReserveById(String id) {
        MeetingReserve entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingManage meetingManage = meetingManageService.getById(entity.getMeetingId());
        entity.setMeetingName(meetingManage.getMeetingName());
        return entity;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public List<MeetingReserveVO> listMeetingReserve(MeetingReserveQuery query) {
        //当前园区、楼栋、楼层下会议室列表
        List<MeetingManage> manageList = meetingManageService.lambdaQuery().eq(MeetingManage::getParkOrgId, query.getParkOrgId())
                .eq(MeetingManage::getBuildingId, query.getBuildingId())
                .eq(MeetingManage::getFloorId, query.getFloorId())
                .like(StringUtils.hasText(query.getMeetingName()), MeetingManage::getMeetingName, query.getMeetingName())
                .orderByAsc(MeetingManage::getMeetingName).list();
        if (CollectionUtil.isEmpty(manageList)) {
            return null;
        }
        Set<String> meetingManageIds = manageList.stream().map(MeetingManage::getId).collect(Collectors.toSet());
        //预定记录
        Map<String, List<ReserveAudit>> manageReserveMap = reserveAuditService.lambdaQuery().in(ReserveAudit::getMeetingId, meetingManageIds)
                .notIn(ReserveAudit::getMeetingStatus, Constant.ReserveStatusEnum.CANDIDATE_5.type(), Constant.ReserveStatusEnum.CANDIDATE_6.type(), Constant.ReserveStatusEnum.RESERVE_7.type())
                .eq(ReserveAudit::getNowDate, query.getNowDate())
                .orderByAsc(ReserveAudit::getStartTime).list()
                .stream().collect(Collectors.groupingBy(ReserveAudit::getMeetingId));

        //会议室权限列表
        Set<String> permissions = meetingPermissionService.getPermission(meetingManageIds, SystemUtil.getInstance().getEmployeeId(), SystemUtil.getInstance().getEmployeeCompanyId());
        //会议室启用禁用状态
        Map<String, Object> statusMap = meetingManageService.lambdaQuery().select(MeetingManage::getId, MeetingManage::getStatus).in(MeetingManage::getId, meetingManageIds).list().parallelStream()
                .collect(Collectors.toMap(MeetingManage::getId, MeetingManage::getStatus));
        List<MeetingReserveVO> meetingReserveVOS = MyBeanUtils.copyList(manageList, MeetingReserveVO::new, (s, t) -> {
            //会议室状态
            if (statusMap.get(s.getId()).equals(DictUtil.getValue(DictConstant.YesOrNo.NO))) {
                t.setNoPermission(DictUtil.getValue(DictConstant.YesOrNo.YES));
            } else {
                //会议室关联权限
                if (CollectionUtil.isEmpty(permissions)) {
                    t.setNoPermission(DictUtil.getValue(DictConstant.YesOrNo.YES));
                } else {
                    t.setNoPermission(DictUtil.getValue(
                            permissions.contains(s.getId()) ? DictConstant.YesOrNo.NO : DictConstant.YesOrNo.YES));
                }
            }

            if (CollectionUtil.isEmpty(manageReserveMap) || CollectionUtil.isEmpty(manageReserveMap.get(s.getId()))) {
                List<ReserveDetailVO> detailVOS = new ArrayList<>();
                ReserveDetailVO reserveDetailVO = new ReserveDetailVO();
                reserveDetailVO.setStartTime(s.getOpenTimeStart().format(DateTimeFormatter.ofPattern("HH:mm")));
                reserveDetailVO.setEndTime(s.getOpenTimeEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
                if (t.getNoPermission().equals(Constant.ReserveStateEnum.RESERVE_NO.type())) {
                    reserveDetailVO.setMeetingState(Constant.ReserveStateEnum.RESERVE_NO.type());
                } else {
                    reserveDetailVO.setMeetingState(Constant.ReserveStateEnum.RESERVE_YES.type());
                }
                detailVOS.add(reserveDetailVO);
                t.setReserveDetailVOS(detailVOS);
            } else {
                t.setReserveDetailVOS(MyBeanUtils.copyList(manageReserveMap.get(s.getId()), ReserveDetailVO::new));
                List<ReserveDetailVO> detailVOS = new ArrayList<>();
                //先补头数据
                ReserveDetailVO reDetail = t.getReserveDetailVOS().get(Constant.ZERO);
                if (LocalTime.parse(reDetail.getStartTime()).compareTo(s.getOpenTimeStart()) == Constant.ZERO) {
                    detailVOS.add(reDetail);
                } else {
                    ReserveDetailVO reserveDetailVO1 = new ReserveDetailVO();
                    if (t.getNoPermission().equals(Constant.ReserveStateEnum.RESERVE_NO.type())) {
                        reserveDetailVO1.setMeetingState(Constant.ReserveStateEnum.RESERVE_NO.type());
                    } else {
                        reserveDetailVO1.setMeetingState(Constant.ReserveStateEnum.RESERVE_YES.type());
                    }
                    reserveDetailVO1.setStartTime(s.getOpenTimeStart().format(DateTimeFormatter.ofPattern("HH:mm")));
                    reserveDetailVO1.setEndTime(reDetail.getStartTime());
                    detailVOS.add(reserveDetailVO1);
                }

                //补中间数据
                for (int i = 0; i < t.getReserveDetailVOS().size() - 1; i++) {
                    ReserveDetailVO reDetail1 = t.getReserveDetailVOS().get(i);
                    ReserveDetailVO reDetail2 = t.getReserveDetailVOS().get(i + 1);
                    if (LocalTime.parse(reDetail1.getEndTime()).compareTo(LocalTime.parse(reDetail2.getStartTime())) == Constant.ZERO) {
                        detailVOS.add(reDetail1);
                    } else {
                        ReserveDetailVO reserveDetailVO = new ReserveDetailVO();
                        reserveDetailVO.setStartTime(reDetail1.getEndTime());
                        reserveDetailVO.setEndTime(reDetail2.getStartTime());
                        if (t.getNoPermission().equals(Constant.ReserveStateEnum.RESERVE_NO.type())) {
                            reserveDetailVO.setMeetingState(Constant.ReserveStateEnum.RESERVE_NO.type());
                        } else {
                            reserveDetailVO.setMeetingState(Constant.ReserveStateEnum.RESERVE_YES.type());
                        }
                        detailVOS.add(reDetail1);
                        detailVOS.add(reserveDetailVO);
                    }
                }

                //补尾数据
                ReserveDetailVO reDetailEnd = t.getReserveDetailVOS().get(t.getReserveDetailVOS().size() - 1);
                if (LocalTime.parse(reDetailEnd.getEndTime()).compareTo(s.getOpenTimeEnd()) == Constant.ZERO) {
                    detailVOS.add(reDetailEnd);
                } else {
                    ReserveDetailVO reserveDetailVO1 = new ReserveDetailVO();
                    if (t.getNoPermission().equals(Constant.ReserveStateEnum.RESERVE_NO.type())) {
                        reserveDetailVO1.setMeetingState(Constant.ReserveStateEnum.RESERVE_NO.type());
                    } else {
                        reserveDetailVO1.setMeetingState(Constant.ReserveStateEnum.RESERVE_YES.type());
                    }
                    reserveDetailVO1.setStartTime(reDetailEnd.getEndTime());
                    reserveDetailVO1.setEndTime(s.getOpenTimeEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
                    detailVOS.add(reDetailEnd);
                    detailVOS.add(reserveDetailVO1);
                }
                t.setReserveDetailVOS(detailVOS);
            }

        });
        return meetingReserveVOS;
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingReserve> getConditionWrapper(MeetingReserveQuery query) {
        LambdaQueryWrapper<MeetingReserve> queryWrapper = new LambdaQueryWrapper<MeetingReserve>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeetingReserveDTO> entitiesToDTOs(List<MeetingReserve> entities) {
        return MyBeanUtils.copyList(entities, MeetingReserveDTO::new, (s, t) -> {

        });
    }
}
