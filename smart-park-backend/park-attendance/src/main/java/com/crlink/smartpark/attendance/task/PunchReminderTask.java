package com.crlink.smartpark.attendance.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crlink.smartpark.attendance.entity.*;
import com.crlink.smartpark.attendance.service.*;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.schedule.CronTaskRegistrar;
import com.crlink.smartpark.core.schedule.TaskExecutorUtils;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PunchReminderTask {

    @Resource
    private RedisUtil redisUtil;

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

    public static final String PREFIX = "punchReminder";

    /**
     * 生成打卡提醒定时任务
     */
    public void generatePunchReminder() {
        List<AttendanceRule> allCompanyIdsWithRule = attendanceRuleService.getAllEnableRules();
        LocalDate today = LocalDate.now();
        boolean workDay = HolidayUtil.isWorkDay(today);
        String cronSuffix = " " + today.getDayOfMonth() + " " + today.getMonthValue() + " ?";
        for (AttendanceRule attendanceRule : allCompanyIdsWithRule) {

            //判断目标天是否需要打卡
            boolean curDayNeedPunch;
            if (DictUtil.getValue(DictConstant.AttendanceWorkdayType.LEGAL).equals(attendanceRule.getWorkdayType())) {
                curDayNeedPunch = workDay;
            } else {
                //自定义星期天
                List<Integer> days = attendanceRule.getDaysOfWork();
                curDayNeedPunch = CollUtil.isNotEmpty(days) && days.contains(today.getDayOfWeek().getValue());
            }
            if (!curDayNeedPunch) {
                continue;
            }

            LocalTime punchInTime = attendanceRule.getPunchInTime();
            LocalTime punchOutTime = attendanceRule.getPunchOutTime();
            LocalTime now = LocalTime.now();
            long inSeconds = ChronoUnit.SECONDS.between(now , punchInTime);
            long outSeconds = ChronoUnit.SECONDS.between(now , punchOutTime);
            if (inSeconds > 0) {
                redisUtil.setObject(Constants.Redis.PUNCH_REMINDER_IN_PREFIX.getBuildCode(attendanceRule.getId()), attendanceRule, inSeconds, TimeUnit.SECONDS);
            }
            if (outSeconds > 0) {
                redisUtil.setObject(Constants.Redis.PUNCH_REMINDER_OUT_PREFIX.getBuildCode(attendanceRule.getId()), attendanceRule, outSeconds, TimeUnit.SECONDS);
            }
        }
    }

}
