package com.crlink.smartpark.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.dto.AttendanceRecordDTO;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordExportDTO;
import com.crlink.smartpark.attendance.entity.AttendanceMonthRecord;
import com.crlink.smartpark.attendance.entity.AttendanceRecord;
import com.crlink.smartpark.attendance.mapper.AttendanceRecordMapper;
import com.crlink.smartpark.attendance.model.AttendanceRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceRecordQuery;
import com.crlink.smartpark.attendance.model.vo.AttendanceRecordDateVO;
import com.crlink.smartpark.attendance.service.AttendanceRecordService;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * 员工考勤记录 服务实现类
 * */
@Service
public class AttendanceRecordServiceImpl extends ServiceImpl<AttendanceRecordMapper, AttendanceRecord> implements AttendanceRecordService {

    public static final String FILE_NAME = "每日考勤";
    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateAttendanceRecord(AttendanceRecordReqDTO reqDTO) {
        AttendanceRecord entity = MyBeanUtils.copy(reqDTO, AttendanceRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public AttendanceRecordDTO getAttendanceRecordById(String id) {
        AttendanceRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<AttendanceRecordDTO> listAttendanceRecordPage(AttendanceRecordQuery query) {
        query.startPage();
        List<AttendanceRecord> list = list(getConditionWrapper(query));
        List<AttendanceRecordDTO> result = entitiesToDTOs(list);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(result);
        return pageInfo;
    }

    @Override
    public void export(AttendanceRecordQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), this::entitiesToDTOs, FILE_NAME, AttendanceRecordDTO.class);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<AttendanceRecord> getConditionWrapper(AttendanceRecordQuery query) {
        LambdaQueryWrapper<AttendanceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AttendanceRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(StrUtil.isNotBlank(query.getEmployeeId()), AttendanceRecord::getEmployeeId, query.getEmployeeId());
        wrapper.eq(query.getDate() != null, AttendanceRecord::getDate, query.getDate());
        wrapper.eq(query.getAttendanceResult() != null, AttendanceRecord::getAttendanceResult, query.getAttendanceResult());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), AttendanceRecord::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), AttendanceRecord::getCompanyId, query.getCompanyId());
        wrapper.eq(query.getPunchType() != null, AttendanceRecord::getPunchType, query.getPunchType());
        wrapper.like(StrUtil.isNotBlank(query.getEmployeeName()), AttendanceRecord::getEmployeeName, query.getEmployeeName());
        wrapper.ge(Objects.nonNull(query.getPunchInTime()), AttendanceRecord::getPunchInTime, query.getPunchInTime());
        wrapper.le(Objects.nonNull(query.getPunchOutTime()), AttendanceRecord::getPunchOutTime, query.getPunchOutTime());

        return wrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<AttendanceRecordDTO> entitiesToDTOs(List<AttendanceRecord> entities) {
        return MyBeanUtils.copyList(entities, AttendanceRecordDTO::new, (s, t) -> {

        });
    }
}
