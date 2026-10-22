package com.crlink.smartpark.attendance.feign;

import com.crlink.smartpark.attendance.dto.AttendanceRuleDTO;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 * */
@FeignClient(name = "park-attendance", path = "/smartPark/park-attendance/attendance-rule", contextId = "AttendanceRuleProvider")
public interface AttendanceRuleProvider {

    @PostMapping("/punchReminder")
    void punchReminder(@RequestParam(value = "ruleId") String ruleId);

    @PostMapping("/getAllRules")
    List<AttendanceRuleDTO> getAllRules();

}
