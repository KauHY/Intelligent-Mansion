package com.crlink.smartpark.attendance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.dto.AttendanceRecordDTO;
import com.crlink.smartpark.attendance.entity.AttendanceRecord;
import com.crlink.smartpark.attendance.model.AttendanceRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceRecordQuery;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 员工考勤记录 服务类
 * </p>
 * */
public interface AttendanceRecordService extends IService<AttendanceRecord> {

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateAttendanceRecord(AttendanceRecordReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    AttendanceRecordDTO getAttendanceRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<AttendanceRecordDTO> listAttendanceRecordPage(AttendanceRecordQuery query);

    void export(AttendanceRecordQuery query);
}
