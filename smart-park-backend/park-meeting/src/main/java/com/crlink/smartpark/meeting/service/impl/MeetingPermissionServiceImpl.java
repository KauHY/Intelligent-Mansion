package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.meeting.MeetingConstant;
import com.crlink.smartpark.meeting.common.Constant;
import com.crlink.smartpark.meeting.entity.MeetingManage;
import com.crlink.smartpark.meeting.entity.MeetingPermission;
import com.crlink.smartpark.meeting.controller.request.query.MeetingPermissionQuery;
import com.crlink.smartpark.meeting.dto.MeetingPermissionDTO;
import com.crlink.smartpark.meeting.model.dto.MeetingPermissionReqDTO;
import com.crlink.smartpark.meeting.service.MeetingManageService;
import com.crlink.smartpark.meeting.service.MeetingPermissionService;
import com.crlink.smartpark.meeting.mapper.MeetingPermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 会议室权限关联 服务实现类
 * */
@Service
public class MeetingPermissionServiceImpl extends ServiceImpl<MeetingPermissionMapper, MeetingPermission>
    implements MeetingPermissionService {

    @Autowired
    private MeetingManageService meetingManageService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingPermission, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingPermission> lambdaQuery = this.lambdaQuery().in(MeetingPermission::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingPermission::getId, sValue);
        }
        Stream<MeetingPermission> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingPermission::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingPermission::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingPermission(MeetingPermissionReqDTO reqDTO) {
        MeetingPermission entity = MyBeanUtils.copy(reqDTO, MeetingPermission::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeetingPermissionDTO getMeetingPermissionById(String id) {
        MeetingPermission entity =
            Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MeetingPermissionDTO> listMeetingPermissionPage(MeetingPermissionQuery query) {
        query.startPage();
        List<MeetingPermission> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingPermissionDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean removePermissionByMeetingId(String meetingId) {
        LambdaQueryWrapper<MeetingPermission> queryWrapper = new LambdaQueryWrapper<MeetingPermission>();
        queryWrapper.eq(MeetingPermission::getMeetingId, meetingId);
        return this.remove(queryWrapper);
    }

    @Override
    public List<MeetingPermissionDTO> findMeetingPermissionByMeetingId(String meetingId) {
        List<MeetingPermission> meetingPermissionList =
            this.lambdaQuery().eq(MeetingPermission::getMeetingId, meetingId).list();
        List<MeetingPermissionDTO> meetingPermissionDTOList =
            MyBeanUtils.copyList(meetingPermissionList, MeetingPermissionDTO::new);

        return meetingPermissionDTOList;
    }

    @Override
    public Set<String> getPermission(Set<String> meetingIds, String employeeId, String companyId) {
        return this.lambdaQuery().select(MeetingPermission::getMeetingId)
            .and(condition -> condition.in(MeetingPermission::getMeetingId, meetingIds)
                .eq(MeetingPermission::getCompanyId, companyId).or().in(MeetingPermission::getMeetingId, meetingIds)
                .eq(MeetingPermission::getEmployeeId, employeeId))
            .list().parallelStream().map(MeetingPermission::getMeetingId).collect(Collectors.toSet());
    }

    @Override
    public boolean noPermission(String meetingId, String employeeId, String companyId) {
        return this
            .lambdaQuery().eq(MeetingPermission::getMeetingId, meetingId).and(condition -> condition
                .eq(MeetingPermission::getCompanyId, companyId).or().eq(MeetingPermission::getEmployeeId, employeeId))
            .count() > Constant.ZERO;
    }

    @Override
    public Boolean checkPermission(String parkOrgId) {
        Set<String> companyIds = new HashSet<>();
        Set<String> employeeIds = new HashSet<>();

        companyIds.add(SystemUtil.getInstance().getEmployeeCompanyId());
        employeeIds.add(SystemUtil.getInstance().getEmployeeId());

        if (CollUtil.isEmpty(companyIds) || CollUtil.isEmpty(employeeIds)) {
            // 如果这两个参数为空，直接无权限
            throw new ResolinkRuntimeException("400001");
        }
        Set<String> meetingIds =
            this.lambdaQuery().select(MeetingPermission::getMeetingId).in(MeetingPermission::getEmployeeId, employeeIds)
                .or().in(MeetingPermission::getCompanyId, companyIds).groupBy(MeetingPermission::getMeetingId).list()
                .parallelStream().map(MeetingPermission::getMeetingId).collect(Collectors.toSet());
        int count = 0;
        if (CollectionUtils.isNotEmpty(meetingIds)) {
            count = meetingManageService.lambdaQuery().eq(MeetingManage::getParkOrgId, parkOrgId)
                .in(MeetingManage::getId, meetingIds).count();
        }

        if (count == Constant.ZERO) {
            throw new ResolinkRuntimeException("400001");
        }
        return true;
    }

    @Override
    public Boolean updatePermissionByMeetingId(Set<String> permissionList, Integer relationType, String meetingId) {
        // Set<MeetingPermission> relationSet = new HashSet<>(permissionList.size());
        // Set<String> needAddEmployeeIds = permissionList;
        // // 如果是公司就全删全加 员工权限去重新增
        // // 类型为员工
        // if (DictUtil.getValue(MeetingConstant.RelationType.EMPLOYEE).equals(relationType)) {
        // List<MeetingPermission> relationList =
        // this.list(Wrappers.<MeetingPermission>lambdaQuery().eq(MeetingPermission::getMeetingId, meetingId));
        // if (CollUtil.isNotEmpty(relationList)) {
        // if (relationList.get(0).getRelationType() == relationType) {
        // Set<String> alreadyExistIds =
        // relationList.stream().map(MeetingPermission::getEmployeeId).collect(Collectors.toSet());
        // Set<String> needDelEmployeeIds = Sets.newHashSet(alreadyExistIds);
        //
        // // 需要删除的
        // needDelEmployeeIds.removeAll(needAddEmployeeIds);
        // // 需要增加的
        // needAddEmployeeIds.removeAll(alreadyExistIds);
        // // 删除多余的
        // if (CollUtil.isNotEmpty(needDelEmployeeIds)) {
        // remove(Wrappers.<MeetingPermission>lambdaQuery().eq(MeetingPermission::getMeetingId, meetingId)
        // .in(MeetingPermission::getEmployeeId, needDelEmployeeIds));
        // }
        // } else {
        // // 权限类型变换 全删全加
        // this.removePermissionByMeetingId(meetingId);
        // }
        // }
        // // 拼装员工权限新增
        // if (CollUtil.isNotEmpty(needAddEmployeeIds)) {
        // needAddEmployeeIds.forEach(e -> {
        // MeetingPermission meetingPermission = new MeetingPermission();
        // meetingPermission.setRelationType(relationType);
        // meetingPermission.setMeetingId(meetingId);
        // meetingPermission.setEmployeeId(e);
        // EntityUserUtil.setAll(meetingPermission);
        // relationSet.add(meetingPermission);
        // });
        // }
        // } else {
        // // 公司权限 全删全加
        // this.removePermissionByMeetingId(meetingId);
        // needAddEmployeeIds.forEach(e -> {
        // MeetingPermission meetingPermission = new MeetingPermission();
        // meetingPermission.setRelationType(relationType);
        // meetingPermission.setMeetingId(meetingId);
        // meetingPermission.setCompanyId(e);
        // EntityUserUtil.setAll(meetingPermission);
        // relationSet.add(meetingPermission);
        // });
        // }

        Set<String> existEmployeeIds = this
            .list(Wrappers.<MeetingPermission>lambdaQuery().select(MeetingPermission::getEmployeeId)
                .eq(MeetingPermission::getMeetingId, meetingId)
                .eq(MeetingPermission::getRelationType, MeetingConstant.MeeRelationType.EMPLOYEE))
            .stream().map(MeetingPermission::getEmployeeId).collect(Collectors.toSet());
        Set<MeetingPermission> relationSet = Sets.newHashSet();
        if (DictUtil.getValue(MeetingConstant.MeeRelationType.EMPLOYEE).equals(relationType)) {
            if (CollUtil.isNotEmpty(existEmployeeIds)) {
                Collection<String> intersection = CollUtil.intersection(permissionList, existEmployeeIds);
                permissionList.removeAll(intersection);
                existEmployeeIds.removeAll(intersection);
                remove(Wrappers.<MeetingPermission>lambdaQuery().eq(MeetingPermission::getMeetingId, meetingId)
                    .in(MeetingPermission::getEmployeeId, existEmployeeIds));
            }
            permissionList.forEach(e -> {
                MeetingPermission meetingPermission = new MeetingPermission();
                meetingPermission.setRelationType(relationType);
                meetingPermission.setMeetingId(meetingId);
                meetingPermission.setEmployeeId(e);
                EntityUserUtil.setAll(meetingPermission);
                relationSet.add(meetingPermission);
            });
        } else {
            // 公司权限 全删全加
            this.removePermissionByMeetingId(meetingId);
            permissionList.forEach(e -> {
                MeetingPermission meetingPermission = new MeetingPermission();
                meetingPermission.setRelationType(relationType);
                meetingPermission.setMeetingId(meetingId);
                meetingPermission.setCompanyId(e);
                EntityUserUtil.setAll(meetingPermission);
                relationSet.add(meetingPermission);
            });
        }
        return this.saveBatch(relationSet);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingPermission> getConditionWrapper(MeetingPermissionQuery query) {
        LambdaQueryWrapper<MeetingPermission> queryWrapper = new LambdaQueryWrapper<MeetingPermission>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<MeetingPermissionDTO> entitiesToDTOs(List<MeetingPermission> entities) {
        return MyBeanUtils.copyList(entities, MeetingPermissionDTO::new, (s, t) -> {
        });
    }
}
