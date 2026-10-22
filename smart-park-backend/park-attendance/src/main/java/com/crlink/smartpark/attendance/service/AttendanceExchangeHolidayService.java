package com.crlink.smartpark.attendance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.dto.AttendanceExchangeHolidayDTO;
import com.crlink.smartpark.attendance.entity.AttendanceExchangeHoliday;

import java.util.List;

/**
 * <p>
 * 节假日调休表 服务类
 * </p>
 *
 */
public interface AttendanceExchangeHolidayService extends IService<AttendanceExchangeHoliday> {

    /**
     * 获取所有考勤规则
     *
     * @return
     */
    List<AttendanceExchangeHolidayDTO> getAllExchangeHolidays();
}
