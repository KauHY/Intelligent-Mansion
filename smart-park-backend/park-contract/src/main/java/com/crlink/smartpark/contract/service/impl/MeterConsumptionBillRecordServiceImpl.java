package com.crlink.smartpark.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.MeterConsumptionBillRecordDTO;
import com.crlink.smartpark.contract.entity.MeterConsumptionBillRecord;
import com.crlink.smartpark.contract.mapper.MeterConsumptionBillRecordMapper;
import com.crlink.smartpark.contract.model.MeterConsumptionBillRecordReqDTO;
import com.crlink.smartpark.contract.model.query.MeterConsumptionBillRecordQuery;
import com.crlink.smartpark.contract.service.MeterConsumptionBillRecordService;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 租赁合同账单减免记录 服务实现类
 * */
@Service
public class MeterConsumptionBillRecordServiceImpl extends ServiceImpl<MeterConsumptionBillRecordMapper, MeterConsumptionBillRecord> implements MeterConsumptionBillRecordService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionBillRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeterConsumptionBillRecord> lambdaQuery = this.lambdaQuery().in(MeterConsumptionBillRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(MeterConsumptionBillRecord::getId, sValue);
        }
        Stream<MeterConsumptionBillRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeterConsumptionBillRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeterConsumptionBillRecord::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeterConsumptionBillRecord(MeterConsumptionBillRecordReqDTO reqDTO) {
        MeterConsumptionBillRecord entity = MyBeanUtils.copy(reqDTO, MeterConsumptionBillRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeterConsumptionBillRecordDTO getMeterConsumptionBillRecordById(String id) {
        MeterConsumptionBillRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<MeterConsumptionBillRecordDTO> listMeterConsumptionBillRecordPage(MeterConsumptionBillRecordQuery query) {
        query.startPage();
        List<MeterConsumptionBillRecord> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<MeterConsumptionBillRecordDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeterConsumptionBillRecord> getConditionWrapper(MeterConsumptionBillRecordQuery query) {
        LambdaQueryWrapper<MeterConsumptionBillRecord> queryWrapper = new LambdaQueryWrapper<MeterConsumptionBillRecord>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeterConsumptionBillRecordDTO> entitiesToDTOs(List<MeterConsumptionBillRecord> entities) {
        return MyBeanUtils.copyList(entities, MeterConsumptionBillRecordDTO::new, (s, t) -> {

        });
    }
}
