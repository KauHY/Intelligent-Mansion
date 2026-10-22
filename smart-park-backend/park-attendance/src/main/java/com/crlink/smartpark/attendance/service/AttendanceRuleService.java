package com.crlink.smartpark.attendance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.dto.AttendanceRuleDTO;
import com.crlink.smartpark.attendance.entity.AttendanceRule;
import com.crlink.smartpark.attendance.model.AttendanceRuleReqDTO;
import com.crlink.smartpark.attendance.model.AttendanceRuleStatusReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceRuleQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 考勤规则 服务类
 * </p>
 * */
public interface AttendanceRuleService extends IService<AttendanceRule> {

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateAttendanceRule(AttendanceRuleReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    AttendanceRuleDTO getAttendanceRuleById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<AttendanceRuleDTO> listAttendanceRulePage(AttendanceRuleQuery query);

    /**
     * 修改状态
     *
     * @param dto
     */
    void modifyStatus(AttendanceRuleStatusReqDTO dto);

    /**
     * 获取所有考勤规则
     *
     * @return
     */
    List<AttendanceRule> getAllEnableRules();

    /**
     * 获取所有考勤规则
     *
     * @return
     */
    List<AttendanceRuleDTO> getAllRules();

    void punchReminder(String ruleId);

    void sendMsg(Set<String> mobile);
}
