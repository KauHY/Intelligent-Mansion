package com.crlink.smartpark.attendance.service;

import com.crlink.smartpark.attendance.dto.AttendanceRuleDTO;
import com.crlink.smartpark.attendance.entity.AttendanceRule;
import com.crlink.smartpark.attendance.entity.PunchRecord;
import com.crlink.smartpark.attendance.dto.PunchRecordDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.attendance.model.PunchRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.PunchRecordQuery;
import com.crlink.smartpark.attendance.model.vo.AttendanceRecordDateVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 打卡记录 服务类
 * </p>
 * */
public interface PunchRecordService extends IService<PunchRecord> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<PunchRecord, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdatePunchRecord(PunchRecordReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    PunchRecordDTO getPunchRecordById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<PunchRecordDTO> listPunchRecordPage(PunchRecordQuery query);

    /**
     * 查询某月每天打卡记录
     * @param query
     * @return
     */
    Map<LocalDate, List<PunchRecord>> listMonthDetail(PunchRecordQuery query);

    void export(PunchRecordQuery query);

    List<SimpleNode> getCompanySelectWithRule();

    List<PunchRecord> getTodayRecord();

    Boolean hasRule();

    AttendanceRule getRule();

    PunchRecordReqDTO wrapReq(PunchRecordReqDTO recordReqDTO);

    AttendanceRecordDateVO getRecordsByDate(String date);

}
