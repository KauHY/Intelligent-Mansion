package com.crlink.smartpark.attendance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.dto.PunchRecordDTO;
import com.crlink.smartpark.attendance.entity.AttendanceRecord;
import com.crlink.smartpark.attendance.entity.AttendanceRule;
import com.crlink.smartpark.attendance.entity.AttendanceRuleRelation;
import com.crlink.smartpark.attendance.entity.PunchRecord;
import com.crlink.smartpark.attendance.mapper.PunchRecordMapper;
import com.crlink.smartpark.attendance.model.PunchRecordExportDTO;
import com.crlink.smartpark.attendance.model.PunchRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.PunchRecordQuery;
import com.crlink.smartpark.attendance.model.vo.AttendanceRecordDateVO;
import com.crlink.smartpark.attendance.service.AttendanceRecordService;
import com.crlink.smartpark.attendance.service.AttendanceRuleRelationService;
import com.crlink.smartpark.attendance.service.AttendanceRuleService;
import com.crlink.smartpark.attendance.service.PunchRecordService;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.field.EmployeeFiled;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 打卡记录 服务实现类
 * */
@Service
public class PunchRecordServiceImpl extends ServiceImpl<PunchRecordMapper, PunchRecord> implements PunchRecordService {

    @Autowired
    private AttendanceRuleRelationService attendanceRuleRelationService;
    @Autowired
    private AttendanceRuleService attendanceRuleService;
    @Autowired
    private EmployeeProvider employeeProvider;
    @Autowired
    private CommonProvider commonProvider;

    @Resource
    private AttendanceRecordService attendanceRecordService;

    public static final String FILE_NAME = "打卡记录";
    public static final String RULE_DATA = "ruleData";


    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<PunchRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<PunchRecord> lambdaQuery = this.lambdaQuery().in(PunchRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(PunchRecord::getId, sValue);
        }
        Stream<PunchRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(PunchRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(PunchRecord::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdatePunchRecord(PunchRecordReqDTO reqDTO) {
        PunchRecord entity = MyBeanUtils.copy(reqDTO, PunchRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public PunchRecordDTO getPunchRecordById(String id) {
        PunchRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<PunchRecordDTO> listPunchRecordPage(PunchRecordQuery query) {
        query.startPage();
        List<PunchRecord> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<PunchRecordDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public List<PunchRecord> getTodayRecord() {
        return this.lambdaQuery()
                .eq(PunchRecord::getEmployeeId, SystemUtil.getInstance().getEmployeeId())
                .eq(PunchRecord::getCompanyId, SystemUtil.getInstance().getEmployeeCompanyId())
                .eq(PunchRecord::getPunchDate, LocalDate.now())
                .orderByAsc(PunchRecord::getPunchDateTime)
                .list();
    }

    @Override
    public Map<LocalDate, List<PunchRecord>> listMonthDetail(PunchRecordQuery query) {
        LocalDate punchDate = query.getPunchDate();
        Map<LocalDate, List<PunchRecord>> result = this.lambdaQuery()
                .eq(PunchRecord::getEmployeeId, SystemUtil.getInstance().getEmployeeId())
                .eq(PunchRecord::getCompanyId, SystemUtil.getInstance().getEmployeeCompanyId())
                .ge(PunchRecord::getPunchDate, punchDate.withDayOfMonth(1))
                .le(PunchRecord::getPunchDate, punchDate.withDayOfMonth(punchDate.getDayOfMonth()))
                .orderByAsc(PunchRecord::getPunchDateTime)
                .list()
                .stream()
                .collect(Collectors.groupingBy(PunchRecord::getPunchDate));
        return result;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<PunchRecord> getConditionWrapper(PunchRecordQuery query) {
        LambdaQueryWrapper<PunchRecord> queryWrapper = new LambdaQueryWrapper<PunchRecord>();
        queryWrapper
                .eq(StrUtil.isNotBlank(query.getEmployeeId()), PunchRecord::getEmployeeId, query.getEmployeeId())
                .in(PunchRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .eq(StrUtil.isNotBlank(query.getParkOrgId()), PunchRecord::getParkOrgId, query.getParkOrgId())
                .eq(StrUtil.isNotBlank(query.getCompanyId()), PunchRecord::getCompanyId, query.getCompanyId())
                .like(StrUtil.isNotBlank(query.getEmployeeName()), PunchRecord::getEmployeeName, query.getEmployeeName())
                .le(Objects.nonNull(query.getPunchDateTimeEnd()), PunchRecord::getPunchDateTime, query.getPunchDateTimeEnd())
                .ge(Objects.nonNull(query.getPunchDateTimeStart()), PunchRecord::getPunchDateTime, query.getPunchDateTimeStart())
                .eq(Objects.nonNull(query.getType()), PunchRecord::getType, query.getType())
                .eq(Objects.nonNull(query.getPunchDate()), PunchRecord::getPunchDate, query.getPunchDate())
                .orderByAsc(PunchRecord::getParkOrgName, PunchRecord::getCompanyName, PunchRecord::getEmployeeName, PunchRecord::getPunchDateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<PunchRecordDTO> entitiesToDTOs(List<PunchRecord> entities) {
        return MyBeanUtils.copyList(entities, PunchRecordDTO::new, (s, t) -> {

        });
    }

    @Id2objHandle
    @Override
    public PunchRecordReqDTO wrapReq(PunchRecordReqDTO recordReqDTO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        AssertUtil.requireTrueOrParamExp(StrUtil.isNotBlank(employeeId));
        EmployeeInfoVO employeeInfo = SystemUtil.getInstance().getEmployeeInfo(employeeId);
        String companyId = employeeInfo.getCompanyId();
        String parkOrgId = employeeInfo.getParkOrgId();
        recordReqDTO.setEmployeeId(employeeId);
        recordReqDTO.setEmployeeName(employeeInfo.getName());
        recordReqDTO.setCompanyId(companyId);
        recordReqDTO.setParkOrgId(parkOrgId);

        recordReqDTO.setPunchDateTime(localDateTime);
        recordReqDTO.setPunchDate(localDateTime.toLocalDate());
        recordReqDTO.setPunchTime(localDateTime.toLocalTime());
        return recordReqDTO;
    }

    @Override
    public AttendanceRecordDateVO getRecordsByDate(String date) {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new ResolinkRuntimeException("10003");
        }
        List<PunchRecord> list = this.lambdaQuery()
                .eq(PunchRecord::getEmployeeId, SystemUtil.getInstance().getEmployeeId())
                .eq(PunchRecord::getCompanyId, SystemUtil.getInstance().getEmployeeCompanyId())
                .eq(PunchRecord::getPunchDate, targetDate)
                .orderByAsc(PunchRecord::getPunchDateTime)
                .list();
        AttendanceRecordDateVO vo = new AttendanceRecordDateVO();
        vo.setPunchRecordList(list);
        LambdaQueryWrapper<AttendanceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttendanceRecord::getEmployeeId, SystemUtil.getInstance().getEmployeeId())
                .eq(AttendanceRecord::getCompanyId, SystemUtil.getInstance().getEmployeeCompanyId())
                .eq(AttendanceRecord::getDate, targetDate);
        AttendanceRecord attendanceRecord = attendanceRecordService.getOne(wrapper, false);
        if (attendanceRecord != null) {
            vo.setAttendanceResult(attendanceRecord.getAttendanceResult());
        }
        return vo;
    }

    @Override
    public void export(PunchRecordQuery query) {
        LambdaQueryWrapper<PunchRecord> conditionWrapper = getConditionWrapper(query);
        ExcelExportUtils.export(() -> list(conditionWrapper), this::entityToExportDTO, FILE_NAME, PunchRecordExportDTO.class);
    }

    private List<PunchRecordExportDTO> entityToExportDTO(List<PunchRecord> list) {
        return MyBeanUtils.copyList(list, PunchRecordExportDTO::new, (s, t) -> {
            if (DictConstant.PunchType.FIELDWORK.getCode() == s.getType()) {
                Map<String, Object> extension = s.getExtension();
                t.setReason((String) extension.get("reason"));
                t.setPicKey((String) extension.get("picKey"));
            }
        });
    }

    @Override
    public List<SimpleNode> getCompanySelectWithRule() {

        EmployeeQueryDTO employeeQueryDTO = EmployeeQueryDTO.mobileBuildDTO(SystemUtil.getInstance().getWxUserMobile(), EmployeeFiled.COMPANY_ID);
        employeeQueryDTO.setParkOrgId(SystemUtil.getInstance().getCurrentHeadOrgId());
        Map<String, Map<String, Object>> employeeResult = employeeProvider.queryEmployeeColumns(employeeQueryDTO);
        Map<String, String> company2EmployeeMap = employeeResult.values().stream()
                .collect(Collectors.toMap(e -> e.get(EmployeeFiled.COMPANY_ID).toString(), e -> e.get(EmployeeFiled.ID).toString(), (v1, v2) -> v1));

        Set<String> ruleIds = attendanceRuleRelationService.lambdaQuery().select(AttendanceRuleRelation::getRuleId)
                .in(AttendanceRuleRelation::getEmployeeId, employeeResult.keySet())
                .list()
                .stream().map(AttendanceRuleRelation::getRuleId).collect(Collectors.toSet());
        List<AttendanceRule> list = attendanceRuleService.lambdaQuery()
                .eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES))
                .and(condition -> condition.eq(CollectionUtil.isNotEmpty(company2EmployeeMap.keySet()), AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY)).in(CollectionUtil.isNotEmpty(company2EmployeeMap.keySet()), AttendanceRule::getCompanyId, company2EmployeeMap.keySet())
                        .or(CollectionUtil.isNotEmpty(ruleIds), condition1 -> condition1.eq(AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.EMPLOYEE)).in(AttendanceRule::getId, ruleIds))
                )
                .list();
        Set<String> companyIdsWithRule = list.stream().map(AttendanceRule::getCompanyId).collect(Collectors.toSet());
        Map<String, String> companyId2NameMap = commonProvider.getNamesByIdsAndType(companyIdsWithRule, SimpleNode.TypeEnum.COMPANY.getCode());

        List<SimpleNode> companySelect = list.stream().map(e -> {
            SimpleNode simpleNode = new SimpleNode(e.getCompanyId(), companyId2NameMap.get(e.getCompanyId()));
            Map<String, Object> extraInfo = Maps.newHashMap();
            extraInfo.put(Constants.EMPLOYEE_ID, company2EmployeeMap.get(e.getCompanyId()));
            extraInfo.put(RULE_DATA, e);
            simpleNode.setExtraInfo(extraInfo);
            return simpleNode;
        }).collect(Collectors.toList());

        return companySelect;
    }

    @Override
    public Boolean hasRule() {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            return false;
        }
        AttendanceRuleRelation attendanceRuleRelation = attendanceRuleRelationService.lambdaQuery().select(AttendanceRuleRelation::getRuleId)
                .eq(AttendanceRuleRelation::getEmployeeId, employeeId).one();

        if (Objects.nonNull(attendanceRuleRelation) &&
                attendanceRuleService.lambdaQuery()
                        .eq(AttendanceRule::getId, attendanceRuleRelation.getRuleId())
                        .eq(AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.EMPLOYEE))
                        .eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES)).count() > 0) {
            return true;
        }

        return attendanceRuleService.lambdaQuery()
                .eq(AttendanceRule::getCompanyId, SystemUtil.getInstance().getEmployeeCompanyId())
                .eq(AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY))
                .eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES)).count() > 0;
    }

    @Override
    public AttendanceRule getRule() {
        AttendanceRuleRelation attendanceRuleRelation = attendanceRuleRelationService.lambdaQuery().select(AttendanceRuleRelation::getRuleId)
                .eq(AttendanceRuleRelation::getEmployeeId, SystemUtil.getInstance().getEmployeeId()).one();
        AttendanceRule attendanceRule;
        if (Objects.nonNull(attendanceRuleRelation)
                && Objects.nonNull(attendanceRule = attendanceRuleService.lambdaQuery()
                .eq(AttendanceRule::getId, attendanceRuleRelation.getRuleId())
                .eq(AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.EMPLOYEE))
                .eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES)).one())) {
            return attendanceRule;
        }

        return attendanceRuleService.lambdaQuery()
                .eq(AttendanceRule::getCompanyId, SystemUtil.getInstance().getEmployeeCompanyId())
                .eq(AttendanceRule::getApplyType, DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY))
                .eq(AttendanceRule::getStatus, DictUtil.getValue(DictConstant.YesOrNo.YES)).one();
    }
}
