package com.crlink.smartpark.device.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.dto.MeterWarningRecordDTO;
import com.crlink.smartpark.device.entity.MeterWarningRecord;
import com.crlink.smartpark.device.mapper.MeterWarningRecordMapper;
import com.crlink.smartpark.device.model.MeterWarningRecordReqDTO;
import com.crlink.smartpark.device.model.query.MeterWarningRecordQuery;
import com.crlink.smartpark.device.service.MeterWarningRecordService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 仪表异常告警记录 服务实现类
 * */
@Service
public class MeterWarningRecordServiceImpl extends ServiceImpl<MeterWarningRecordMapper, MeterWarningRecord> implements MeterWarningRecordService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterWarningRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeterWarningRecord> lambdaQuery = this.lambdaQuery().in(MeterWarningRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(MeterWarningRecord::getId, sValue);
        }
        Stream<MeterWarningRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeterWarningRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeterWarningRecord::getId, Function.identity()));
        }
        return result;
    }

    @Override
    public Integer countMeterWarning(String parkOrgId, LocalDateTime startTime, LocalDateTime endTime, Integer status) {
        return this.baseMapper.countMeterWarning(parkOrgId, startTime, endTime, status);
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeterWarningRecord(MeterWarningRecordReqDTO reqDTO) {
        MeterWarningRecord entity = MyBeanUtils.copy(reqDTO, MeterWarningRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeterWarningRecordDTO getMeterWarningRecordById(String id) {
        MeterWarningRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeterWarningRecordDTO> listMeterWarningRecordPage(MeterWarningRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        List<MeterWarningRecordDTO> result = baseMapper.listMeterWarningRecordPage(query);
        return query.createPageInfo(result);
    }

    @Override
    @Id2objHandle
    public PageInfo<MeterWarningRecordDTO> list3dMeterWarningRecordPage(MeterWarningRecordQuery query) {
        query.startPage();
        List<MeterWarningRecordDTO> result = baseMapper.listMeterWarningRecordPage(query);
        return query.createPageInfo(result);
    }


    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeterWarningRecordDTO> entitiesToDTOs(List<MeterWarningRecord> entities) {
        return MyBeanUtils.copyList(entities, MeterWarningRecordDTO::new, (s, t) -> {

        });
    }
}
