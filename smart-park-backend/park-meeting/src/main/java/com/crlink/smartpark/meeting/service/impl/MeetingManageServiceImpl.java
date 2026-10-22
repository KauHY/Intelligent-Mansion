package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.feign.DeviceInfoProvider;
import com.crlink.smartpark.meeting.common.Constant;
import com.crlink.smartpark.meeting.controller.request.query.MeetingManageQuery;
import com.crlink.smartpark.meeting.dto.MeetingManageDTO;
import com.crlink.smartpark.meeting.dto.MeetingPermissionDTO;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.entity.MeetingPermission;
import com.crlink.smartpark.meeting.entity.MeetingReview;
import com.crlink.smartpark.meeting.entity.ReserveAudit;
import com.crlink.smartpark.meeting.mapper.MeetingManageMapper;
import com.crlink.smartpark.meeting.model.dto.MeetingManageReqDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingManageStatusDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingOverviewDTO;
import com.crlink.smartpark.meeting.model.query.MeetingManageTreeQuery;
import com.crlink.smartpark.meeting.model.vo.ReserveTimeVO;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.meeting.service.MeetingPermissionService;
import com.crlink.smartpark.meeting.service.MeetingReviewService;
import com.crlink.smartpark.meeting.service.ReserveAuditService;
import com.crlink.smartpark.meeting.vo.MeetingManageVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_TIME_MINUTES_FORMAT;
import static com.crlink.smartpark.meeting.common.Constant.MeetingUsedState.*;
import static com.crlink.smartpark.meeting.common.Constant.ReserveStatusEnum.RESERVE_2;

/**
 * 会议室管理 服务实现类
 * */
@Slf4j
@Service
public class MeetingManageServiceImpl extends ServiceImpl<MeetingManageMapper, MeetingManage>
        implements MeetingManageService {

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private DeviceInfoProvider deviceInfoProvider;

    @Autowired
    private MeetingPermissionService permissionService;

    @Autowired
    private MeetingReviewService reviewService;

    @Autowired
    private ReserveAuditService reserveAuditService;

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<MeetingManage, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(MeetingManage::getId, sValue).in(MeetingManage::getId, ids).list()
                .parallelStream().collect(Collectors.toMap(MeetingManage::getId, sValue));
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateMeetingManage(MeetingManageReqDTO reqDTO) {
        checkScreenDevice(reqDTO);
        // 单独参数校验
        if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(reqDTO.getNeedReview())) {
            AssertUtil.requireTrue(CollUtil.isNotEmpty(reqDTO.getReviewerList()), "400002");
        }
        if (CollectionUtil.isEmpty(reqDTO.getCompanyIds()) && CollectionUtil.isEmpty(reqDTO.getEmployeeIds())) {
            throw new ResolinkRuntimeException("10003");
        }
        MeetingManage entity = MyBeanUtils.copy(reqDTO, MeetingManage::new);
        EntityUserUtil.setAllDynamic(entity);
        // 时间格式转化
        this.saveOrUpdate(entity);
        String meetingId = entity.getId();
        // DO 需审核的会议室新增会议室关联审核人
        // 不管什么模式，最先移除旧权限
        reviewService.removeMeetingReviewByMeetingId(meetingId);
        if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(reqDTO.getNeedReview())) {
            AssertUtil.requireTrue(CollUtil.isNotEmpty(reqDTO.getReviewerList()), "400002");

            List<MeetingReview> result = reqDTO.getReviewerList().stream().map(reviewer -> {
                MeetingReview meetingReview = new MeetingReview();
                meetingReview.setMeetingId(meetingId);
                meetingReview.setEmployeeId(reviewer);
                return meetingReview;
            }).collect(Collectors.toList());
            reviewService.saveBatch(result);
        }

        //使用权限
        //移除旧权限
        permissionService.removePermissionByMeetingId(entity.getId());
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
        permissionService.saveBatch(permissionComList);

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
        return permissionService.saveBatch(permissionEmpList);

//        return permissionService.updatePermissionByMeetingId(new HashSet<>(reqDTO.getPermissionList()),
//            reqDTO.getRelationType(), meetingId);
        // 保存权限列表
        // permissionService.removePermissionByMeetingId(meetingId);
        // List<MeetingPermission> permissionList = convertPermissionForInsert(meetingId, reqDTO);
        // return permissionService.saveBatch(permissionList);
    }

    private void checkScreenDevice(MeetingManageReqDTO reqDTO) {
        if (StringUtils.isNotBlank(reqDTO.getScreenDeviceId())) {
            MeetingManage one = this.lambdaQuery().select(MeetingManage::getId)
                    .ne(StringUtils.isNotBlank(reqDTO.getId()), MeetingManage::getId, reqDTO.getId())
                    .eq(MeetingManage::getScreenDeviceId, reqDTO.getScreenDeviceId()).last("limit 1").one();
            AssertUtil.requireNull(one, "50017");
        }
    }

    @Override
    public Boolean changeStatusMeetingManage(MeetingManageStatusDTO reqDTO) {
        MeetingManage entity = MyBeanUtils.copy(reqDTO, MeetingManage::new);
        EntityUserUtil.setAllDynamic(entity);
        //控制会议室信息屏幕灯光
        controlScreenDeviceLight(reqDTO);
        return this.saveOrUpdate(entity);

    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeetingManageDTO getMeetingManageById(String id) {
        MeetingManage entity =
                Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingManageDTO meetingManageDTO = MyBeanUtils.copy(entity, MeetingManageDTO::new);
        // DO 加载权限列表
//        meetingManageDTO.setMeetingPermis(permissionService.findMeetingPermissionByMeetingId(id));
//        if (!meetingManageDTO.getMeetingPermis().isEmpty()) {
//            meetingManageDTO.setRelationType(meetingManageDTO.getMeetingPermis().get(0).getRelationType());
//        }
        List<MeetingPermission> permissionComList =
                permissionService.lambdaQuery().eq(MeetingPermission::getMeetingId, id)
                        .eq(MeetingPermission::getRelationType, Constant.MeetingPermissionType.COMPANY.state).list();
        List<MeetingPermissionDTO> companyList = MyBeanUtils.copyList(permissionComList, MeetingPermissionDTO::new);

        List<MeetingPermission> permissionEmpList =
                permissionService.lambdaQuery().eq(MeetingPermission::getMeetingId, id)
                        .eq(MeetingPermission::getRelationType, Constant.MeetingPermissionType.EMPLOYEE.state).list();
        List<MeetingPermissionDTO> employeeList = MyBeanUtils.copyList(permissionEmpList, MeetingPermissionDTO::new);
        meetingManageDTO.setCompanyList(companyList);
        meetingManageDTO.setEmployeeList(employeeList);
        // DO 加载审核人列表
        if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(meetingManageDTO.getNeedReview())) {
            meetingManageDTO.setReviewerList(reviewService.findMeetingReviewByMeetingId(id));
        }
        return meetingManageDTO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeetingManageVO> listMeetingManagePage(MeetingManageTreeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryChainWrapper<MeetingManage> wrapper = this.lambdaQuery().in(MeetingManage::getParkOrgId, parkOrgIds).orderByDesc(MeetingManage::getCreateTime);
        query.startPage();
        List<MeetingManage> list = wrapper.list();
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingManageVO> result = MyBeanUtils.copyList(list, MeetingManageVO::new);
        handleScreenDeviceStatus(result);
        return query.createPageInfo(result);
    }

    @Override
    public List<DictVO> listBuildingDict(String parkOrgId) {
        LambdaQueryWrapper<MeetingManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(MeetingManage::getBuildingId).eq(MeetingManage::getParkOrgId, parkOrgId);
        Set<String> buildingIds =
                this.list(queryWrapper).parallelStream().map(MeetingManage::getBuildingId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(buildingIds)) {
            return null;
        }
        Map<String, String> buildingId2NameMap =
                commonProvider.getNamesByIdsAndType(buildingIds, SimpleNode.TypeEnum.BUILDING.getCode());
        List<DictVO> buildingNames = buildingIds.parallelStream().map(t -> {
            if (StringUtils.isEmpty(buildingId2NameMap.get(t))) {
                return null;
            }
            SimpleNode simpleNode =
                    new SimpleNode(t, buildingId2NameMap.get(t), SimpleNode.TypeEnum.BUILDING.getCode());
            setBuildingExtraInfo(simpleNode, t);
            return simpleNode;
        }).collect(Collectors.toList());
        return buildingNames;
    }

    @Override
    public MeetingOverviewDTO getMeetingOverview(String id, Integer idType) {
        //会议室id
        String meetingId = null;
        String deviceId = null;
        if (Objects.equals(idType, 1)) {
            meetingId = id;
        } else {
            deviceId = id;
            String screenDeviceId = deviceInfoProvider.getIdByDeviceIds(Sets.newHashSet(id), DictConstant.DeviceType.MEETING_SCREEN.type).get(id);
            MeetingManage one = this.lambdaQuery().select(MeetingManage::getId).eq(MeetingManage::getScreenDeviceId, screenDeviceId).one();
            meetingId = Optional.ofNullable(one).map(MeetingManage::getId).orElseThrow(() -> new ResolinkRuntimeException("50018"));
        }
        LocalTime localTime = LocalTime.now();
        Integer status = this.id2ValueMap(Sets.newHashSet(meetingId), MeetingManage::getStatus).get(meetingId);
        MeetingOverviewDTO result = new MeetingOverviewDTO();
        String meetingName = this.id2ValueMap(Sets.newHashSet(meetingId), MeetingManage::getMeetingName).get(meetingId);
        result.setNow(LocalDateTime.now());
        result.setMeetingName(meetingName);
        if (Objects.equals(Constant.MeetingManageStatus.OPENED.status, status)) {
            List<ReserveAudit> auditList = reserveAuditService.lambdaQuery().select(ReserveAudit::getMeetingSubject, ReserveAudit::getStartTime, ReserveAudit::getEndTime)
                    .eq(ReserveAudit::getMeetingId, meetingId)
                    .eq(ReserveAudit::getMeetingStatus, RESERVE_2.type)
                    .eq(ReserveAudit::getNowDate, LocalDate.now())
                    .ge(ReserveAudit::getEndTime, localTime)
                    .orderByAsc(ReserveAudit::getStartTime).list();
            List<ReserveTimeVO> times = auditList.parallelStream().map(t -> new ReserveTimeVO(t.getStartTime(), t.getEndTime(), t.getMeetingSubject()))
                    .collect(Collectors.toList());
            result.setMeetingTimes(times);
            result.setMeetingStatus(handleMeetingStatus(auditList));
        } else {
            result.setMeetingStatus(Constant.MeetingUsedState.SUSPENDED.state);
        }
        if (Objects.nonNull(deviceId)) {
            String finalDeviceId = deviceId;
            CompletableFuture.runAsync(() -> controlMeetingLight(finalDeviceId, result.getMeetingStatus()));
        }
        return result;
    }

    private void controlMeetingLight(String deviceId, int state) {
        if (USING.state == state) {
            log.info("当前会议室处于使用中,deviceId:{}", deviceId);
            MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.red, 90);
        } else if (AHEAD.state == state) {
            log.info("当前会议室即将开始,deviceId:{}", deviceId);
            MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.blue, 90);
        } else if (FREE.state == state) {
            log.info("当前会议室处于空闲,deviceId:{}", deviceId);
            MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.green, 90);
        }
    }

    private int handleMeetingStatus(List<ReserveAudit> auditList) {
        if (CollUtil.isNotEmpty(auditList)) {
            String startTime = auditList.get(0).getStartTime();
            LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_MINUTES_FORMAT));
            if (LocalTime.now().plusMinutes(30).isBefore(start)) {
                return FREE.state;
            } else if (LocalTime.now().isBefore(start)) {
                return AHEAD.state;
            } else {
                return USING.state;
            }
        }
        return FREE.state;
    }

    private void controlScreenDeviceLight(MeetingManageStatusDTO reqDTO) {
        CompletableFuture.runAsync(() -> {
            String screenDeviceId = this.getById(reqDTO.getId()).getScreenDeviceId();
            if (Objects.nonNull(screenDeviceId)) {
                Map<String, String> map = deviceInfoProvider.getDeviceIdByIds(Sets.newHashSet(reqDTO.getId()));
                String deviceId = map.get(screenDeviceId);
                if (Objects.nonNull(deviceId)) {
                    if (reqDTO.getStatus() == 0) {
                        MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.green, 0);
                    } else {
                        MeetingScreenUtils.controlLight(deviceId, MeetingScreenUtils.LightColor.green, 50);
                    }
                }
            }
        });

    }

    private void handleScreenDeviceStatus(List<MeetingManageVO> list) {
        Set<String> screenDeviceIds = list.parallelStream().map(MeetingManageVO::getScreenDeviceId).collect(Collectors.toSet());
        Map<String, Integer> map = deviceInfoProvider.getDeviceStatusByIds(screenDeviceIds);
        list.forEach(t -> {
            Integer status = map.get(t.getScreenDeviceId());
            t.setScreenStatus(status);
        });
    }

    /**
     * 塞入多余参数-拉取存在会议室的楼层
     *
     * @param node:
     * @param buildingId:
     * @return void
     * @MethodName setBuildingExtraInfo
     */
    private void setBuildingExtraInfo(SimpleNode node, String buildingId) {
        JSONObject jsonObject = new JSONObject();
        LambdaQueryWrapper<MeetingManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(MeetingManage::getFloorId).eq(MeetingManage::getBuildingId, buildingId).orderByDesc(MeetingManage::getFloorId);
        Set<String> floorIds =
                this.list(queryWrapper).parallelStream().map(MeetingManage::getFloorId).collect(Collectors.toSet());

        jsonObject.put("useFloor", floorIds);
        node.setExtraInfo(jsonObject);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingManage> getConditionWrapper(MeetingManageQuery query) {

        LambdaQueryWrapper<MeetingManage> queryWrapper = new LambdaQueryWrapper<MeetingManage>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<MeetingManageVO> entitiesToDTOs(List<MeetingManage> entities) {

        return MyBeanUtils.copyList(entities, MeetingManageVO::new, (s, t) -> {
        });
    }

    /**
     * 弃用方法
     *
     * @param meetingManages
     * @param supplier
     * @return
     */
    public List<MeetingManageVO> convert(List<MeetingManage> meetingManages, Supplier<MeetingManageVO> supplier) {
        Set<String> parkOrgIds =
                meetingManages.parallelStream().map(MeetingManage::getParkOrgId).collect(Collectors.toSet());
        Map<String, String> parkOrgNameMap =
                commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        List<MeetingManageVO> meetingManageVOList = MyBeanUtils.copyList(meetingManages, supplier, (s, t) -> {
            t.setParkOrgName(parkOrgNameMap.get(s.getParkOrgId()));
            // DO 拼接楼栋单元，楼层

        });
        return meetingManageVOList;
    }

}
