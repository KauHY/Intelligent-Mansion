package com.crlink.smartpark.attendance.service;

import com.crlink.smartpark.attendance.entity.AttendanceMonthRecord;
import com.crlink.smartpark.attendance.dto.AttendanceMonthRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.model.AttendanceMonthRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceMonthRecordQuery;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 考勤月度统计 服务类
 * </p>
 * */
public interface AttendanceMonthRecordService extends IService<AttendanceMonthRecord> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<AttendanceMonthRecord, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateAttendanceMonthRecord(AttendanceMonthRecordReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    AttendanceMonthRecordDTO getAttendanceMonthRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<AttendanceMonthRecordDTO> listAttendanceMonthRecordPage(AttendanceMonthRecordQuery query);

    void export(AttendanceMonthRecordQuery query);
}
