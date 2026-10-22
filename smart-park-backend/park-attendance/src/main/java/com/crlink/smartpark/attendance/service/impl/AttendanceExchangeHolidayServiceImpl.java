package com.crlink.smartpark.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.dto.AttendanceExchangeHolidayDTO;
import com.crlink.smartpark.attendance.entity.AttendanceExchangeHoliday;
import com.crlink.smartpark.attendance.mapper.AttendanceExchangeHolidayMapper;
import com.crlink.smartpark.attendance.service.AttendanceExchangeHolidayService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 节假日调休表 服务实现类
 *
 */
@Service
public class AttendanceExchangeHolidayServiceImpl extends ServiceImpl<AttendanceExchangeHolidayMapper, AttendanceExchangeHoliday> implements AttendanceExchangeHolidayService {

    @Override
    public List<AttendanceExchangeHolidayDTO> getAllExchangeHolidays() {
        LambdaQueryWrapper<AttendanceExchangeHoliday> wrapper = new LambdaQueryWrapper<>();
        return entitiesToDTOs(list(wrapper));
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<AttendanceExchangeHolidayDTO> entitiesToDTOs(List<AttendanceExchangeHoliday> entities) {
        return MyBeanUtils.copyList(entities, AttendanceExchangeHolidayDTO::new, (s, t) -> {
        });
    }
}
