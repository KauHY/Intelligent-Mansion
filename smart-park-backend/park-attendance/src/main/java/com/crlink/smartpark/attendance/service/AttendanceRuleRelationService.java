package com.crlink.smartpark.attendance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.entity.AttendanceRuleRelation;
import com.crlink.smartpark.system.vo.DictVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 考勤规则关联员工 服务类
 * </p>
 * */
public interface AttendanceRuleRelationService extends IService<AttendanceRuleRelation> {

    /**
     * 通过考勤规则ID查询所有员工
     *
     * @param ruleId
     * @return
     */
    List<DictVO> getAllEmployeeByRuleId(String ruleId);

    /**
     * 更新规则关联的员工
     *
     * @param employeeIds
     * @param ruleId
     */
    void updateAttendanceRel(Set<String> employeeIds, String ruleId);

    /**
     * 通过员工ID获取考勤规则ID
     *
     * @param employeeId
     * @return
     */
    String getRuleIdByEmployeeId(String employeeId);

    /**
     * 通过规则集合获取所有关联关系
     *
     * @param ruleId
     * @return
     */
    List<AttendanceRuleRelation> getAllRelByRuleIds(Collection<String> ruleId);

    Set<String> getEmployeeIdsWithIndividualRule(Set<String> employeeIds);
}
