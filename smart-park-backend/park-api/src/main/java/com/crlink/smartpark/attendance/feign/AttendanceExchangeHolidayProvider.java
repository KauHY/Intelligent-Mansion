package com.crlink.smartpark.attendance.feign;

import com.crlink.smartpark.attendance.dto.AttendanceExchangeHolidayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 * 节假日调休 服务类
 * </p>
 *
 */
@FeignClient(name = "park-attendance", path = "/smartPark/park-attendance/attendance-exchange-holiday", contextId = "AttendanceExchangeHolidayProvider")
public interface AttendanceExchangeHolidayProvider {

    @PostMapping("/getAllExchangeHolidays")
    List<AttendanceExchangeHolidayDTO> getAllExchangeHolidays();

}
