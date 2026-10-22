package com.crlink.smartpark.attendance.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.dto.AttendanceRuleDTO;
import com.crlink.smartpark.attendance.entity.AttendanceRule;
import com.crlink.smartpark.attendance.entity.AttendanceRuleRelation;
import com.crlink.smartpark.attendance.mapper.AttendanceRuleMapper;
import com.crlink.smartpark.attendance.model.AttendanceRuleReqDTO;
import com.crlink.smartpark.attendance.model.AttendanceRuleStatusReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceRuleQuery;
import com.crlink.smartpark.attendance.service.AttendanceRuleRelationService;
import com.crlink.smartpark.attendance.service.AttendanceRuleService;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.bean.wx.WxOATemplateReqDTO;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.feign.WxOaMobileRelationProvider;
import com.crlink.smartpark.system.query.WxOaMobileRelationQuery;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 考勤规则 服务实现类
 * */
@Service
@Slf4j
public class AttendanceRuleServiceImpl extends ServiceImpl<AttendanceRuleMapper, AttendanceRule> implements AttendanceRuleService {

    @Resource
    private AttendanceRuleRelationService attendanceRuleRelationService;
    @Resource
    private EmployeeProvider employeeProvider;
    @Resource
    private WxOaMobileRelationProvider wxOaMobileRelationProvider;
    @Resource
    private WxOAUtil wxOAUtil;

    @Value("${param.wx.oa.appID:wx14a0e643373f29ea}")
    private String appID;

    @Value("${param.wx.oa.punchTemplateId:VviqEnieHGAcjEZNS1EeZB5NX-OdssqttcGsFAPM-ug}")
    private String punchTemplateId;

    /**
     * 保存或更新数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateAttendanceRule(AttendanceRuleReqDTO reqDTO) {
        checkParams(reqDTO);
        AttendanceRule entity = MyBeanUtils.copy(reqDTO, AttendanceRule::new);
        EntityUserUtil.setAllDynamic(entity);
        saveOrUpdate(entity);
        if (DictUtil.getValue(DictConstant.AttendanceRuleApplyType.EMPLOYEE).equals(reqDTO.getApplyType())) {
            attendanceRuleRelationService.updateAttendanceRel(reqDTO.getEmployeeIds(), entity.getId());
        } else if (DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY).equals(reqDTO.getApplyType())) {
            attendanceRuleRelationService.updateAttendanceRel(null, entity.getId());
        }
        return true;
    }

    /**
     * 检查是否会出现重复配置
     *
     * @param reqDTO
     */
    private void checkParams(AttendanceRuleReqDTO reqDTO) {
        if (DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY).equals(reqDTO.getApplyType())) {
            LambdaQueryWrapper<AttendanceRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AttendanceRule::getCompanyId, reqDTO.getCompanyId());
            wrapper.eq(AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY));
            AttendanceRule rule = getOne(wrapper, false);
            AssertUtil.requireTrue(rule == null || rule.getId().equals(reqDTO.getId()), "500001");
        } else if (DictUtil.getValue(DictConstant.AttendanceRuleApplyType.EMPLOYEE).equals(reqDTO.getApplyType())) {
            if (CollUtil.isEmpty(reqDTO.getEmployeeIds())) {
                return;
            }
            LambdaQueryWrapper<AttendanceRuleRelation> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(AttendanceRuleRelation::getEmployeeId, reqDTO.getEmployeeIds());
            Set<String> ruleIdSet = attendanceRuleRelationService.list(wrapper).stream().map(AttendanceRuleRelation::getRuleId).collect(Collectors.toSet());
            if (CollUtil.isEmpty(ruleIdSet)) {
                return;
            }
            if (StrUtil.isBlank(reqDTO.getId())) {
                throw new ResolinkRuntimeException("500002");
            } else {
                ruleIdSet.remove(reqDTO.getId());
                if (ruleIdSet.size() > 0) {
                    throw new ResolinkRuntimeException("500002");
                }
            }
        } else {
            throw new ResolinkRuntimeException("10003");
        }
    }

    /**
     * 通过id查询实体
     */
    @Override
    public AttendanceRuleDTO getAttendanceRuleById(String id) {
        AttendanceRule entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<AttendanceRuleDTO> listAttendanceRulePage(AttendanceRuleQuery query) {
        LambdaQueryWrapper<AttendanceRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AttendanceRule::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), AttendanceRule::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), AttendanceRule::getCompanyId, query.getCompanyId());
        wrapper.eq(StrUtil.isNotBlank(query.getRuleName()), AttendanceRule::getRuleName, query.getRuleName());
        wrapper.eq(query.getStatus() != null, AttendanceRule::getStatus, query.getStatus());
        wrapper.orderByDesc(AttendanceRule::getCreateTime);
        query.startPage();
        List<AttendanceRule> list = list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<AttendanceRuleDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void modifyStatus(AttendanceRuleStatusReqDTO dto) {
        LambdaUpdateWrapper<AttendanceRule> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(AttendanceRule::getStatus, dto.getEnable());
        wrapper.eq(AttendanceRule::getId, dto.getId());
        update(wrapper);
    }

    @Override
    public List<AttendanceRule> getAllEnableRules() {
        LambdaQueryWrapper<AttendanceRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES));
        return list(wrapper);
    }

    @Override
    public List<AttendanceRuleDTO> getAllRules() {
        LambdaQueryWrapper<AttendanceRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES));
        return entitiesToDTOs(list(wrapper));
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<AttendanceRuleDTO> entitiesToDTOs(List<AttendanceRule> entities) {
        return MyBeanUtils.copyList(entities, AttendanceRuleDTO::new, (s, t) -> {

        });
    }

    @Override
    public void punchReminder(String ruleId) {
        System.out.println(Thread.currentThread());
        Set<String> employeeMobiles = getEmployeeMobilesByRuleId(ruleId);
        WxOaMobileRelationQuery query = WxOaMobileRelationQuery.buildQuery(appID, employeeMobiles);
        Set<String> openIds = wxOaMobileRelationProvider.getOpenIdsByMobilesAndAppId(query);
        wxOAUtil.sendMsgByTemplate(openIds, wxOAUtil.getPunchReminderTemplateId());
    }

    @Override
    public void sendMsg(Set<String> mobile) {
        WxOaMobileRelationQuery query = WxOaMobileRelationQuery.buildQuery(appID, mobile);
        Set<String> openIds = wxOaMobileRelationProvider.getOpenIdsByMobilesAndAppId(query);
        wxOAUtil.sendMsgByTemplate(openIds, punchTemplateId);
    }

    @NotNull
    private Set<String> getEmployeeMobilesByRuleId(String ruleId) {
        Set<String> employeeMobiles;
        AttendanceRule attendanceRule = getById(ruleId);
        if (DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY).equals(attendanceRule.getApplyType())) {
            List<EmployeeVO> employeeVOList = employeeProvider.findEmployeeByCompanyIdsAndTagIds(Sets.newHashSet(attendanceRule.getCompanyId()), null, true);
            Set<String> employeeIds = employeeVOList.stream().map(EmployeeVO::getId).collect(Collectors.toSet());
            Set<String> employeeIdsWithIndividualRule = attendanceRuleRelationService.getEmployeeIdsWithIndividualRule(employeeIds);
            employeeMobiles = employeeVOList.stream()
                    .filter(e -> !employeeIdsWithIndividualRule.contains(e.getId())).map(EmployeeVO::getMobile).collect(Collectors.toSet());
        } else {
            Set<String> employeeIds = attendanceRuleRelationService.lambdaQuery()
                    .select(AttendanceRuleRelation::getEmployeeId)
                    .eq(AttendanceRuleRelation::getRuleId, ruleId)
                    .list().stream().map(AttendanceRuleRelation::getEmployeeId).collect(Collectors.toSet());
            EmployeeQueryDTO queryDTO = EmployeeQueryDTO.employeeIdsBuildDTO(employeeIds, "mobile");
            Map<String, EmployeeDTO> employeeDTOMap = employeeProvider.queryEmployeeByCondition(queryDTO);
            employeeMobiles = employeeDTOMap.values().stream().map(EmployeeDTO::getMobile).collect(Collectors.toSet());
        }
        return employeeMobiles;
    }
}
