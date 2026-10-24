package com.crlink.smartpark.attendance.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.entity.AttendanceRule;
import com.crlink.smartpark.attendance.entity.AttendanceRuleRelation;
import com.crlink.smartpark.attendance.mapper.AttendanceRuleRelationMapper;
import com.crlink.smartpark.attendance.service.AttendanceRuleRelationService;
import com.crlink.smartpark.attendance.service.AttendanceRuleService;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 考勤规则关联员工 服务实现类
 * */
@Service
public class AttendanceRuleRelationServiceImpl extends ServiceImpl<AttendanceRuleRelationMapper, AttendanceRuleRelation>
        implements AttendanceRuleRelationService {

    @Resource
    private CommonProvider commonProvider;
    @Autowired
    private AttendanceRuleService attendanceRuleService;

    @Override
    public List<DictVO> getAllEmployeeByRuleId(String ruleId) {
        LambdaQueryWrapper<AttendanceRuleRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AttendanceRuleRelation::getEmployeeId);
        wrapper.eq(AttendanceRuleRelation::getRuleId, ruleId);
        Set<String> ids = list(wrapper).stream().map(AttendanceRuleRelation::getEmployeeId).collect(Collectors.toSet());
        Map<String, String> nameMap = commonProvider.getNamesByIdsAndType(ids, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        List<DictVO> list = ids.stream().map(e -> new DictVO(nameMap.get(e), e)).collect(Collectors.toList());
        return list;
    }

    /**
     * 更新员工关联关系
     *
     * @param employeeIds
     * @param ruleId
     */
    @Override
    public void updateAttendanceRel(Set<String> employeeIds, String ruleId) {
        if (StrUtil.isBlank(ruleId)) {
            return;
        }
        if (employeeIds == null) {
            employeeIds = new HashSet<>();
        }
        Set<AttendanceRuleRelation> relationSet = new HashSet<>();
        List<AttendanceRuleRelation> relationList =
                list(Wrappers.<AttendanceRuleRelation>lambdaQuery().eq(AttendanceRuleRelation::getRuleId, ruleId));
        Set<String> alreadyExistEmployeeIds =
                relationList.stream().map(AttendanceRuleRelation::getEmployeeId).collect(Collectors.toSet());
        Set<String> needDelEmployeeIds = Sets.newHashSet(alreadyExistEmployeeIds);
        Set<String> needAddEmployeeIds = employeeIds;
        if (CollUtil.isNotEmpty(relationList)) {
            // 需要删除的
            needDelEmployeeIds.removeAll(needAddEmployeeIds);
            // 需要增加的
            needAddEmployeeIds.removeAll(alreadyExistEmployeeIds);
            if (CollUtil.isNotEmpty(needDelEmployeeIds)) {
                remove(Wrappers.<AttendanceRuleRelation>lambdaQuery().eq(AttendanceRuleRelation::getRuleId, ruleId)
                        .in(AttendanceRuleRelation::getEmployeeId, needDelEmployeeIds));
            }
        }
        if (CollUtil.isNotEmpty(needAddEmployeeIds)) {
            needAddEmployeeIds.forEach(e -> {
                AttendanceRuleRelation relation = new AttendanceRuleRelation();
                relation.setEmployeeId(e);
                relation.setRuleId(ruleId);
                EntityUserUtil.setAll(relation);
                relationSet.add(relation);
            });
            saveBatch(relationSet);
        }
    }

    @Override
    public String getRuleIdByEmployeeId(String employeeId) {
        LambdaQueryWrapper<AttendanceRuleRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendanceRuleRelation::getEmployeeId, employeeId);
        AttendanceRuleRelation attendanceRuleRelation = getOne(wrapper, false);
        if (attendanceRuleRelation == null) {
            return null;
        }
        return attendanceRuleRelation.getRuleId();
    }

    @Override
    public List<AttendanceRuleRelation> getAllRelByRuleIds(Collection<String> ruleId) {
        LambdaQueryWrapper<AttendanceRuleRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AttendanceRuleRelation::getRuleId, ruleId);
        return list(wrapper);
    }

    @Override
    public Set<String> getEmployeeIdsWithIndividualRule(Set<String> employeeIds) {
        Set<String> enabledEmployees = Sets.newHashSet();
        Map<String, Set<String>> ruleId2EmployeeIds = this.lambdaQuery()
                .select(AttendanceRuleRelation::getRuleId, AttendanceRuleRelation::getEmployeeId)
                .in(AttendanceRuleRelation::getEmployeeId, employeeIds)
                .list()
                .stream().collect(Collectors.groupingBy(AttendanceRuleRelation::getRuleId,
                        Collectors.mapping(AttendanceRuleRelation::getEmployeeId, Collectors.toSet())));
        if (CollectionUtil.isEmpty(ruleId2EmployeeIds)) {
            return enabledEmployees;
        }
        Set<String> enabledRuleIds = attendanceRuleService.lambdaQuery().select(AttendanceRule::getId)
                .in(AttendanceRule::getId, ruleId2EmployeeIds.keySet())
                .eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES))
                .list().stream().map(AttendanceRule::getId).collect(Collectors.toSet());

        for (String enabledRuleId : enabledRuleIds) {
            enabledEmployees.addAll(ruleId2EmployeeIds.get(enabledRuleId));
        }
        return enabledEmployees;
    }
}
