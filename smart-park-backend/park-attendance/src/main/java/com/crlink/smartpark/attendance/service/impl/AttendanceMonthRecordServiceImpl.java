package com.crlink.smartpark.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.dto.AttendanceMonthRecordDTO;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordExportDTO;
import com.crlink.smartpark.attendance.entity.AttendanceMonthRecord;
import com.crlink.smartpark.attendance.mapper.AttendanceMonthRecordMapper;
import com.crlink.smartpark.attendance.model.AttendanceMonthRecordReqDTO;
import com.crlink.smartpark.attendance.model.query.AttendanceMonthRecordQuery;
import com.crlink.smartpark.attendance.service.AttendanceMonthRecordService;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 考勤月度统计 服务实现类
 * */
@Service
public class AttendanceMonthRecordServiceImpl extends ServiceImpl<AttendanceMonthRecordMapper, AttendanceMonthRecord> implements AttendanceMonthRecordService {

    public static final String FILE_NAME = "月度考勤";
    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<AttendanceMonthRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<AttendanceMonthRecord> lambdaQuery = this.lambdaQuery().in(AttendanceMonthRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(AttendanceMonthRecord::getId, sValue);
        }
        Stream<AttendanceMonthRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(AttendanceMonthRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(AttendanceMonthRecord::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateAttendanceMonthRecord(AttendanceMonthRecordReqDTO reqDTO) {
        AttendanceMonthRecord entity = MyBeanUtils.copy(reqDTO, AttendanceMonthRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public AttendanceMonthRecordDTO getAttendanceMonthRecordById(String id) {
        AttendanceMonthRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<AttendanceMonthRecordDTO> listAttendanceMonthRecordPage(AttendanceMonthRecordQuery query) {
        query.startPage();
        List<AttendanceMonthRecord> list = list(getConditionWrapper(query));
        List<AttendanceMonthRecordDTO> result = entitiesToDTOs(list);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(result);
        return pageInfo;
    }

    @Override
    public void export(AttendanceMonthRecordQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), this::entitiesToDTOs, FILE_NAME, AttendanceMonthRecordDTO.class);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<AttendanceMonthRecord> getConditionWrapper(AttendanceMonthRecordQuery query) {
        if (StrUtil.isNotBlank(query.getYearMonth())) {
            String[] split = query.getYearMonth().split("-");
            query.setYear(Integer.parseInt(split[0]));
            query.setMonth(Integer.parseInt(split[1]));
        }
        LambdaQueryWrapper<AttendanceMonthRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AttendanceMonthRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), AttendanceMonthRecord::getCompanyId, query.getCompanyId());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), AttendanceMonthRecord::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StrUtil.isNotBlank(query.getEmployeeId()), AttendanceMonthRecord::getEmployeeId, query.getEmployeeId());
        wrapper.eq(query.getMonth() != null, AttendanceMonthRecord::getMonth, query.getMonth());
        wrapper.eq(query.getYear() != null, AttendanceMonthRecord::getYear, query.getYear());
        wrapper.like(StrUtil.isNotBlank(query.getEmployeeName()), AttendanceMonthRecord::getEmployeeName, query.getEmployeeName());

        return wrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<AttendanceMonthRecordDTO> entitiesToDTOs(List<AttendanceMonthRecord> entities) {
        return MyBeanUtils.copyList(entities, AttendanceMonthRecordDTO::new, (s, t) -> {

        });
    }
}
