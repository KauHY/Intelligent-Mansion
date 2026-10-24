package com.crlink.smartpark.attendance.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.attendance.entity.*;
import com.crlink.smartpark.attendance.service.*;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.HolidayUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceTask {

    @Resource
    private AttendanceRuleService attendanceRuleService;

    @Resource
    private AttendanceRecordService attendanceRecordService;

    @Resource
    private PunchRecordService punchRecordService;

    @Resource
    private AttendanceRuleRelationService attendanceRuleRelationService;

    @Resource
    private EmployeeProvider employeeProvider;

    @Resource
    private AttendanceMonthRecordService attendanceMonthRecordService;

    /**
     * 生成考勤记录
     */
    public void generateAttendanceRecord() {
        StopWatch stopWatch = new StopWatch();
        log.info("生成考勤记录定时器开始");
        stopWatch.start();
        //所有设置了考勤规则的公司
        List<AttendanceRule> allCompanyIdsWithRule = attendanceRuleService.getAllEnableRules();
        if (CollUtil.isEmpty(allCompanyIdsWithRule)) {
            return;
        }
        //所有设置了考勤规则的公司下的所有员工
        List<EmployeeVO> allNeedPunchEmployees = employeeProvider.findEmployeeByCompanyIdsAndTagIds(allCompanyIdsWithRule.stream().map(AttendanceRule::getCompanyId).collect(Collectors.toSet()), null, true);
        if (CollUtil.isEmpty(allNeedPunchEmployees)) {
            return;
        }
        //获取目标日期的所有打卡记录
        LocalDate targetDay = LocalDate.now().minusDays(1);
        LambdaQueryWrapper<PunchRecord> punchWrapper = new LambdaQueryWrapper<>();
        punchWrapper.eq(PunchRecord::getPunchDate, targetDay);
        List<PunchRecord> punchRecordList = punchRecordService.list(punchWrapper);
        if (CollUtil.isEmpty(punchRecordList)) {
            return;
        }
        //获取所有启用的规则的关联员工
        List<AttendanceRuleRelation> ruleEmployeeRelationList = attendanceRuleRelationService.getAllRelByRuleIds(allCompanyIdsWithRule.stream().map(AttendanceRule::getId).collect(Collectors.toSet()));
        //转换成公司考勤规则map（作为数据库缓存使用）
        Map<String, Map<Integer, Map<String, AttendanceRule>>> companyAttendanceMap = allCompanyIdsWithRule.stream().collect(
                Collectors.groupingBy(AttendanceRule::getCompanyId,
                        Collectors.groupingBy(AttendanceRule::getApplyType,
                                Collectors.toMap(AttendanceRule::getId, Function.identity()))));
        //转换成按照公司分组打卡记录map（作为数据库缓存使用）
        Map<String, Map<String, List<PunchRecord>>> companyPunchRecordMap = punchRecordList.stream().collect(Collectors.groupingBy(PunchRecord::getCompanyId, Collectors.groupingBy(PunchRecord::getEmployeeId)));
        //转换成按照公司分组所有员工map（作为数据库缓存使用）
        Map<String, List<EmployeeVO>> companyEmployeeMap = allNeedPunchEmployees.stream().collect(Collectors.groupingBy(EmployeeVO::getCompanyId));
        //转换换成按照员工关联map
        Map<String, AttendanceRuleRelation> employeeRuleRelMap = ruleEmployeeRelationList.stream().collect(Collectors.toMap(AttendanceRuleRelation::getEmployeeId, Function.identity()));
        //目标日是否节假日
        boolean targetDayIsWorkDay = HolidayUtil.isWorkDay(targetDay);
        //当前处理的年份
        Integer year = targetDay.getYear();
        //当前处理的月份
        Integer month = targetDay.getMonthValue();
        for (Map.Entry<String, List<EmployeeVO>> entry : companyEmployeeMap.entrySet()) {
            //公司ID
            String companyId = entry.getKey();
            //当前公司下所有需要打卡的员工
            List<EmployeeVO> employeeList = entry.getValue();
            try {
                //当前公司所有考勤规则
                Map<Integer, Map<String, AttendanceRule>> companyRules = companyAttendanceMap.get(companyId);
                if (CollUtil.isEmpty(companyRules)) {
                    continue;
                }
                //正常打卡员工ID集合
                Set<EmployeeVO> normalPunchEmployeeSet = new HashSet<>();
                //异常打卡员工ID集合
                Set<EmployeeVO> exPunchEmployeeSet = new HashSet<>();
                //缺席员工ID集合
                Set<EmployeeVO> absentEmployeeSet = new HashSet<>();
                //需要新增的考勤记录集合
                List<AttendanceRecord> companyAllAttendanceRecord = new ArrayList<>();
                for (EmployeeVO employeeVO : employeeList) {
                    //员工ID
                    String employeeId = employeeVO.getId();
                    //当前员工适用的考勤规则
                    AttendanceRule curEmployeeAttendanceRule = null;
                    //员工个性考勤规则
                    AttendanceRuleRelation attendanceRuleRelation = employeeRuleRelMap.get(employeeId);
                    //存在个性规则设置
                    if (attendanceRuleRelation != null) {
                        Map<String, AttendanceRule> employeeTypeRules = companyRules.get(DictUtil.getValue(DictConstant.AttendanceRuleApplyType.EMPLOYEE));
                        if (CollUtil.isNotEmpty(employeeTypeRules)) {
                            curEmployeeAttendanceRule = employeeTypeRules.get(attendanceRuleRelation.getRuleId());
                        }
                    } else {
                        Map<String, AttendanceRule> companyTypeRules = companyRules.get(DictUtil.getValue(DictConstant.AttendanceRuleApplyType.COMPANY));
                        //适用公司统一规则
                        if (CollUtil.isNotEmpty(companyTypeRules)) {
                            curEmployeeAttendanceRule = companyTypeRules.values().stream().findFirst().orElse(null);
                        }
                    }
                    if (curEmployeeAttendanceRule == null) {
                        log.error("考勤规则不存在或被禁用，暂无法处理，companyId：{}，employeeId：{}", companyId, employeeId);
                        continue;
                    }
                    //判断目标天是否需要打卡
                    boolean curDayNeedPunch;
                    //关联法定工作日
                    if (DictUtil.getValue(DictConstant.AttendanceWorkdayType.LEGAL).equals(curEmployeeAttendanceRule.getWorkdayType())) {
                        curDayNeedPunch = targetDayIsWorkDay;
                    } else {
                        //自定义星期天
                        List<Integer> days = curEmployeeAttendanceRule.getDaysOfWork();
                        curDayNeedPunch = CollUtil.isNotEmpty(days) && days.contains(targetDay.getDayOfWeek().getValue());
                    }
                    //不在打卡日期范围内
                    if (!curDayNeedPunch) {
                        continue;
                    }
                    //当前员工当天所有打卡记录
                    Map<String, List<PunchRecord>> curCompanyPunchRecordMap = companyPunchRecordMap.get(companyId);
                    List<PunchRecord> employeePunchRecordList = MapUtil.isEmpty(curCompanyPunchRecordMap) ? null : curCompanyPunchRecordMap.get(employeeVO.getId());
                    //缺勤记录后直接跳过
                    if (CollUtil.isEmpty(employeePunchRecordList)) {
                        absentEmployeeSet.add(employeeVO);
                        continue;
                    }
                    //时间排序
                    employeePunchRecordList = employeePunchRecordList.stream().sorted(Comparator.comparing(PunchRecord::getPunchDateTime)).collect(Collectors.toList());
                    PunchRecord startPunchRecord = employeePunchRecordList.get(0);
                    PunchRecord endPunchRecord = null;
                    //多次打卡记录
                    if (employeePunchRecordList.size() >= 2) {
                        endPunchRecord = employeePunchRecordList.get(employeePunchRecordList.size() - 1);
                    }
                    //获取打卡结果
                    Integer attendanceResult = getPunchResult(curEmployeeAttendanceRule, startPunchRecord, endPunchRecord);
                    //封装考勤记录
                    AttendanceRecord attendanceRecord = buildAttendanceRecord(startPunchRecord, endPunchRecord, attendanceResult);
                    companyAllAttendanceRecord.add(attendanceRecord);
                    //统计异常和正常打卡的员工ID
                    if (DictUtil.getValue(DictConstant.AttendanceResultType.EXCEPTION).equals(attendanceResult)) {
                        exPunchEmployeeSet.add(employeeVO);
                    } else if (DictUtil.getValue(DictConstant.AttendanceResultType.NORMAL).equals(attendanceResult)) {
                        normalPunchEmployeeSet.add(employeeVO);
                    }
                }
                //新增打卡记录
                attendanceRecordService.saveBatch(companyAllAttendanceRecord);
                //月度记录处理开始
                updateMonthRecord(employeeList, normalPunchEmployeeSet, exPunchEmployeeSet, absentEmployeeSet, year, month, companyId);
            } catch (Exception e) {
                log.error("考勤记录定时器处理异常，companyId:{}", companyId, e);
            }
        }
        stopWatch.stop();
        log.info("生成考勤记录定时器结束，耗时：{}", stopWatch.getTotalTimeSeconds());
    }

    /**
     * 更新月度统计表
     *
     * @param employeeList
     * @param normalPunchEmployeeSet
     * @param exPunchEmployeeSet
     * @param absentEmployeeSet
     * @param year
     * @param month
     * @param companyId
     */
    private void updateMonthRecord(List<EmployeeVO> employeeList, Set<EmployeeVO> normalPunchEmployeeSet, Set<EmployeeVO> exPunchEmployeeSet, Set<EmployeeVO> absentEmployeeSet, Integer year, Integer month, String companyId) {
        LambdaQueryWrapper<AttendanceMonthRecord> monthRecordWrapper = new LambdaQueryWrapper<>();
        monthRecordWrapper.eq(AttendanceMonthRecord::getYear, year);
        monthRecordWrapper.eq(AttendanceMonthRecord::getMonth, month);
        monthRecordWrapper.eq(AttendanceMonthRecord::getCompanyId, companyId);
        List<AttendanceMonthRecord> existMonthRecords = attendanceMonthRecordService.list(monthRecordWrapper);
        Map<String, AttendanceMonthRecord> existMonthRecordEmployeeMap = existMonthRecords.stream().collect(Collectors.toMap(AttendanceMonthRecord::getEmployeeId, Function.identity()));
        AttendanceMonthRecord attendanceMonthRecord;
        List<AttendanceMonthRecord> needAddMonthRecords = new ArrayList<>();
        List<AttendanceMonthRecord> needUpdateMonthRecords = new ArrayList<>();
        for (EmployeeVO employeeVO : employeeList) {
            //新增月度记录
            if (!existMonthRecordEmployeeMap.containsKey(employeeVO.getId())) {
                attendanceMonthRecord = buildNewMonthRecord(year, month, employeeVO);
                updateMonthRecordTimes(normalPunchEmployeeSet, exPunchEmployeeSet, absentEmployeeSet, attendanceMonthRecord, employeeVO);
                needAddMonthRecords.add(attendanceMonthRecord);
            } else {
                //修改月度记录
                attendanceMonthRecord = existMonthRecordEmployeeMap.get(employeeVO.getId());
                updateMonthRecordTimes(normalPunchEmployeeSet, exPunchEmployeeSet, absentEmployeeSet, attendanceMonthRecord, employeeVO);
                needUpdateMonthRecords.add(attendanceMonthRecord);
            }
        }
        //新增月度统计记录
        attendanceMonthRecordService.saveBatch(needAddMonthRecords);
        //更新月度统计记录
        attendanceMonthRecordService.updateBatchById(needUpdateMonthRecords);
    }

    @NotNull
    private AttendanceMonthRecord buildNewMonthRecord(Integer year, Integer month, EmployeeVO employeeVO) {
        AttendanceMonthRecord attendanceMonthRecord;
        attendanceMonthRecord = new AttendanceMonthRecord();
        attendanceMonthRecord.setEmployeeId(employeeVO.getId());
        attendanceMonthRecord.setEmployeeName(employeeVO.getEmployeeName());
        attendanceMonthRecord.setParkOrgId(employeeVO.getParkOrgId());
        attendanceMonthRecord.setParkOrgName(employeeVO.getParkOrgName());
        attendanceMonthRecord.setCompanyId(employeeVO.getCompanyId());
        attendanceMonthRecord.setCompanyName(employeeVO.getCompanyName());
        attendanceMonthRecord.setYear(year);
        attendanceMonthRecord.setMonth(month);
        attendanceMonthRecord.setExceptionTimes(0);
        attendanceMonthRecord.setAbsenceTimes(0);
        attendanceMonthRecord.setNormalTimes(0);
        return attendanceMonthRecord;
    }

    private void updateMonthRecordTimes(Set<EmployeeVO> normalPunchEmployeeSet, Set<EmployeeVO> exPunchEmployeeSet, Set<EmployeeVO> absentEmployeeSet, AttendanceMonthRecord attendanceMonthRecord, EmployeeVO employeeVO) {
        if (normalPunchEmployeeSet.contains(employeeVO)) {
            attendanceMonthRecord.setNormalTimes(attendanceMonthRecord.getNormalTimes() + 1);
        }
        if (exPunchEmployeeSet.contains(employeeVO)) {
            attendanceMonthRecord.setExceptionTimes(attendanceMonthRecord.getExceptionTimes() + 1);
        }
        if (absentEmployeeSet.contains(employeeVO)) {
            attendanceMonthRecord.setAbsenceTimes(attendanceMonthRecord.getAbsenceTimes() + 1);
        }
    }

    @NotNull
    private AttendanceRecord buildAttendanceRecord(PunchRecord startPunchRecord, PunchRecord endPunchRecord, Integer attendanceResult) {
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setEmployeeId(startPunchRecord.getEmployeeId());
        attendanceRecord.setEmployeeName(startPunchRecord.getEmployeeName());
        attendanceRecord.setParkOrgId(startPunchRecord.getParkOrgId());
        attendanceRecord.setParkOrgName(startPunchRecord.getParkOrgName());
        attendanceRecord.setCompanyId(startPunchRecord.getCompanyId());
        attendanceRecord.setCompanyName(startPunchRecord.getCompanyName());
        attendanceRecord.setDate(startPunchRecord.getPunchDate());
        attendanceRecord.setPunchInTime(startPunchRecord.getPunchDateTime());
        attendanceRecord.setAttendanceResult(attendanceResult);
        attendanceRecord.setPunchInType(startPunchRecord.getType());
        attendanceRecord.setPunchOutType(endPunchRecord != null ? endPunchRecord.getType() : null);
        attendanceRecord.setPunchOutTime(endPunchRecord != null ? endPunchRecord.getPunchDateTime() : null);
        Integer outSidePunchType = DictConstant.PunchType.FIELDWORK.getCode();
        if (outSidePunchType.equals(startPunchRecord.getType()) || (endPunchRecord != null && outSidePunchType.equals(endPunchRecord.getType()))) {
            attendanceRecord.setPunchType(outSidePunchType);
        } else {
            attendanceRecord.setPunchType(DictConstant.PunchType.NORMAL.getCode());
        }
        return attendanceRecord;
    }

    private Integer getPunchResult(AttendanceRule curEmployeeAttendanceRule, PunchRecord startPunchRecord, PunchRecord endPunchRecord) {
        if (startPunchRecord == null || endPunchRecord == null) {
            return DictUtil.getValue(DictConstant.AttendanceResultType.EXCEPTION);
        }
        //上下班打卡都在打卡范围内时间时设置打卡结果为正常
        if (startPunchRecord.getPunchTime().isAfter(curEmployeeAttendanceRule.getPunchInTime())
                || endPunchRecord.getPunchTime().isBefore(curEmployeeAttendanceRule.getPunchOutTime())) {
            return DictUtil.getValue(DictConstant.AttendanceResultType.EXCEPTION);
        }
        return DictUtil.getValue(DictConstant.AttendanceResultType.NORMAL);
    }

}
