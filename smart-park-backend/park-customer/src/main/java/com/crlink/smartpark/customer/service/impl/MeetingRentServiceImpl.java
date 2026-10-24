package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.controller.request.query.MeetingRentStatusDTO;
import com.crlink.smartpark.customer.dto.MeetingGoodsDTO;
import com.crlink.smartpark.customer.dto.MeetingPermissionDTO;
import com.crlink.smartpark.customer.dto.MeetingRentDTO;
import com.crlink.smartpark.customer.dto.MeetingScheduleTimeDTO;
import com.crlink.smartpark.customer.entity.*;
import com.crlink.smartpark.customer.mapper.MeetingRentMapper;
import com.crlink.smartpark.customer.model.MeetingRentReqDTO;
import com.crlink.smartpark.customer.model.query.MeetingRentQuery;
import com.crlink.smartpark.customer.model.query.MeetingRentWxQuery;
import com.crlink.smartpark.customer.model.vo.*;
import com.crlink.smartpark.customer.service.*;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 会议室租赁管理 服务实现类
 * */
@Service
@Slf4j
public class MeetingRentServiceImpl extends ServiceImpl<MeetingRentMapper, MeetingRent> implements MeetingRentService {

    @Autowired
    private MeetingScheduleTimeService meetingScheduleTimeService;
    @Autowired
    private MeetingPermissionService meetingPermissionService;
    @Autowired
    private MeetingGoodsService meetingGoodsService;
    @Autowired
    private MeetingReserveTimeService reserveTimeService;
    @Autowired
    private MeetingReserveService meetingReserveService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingRent, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingRent> lambdaQuery = this.lambdaQuery().in(MeetingRent::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingRent::getId, sValue);
        }
        Stream<MeetingRent> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingRent::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingRent::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingRent(MeetingRentReqDTO reqDTO) {
        MeetingRent meetingRent = this.lambdaQuery().eq(MeetingRent::getParkOrgId, reqDTO.getParkOrgId())
                .eq(MeetingRent::getMeetingName, reqDTO.getMeetingName()).one();
        MeetingRent entity = MyBeanUtils.copy(reqDTO, MeetingRent::new);
        if (StringUtils.isBlank(reqDTO.getId())) {
            if (meetingRent != null) {
                throw new ResolinkRuntimeException("50013");
            }
            entity.setStatus(DictUtil.getValue(DictConstant.YesOrNo.YES));
            EntityUserUtil.setAllDynamic(entity);
        } else {
            MeetingRent meeting =
                    Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            if (!meeting.getMeetingName().equals(reqDTO.getMeetingName())) {
                int count = this.lambdaQuery().eq(MeetingRent::getParkOrgId, reqDTO.getParkOrgId())
                        .eq(MeetingRent::getMeetingName, reqDTO.getMeetingName()).count();
                if (count > Constant.ZERO) {
                    throw new ResolinkRuntimeException("50013");
                }
            }
            EntityUserUtil.setUpdateBy(entity);
            LambdaQueryWrapper<MeetingPermission> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MeetingPermission::getMeetingId, reqDTO.getId());
            meetingPermissionService.remove(wrapper);

            LambdaQueryWrapper<MeetingScheduleTime> timeWrapper = new LambdaQueryWrapper<>();
            timeWrapper.eq(MeetingScheduleTime::getMeetingId, reqDTO.getId());
            meetingScheduleTimeService.remove(timeWrapper);
        }
        this.saveOrUpdate(entity);
        List<MeetingPermission> permissionComList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reqDTO.getCompanyIds())) {
            reqDTO.getCompanyIds().forEach(e -> {
                MeetingPermission meetingPermission = new MeetingPermission();
                meetingPermission.setCompanyId(e.getCompanyId());
                meetingPermission.setMeetingId(entity.getId());
                meetingPermission.setRelationType(e.getRelationType());
                permissionComList.add(meetingPermission);
            });
        }
        meetingPermissionService.saveBatch(permissionComList);

        List<MeetingPermission> permissionEmpList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reqDTO.getEmployeeIds())) {
            reqDTO.getEmployeeIds().forEach(e -> {
                MeetingPermission meetingPermission = new MeetingPermission();
                meetingPermission.setEmployeeId(e.getEmployeeId());
                meetingPermission.setMeetingId(entity.getId());
                meetingPermission.setRelationType(e.getRelationType());
                permissionEmpList.add(meetingPermission);
            });
        }
        meetingPermissionService.saveBatch(permissionEmpList);
        List<MeetingScheduleTime> scheduleTimes =
                MyBeanUtils.copyList(reqDTO.getTimeReqDTOList(), MeetingScheduleTime::new, (s, t) -> {
                    t.setMeetingId(entity.getId());
                });
        return meetingScheduleTimeService.saveBatch(scheduleTimes);
    }

    @Override
    public Boolean removeByMeetingId(String id) {
        MeetingRent entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (entity.getStatus().equals(DictUtil.getValue(DictConstant.YesOrNo.YES))) {
            throw new ResolinkRuntimeException("50008");
        }
        return this.removeById(id);
    }

    @Override
    public Boolean updateStatus(MeetingRentStatusDTO reqDTO) {
        MeetingRent entity =
                Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        entity.setStatus(reqDTO.getStatus());
        return this.updateById(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeetingRentDetailVO getMeetingRentById(String id) {
        MeetingRentDetailVO meetingRentDetailVO = new MeetingRentDetailVO();
        MeetingRent entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingRentDTO meetingRentDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        List<DictVO> goodsList = null;
        if (StringUtils.isNotBlank(entity.getGoodsId())) {
            Set<String> ids = new HashSet<>(Arrays.asList(entity.getGoodsId().split(",")));
            goodsList = meetingGoodsService.lambdaQuery().in(MeetingGoods::getId, ids).list()
                    .parallelStream().map(e -> new DictVO(e.getGoodsName(), e.getId())).collect(Collectors.toList());
        }
        List<MeetingPermission> permissionComList =
                meetingPermissionService.lambdaQuery().eq(MeetingPermission::getMeetingId, id)
                        .eq(MeetingPermission::getRelationType, Constant.MeetingPermissionType.COMPANY.state).list();
        List<MeetingPermissionDTO> companyList = MyBeanUtils.copyList(permissionComList, MeetingPermissionDTO::new);

        List<MeetingPermission> permissionEmpList =
                meetingPermissionService.lambdaQuery().eq(MeetingPermission::getMeetingId, id)
                        .eq(MeetingPermission::getRelationType, Constant.MeetingPermissionType.EMPLOYEE.state).list();
        List<MeetingPermissionDTO> employeeList = MyBeanUtils.copyList(permissionEmpList, MeetingPermissionDTO::new);

        List<MeetingScheduleTime> scheduleTimes =
                meetingScheduleTimeService.lambdaQuery().eq(MeetingScheduleTime::getMeetingId, id).list();
        List<MeetingScheduleTimeDTO> timeList = MyBeanUtils.copyList(scheduleTimes, MeetingScheduleTimeDTO::new);
        meetingRentDetailVO.setMeetingRentDTO(meetingRentDTO);
        meetingRentDetailVO.setGoodsList(goodsList);
        meetingRentDetailVO.setCompanyList(companyList);
        meetingRentDetailVO.setEmployeeList(employeeList);
        meetingRentDetailVO.setTimeList(timeList);
        return meetingRentDetailVO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeetingRentDTO> listMeetingRentPage(MeetingRentQuery query) {
        query.startPage();
        List<MeetingRent> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingRentDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    @Id2objHandle
    public PageInfo<MeetingRentDTO> listWxMeetingRentPage(BaseQuery query) {
        String curEmployeeId = SystemUtil.getInstance().getEmployeeId();
        if (StringUtils.isBlank(curEmployeeId)){
            throw new ResolinkRuntimeException("400001");
        }
        String curParkOrgId = SystemUtil.getInstance().getEmployeeParkOrgId();
        MyPageInfoUtils.isEmptyPage(curParkOrgId);
        Set<String> companyIds = new HashSet<>();
        Set<String> employeeIds = new HashSet<>();

        companyIds.add(SystemUtil.getInstance().getEmployeeCompanyId());
        employeeIds.add(SystemUtil.getInstance().getEmployeeId());

        if (CollUtil.isEmpty(companyIds) || CollUtil.isEmpty(employeeIds)) {
            // 如果这两个参数为空，直接无权限
            throw new ResolinkRuntimeException("400001");
        }
        // DO 列表取值运营管理后台“会议室租赁-会议室管理”列表中当前园区“已启用”状态、且当前用户具备使用权限的会议室，按会议室名称升序排列；
        // 1、根据 员工id,对应企业ID，查询可以租赁的会议室id
        Set<String> permissionList = meetingPermissionService.lambdaQuery()
                .in(MeetingPermission::getEmployeeId, employeeIds).or().in(MeetingPermission::getCompanyId, companyIds)
                .list().parallelStream().map(MeetingPermission::getMeetingId).collect(Collectors.toSet());
        // 2、根据 园区ID、会议室ID、会议室状态、分页查询
        query.startPage();
        LambdaQueryWrapper<MeetingRent> queryWrapper = new LambdaQueryWrapper<MeetingRent>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        queryWrapper.eq(MeetingRent::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES));

        queryWrapper.eq(MeetingRent::getParkOrgId, curParkOrgId);
//                .in(MeetingRent::getParkOrgId, parkOrgIds);
        if (CollUtil.isNotEmpty(permissionList)) {
            queryWrapper.and(wq -> wq
                    .in(MeetingRent::getId, permissionList)
                    .or()
                    .eq(MeetingRent::getUsePermission, DictUtil.getValue(DictConstant.YesOrNo.NO)));
        } else {
            queryWrapper.eq(MeetingRent::getUsePermission, DictUtil.getValue(DictConstant.YesOrNo.NO));
        }

        queryWrapper.orderByAsc(MeetingRent::getMeetingName);
        List<MeetingRent> list = this.list(queryWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingRentDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    @Id2objHandle
    public MeetingRentDetailWxVO getWxMeetingRentById(String id) {
        MeetingRentDetailWxVO meetingRentDetailVO = new MeetingRentDetailWxVO();
        MeetingRent entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingRentDTO meetingRentDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        List<MeetingGoods> goodsList = null;
        List<MeetingGoodsDTO> meetingGoodsDTOList = new ArrayList<>();
        if (StringUtils.isNotBlank(entity.getGoodsId())) {
            Set<String> ids = new HashSet<>(Arrays.asList(entity.getGoodsId().split(",")));
            goodsList = meetingGoodsService.lambdaQuery().in(MeetingGoods::getId, ids).orderByAsc(MeetingGoods::getGoodsName).list();
            meetingGoodsDTOList = MyBeanUtils.copyList(goodsList, MeetingGoodsDTO::new);
        }
        List<MeetingScheduleTime> scheduleTimes =
                meetingScheduleTimeService.lambdaQuery().eq(MeetingScheduleTime::getMeetingId, id).list();
        List<MeetingScheduleTimeDTO> timeList = MyBeanUtils.copyList(scheduleTimes, MeetingScheduleTimeDTO::new);
        meetingRentDetailVO.setMeetingRentDTO(meetingRentDTO);
        meetingRentDetailVO.setGoodsList(meetingGoodsDTOList);
        meetingRentDetailVO.setTimeList(timeList);
        return meetingRentDetailVO;
    }

    @Override
    @Id2objHandle
    public MeetingRentReserveVO getWxMeetingRentReserve(MeetingRentWxQuery query) {

        String curEmployeeId = SystemUtil.getInstance().getEmployeeId();
        MyPageInfoUtils.isEmptyPage(curEmployeeId);

        MeetingRentReserveVO meetingRentReserveVO = new MeetingRentReserveVO();
        // 写入会议室基础信息
        meetingRentReserveVO.setMeetingId(query.getMeetingId());
        MeetingRent entity = Optional.ofNullable(getById(query.getMeetingId()))
                .orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingRentDTO meetingRentDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        meetingRentReserveVO.setMeetingRentDTO(meetingRentDTO);

        // 2、写入可预订时段集合
        List<MeetingScheduleTime> scheduleTimes =
                meetingScheduleTimeService.lambdaQuery().eq(MeetingScheduleTime::getMeetingId, query.getMeetingId())
                        .orderByAsc(MeetingScheduleTime::getTimeType).list();
        List<MeetingScheduleTimeDTO> timeList = MyBeanUtils.copyList(scheduleTimes, MeetingScheduleTimeDTO::new);
        meetingRentReserveVO.setTimeList(timeList);

        LocalDate dateStart = LocalDate.parse(query.getMonth() + "-01");
        LocalDate dateEnd = dateStart.plusMonths(Constants.ONE);

        String msgSuccess = "已预定";
        String msgError = "不可预定";
        String msgMoney = entity.getCharge() == 1 ? "￥" + entity.getRentPrice() : "免费";

        // 3、拉取预定记录
        List<MeetingReserveTime> reserveList =
                reserveTimeService.lambdaQuery().eq(MeetingReserveTime::getMeetingId, query.getMeetingId())
                        .eq(MeetingReserveTime::getMeetingState, DictUtil.getValue(DictConstant.YesOrNo.NO))
                        .between(MeetingReserveTime::getNowDate, dateStart, dateEnd)
                        .orderByAsc(MeetingReserveTime::getNowDate, MeetingReserveTime::getTimeType).list();
        Map<LocalDate, Map<Integer, MeetingReserveTime>> reserveMap =
                reserveList.stream().collect(Collectors.groupingBy(MeetingReserveTime::getNowDate,
                        Collectors.toMap(MeetingReserveTime::getTimeType, Function.identity())));

        // 4、详情处理
        Map<LocalDate, List<ReserveDetailVO>> reserveDetailMap = new HashMap<>();
        List<String> dateRange = new ArrayList<>();

        LocalDateTime newDateTime = LocalDateTime.now();
        for (; dateEnd.toEpochDay() - dateStart.toEpochDay() >= Constants.ONE;
             dateStart = dateStart.plusDays(Constants.ONE)) {

            List<ReserveDetailVO> reserveDetailVOList = new ArrayList<>();

            LocalDate finalDateStart = dateStart;
            timeList.forEach(time -> {
                ReserveDetailVO detailVO = new ReserveDetailVO();
                detailVO.setTimeType(time.getTimeType());
                // 1、进行时间校验
                LocalDateTime detailTime = LocalDateTime.of(finalDateStart, LocalTime.parse(time.getEndTime() + ":00"));
                if (detailTime.compareTo(newDateTime) < 0) {
                    detailVO.setMsg(msgError);
                    detailVO.setNoPermission(Constants.TWO);
                } else {
                    detailVO.setMsg(msgMoney);
                    detailVO.setNoPermission(Constants.ONE);
                }

                // 2、筛查对应预定数据
                if (reserveMap.containsKey(finalDateStart)) {
                    Map<Integer, MeetingReserveTime> timeTypeMap = reserveMap.get(finalDateStart);
                    if (timeTypeMap.containsKey(time.getTimeType())) {
                        // 如果预定人是本人，显示已预定
                        if (timeTypeMap.get(time.getTimeType()).getEmployeeId().equals(curEmployeeId)) {
                            detailVO.setMsg(msgSuccess);
                            detailVO.setNoPermission(Constants.THREE);
                        } else {
                            detailVO.setMsg(msgError);
                            detailVO.setNoPermission(Constants.TWO);
                        }
                    }
                }
                reserveDetailVOList.add(detailVO);
            });
            dateRange.add(dateStart.toString());
            reserveDetailMap.put(finalDateStart, reserveDetailVOList);
        }
        meetingRentReserveVO.setReserveDetailVOS(reserveDetailMap);
//        log.info("dateRange{}", dateRange);

        return meetingRentReserveVO;
    }

    /**
     * 获取会议室租赁占用率（运营大屏）
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public MeetingOccupationRatioVO getMeetingOccupationRatio(String parkOrgId) {
        MeetingOccupationRatioVO ratioVO = new MeetingOccupationRatioVO();
        int meetingCount = this.count(Wrappers.<MeetingRent>lambdaQuery().eq(MeetingRent::getParkOrgId, parkOrgId).eq(MeetingRent::getStatus, 1));
        ratioVO.setMeetingCount(meetingCount);
        LocalDate localDate = LocalDate.now();
        LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        LambdaQueryWrapper<MeetingReserve> queryWrapper = new LambdaQueryWrapper<MeetingReserve>();
        queryWrapper.eq(MeetingReserve::getParkOrgId, parkOrgId);
        queryWrapper.between(MeetingReserve::getApplicantTime, startDateTime, endDateTime);
        queryWrapper.eq(MeetingReserve::getReviewStatus, 2);//审核通过
        int reserveCount = meetingReserveService.count(queryWrapper);
        ratioVO.setReserveCount(reserveCount);
        return ratioVO;
    }

//    public static void main(String[] args) {
//        MeetingRentWxQuery wxQuery = new MeetingRentWxQuery();
//        wxQuery.setMonth("2022-05");
//        wxQuery.setMeetingId("1503228323948400642");
//        MeetingRentServiceImpl xx = new MeetingRentServiceImpl();
//
//        log.info("date:{}", xx.getWxMeetingRentReserve(wxQuery));
//    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingRent> getConditionWrapper(MeetingRentQuery query) {
        LambdaQueryWrapper<MeetingRent> queryWrapper = new LambdaQueryWrapper<MeetingRent>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        queryWrapper.like(StringUtils.isNotBlank(query.getMeetingName()), MeetingRent::getMeetingName,
                query.getMeetingName());
        queryWrapper.eq(query.getStatus() != null, MeetingRent::getStatus, query.getStatus());
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeetingRent::getParkOrgId, query.getParkOrgId())
                .in(MeetingRent::getParkOrgId, parkOrgIds).orderByDesc(MeetingRent::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<MeetingRentDTO> entitiesToDTOs(List<MeetingRent> entities) {
        return MyBeanUtils.copyList(entities, MeetingRentDTO::new, (s, t) -> {

        });
    }

}
