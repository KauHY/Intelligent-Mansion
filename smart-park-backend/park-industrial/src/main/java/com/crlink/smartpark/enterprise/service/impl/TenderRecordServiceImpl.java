package com.crlink.smartpark.enterprise.service.impl;

import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.enterprise.entity.TenderRecord;
import com.crlink.smartpark.enterprise.model.query.TenderRecordQuery;
import com.crlink.smartpark.enterprise.model.TenderRecordReqDTO;
import com.crlink.smartpark.industrial.dto.TenderRecordDTO;
import com.crlink.smartpark.enterprise.service.TenderRecordService;
import com.crlink.smartpark.enterprise.mapper.TenderRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.time.LocalDateTime;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 投标记录 服务实现类
 * */
@Service
public class TenderRecordServiceImpl extends ServiceImpl<TenderRecordMapper, TenderRecord> implements TenderRecordService {

    public static final String FILE_NAME = "投标信息详情";
    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<TenderRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<TenderRecord> lambdaQuery = this.lambdaQuery().in(TenderRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(TenderRecord::getId, sValue);
        }
        Stream<TenderRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(TenderRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(TenderRecord::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateTenderRecord(TenderRecordReqDTO reqDTO) {
        TenderRecord entity = MyBeanUtils.copy(reqDTO, TenderRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public TenderRecordDTO getTenderRecordById(String id) {
        TenderRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return MyBeanUtils.copy(entity, TenderRecordDTO::new);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<TenderRecordDTO> listTenderRecordPage(TenderRecordQuery query) {
        query.startPage();
        List<TenderRecord> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<TenderRecordDTO> result = MyBeanUtils.copyList(list, TenderRecordDTO::new);
        return query.createPageInfo(result);
    }

    @Override
    public void export(TenderRecordQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), this::entitiesToExportDTOs, FILE_NAME, TenderRecordDTO.class);
    }

    @Override
    public Boolean confirm(String id) {
        TenderRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        entity.setBiddingDocumentsPaid(DictConstant.Payment.PAID.getCode());
        entity.setMarginPaid(DictConstant.Contribute.PAID.getCode());
        EntityUserUtil.setUpdateBy(entity);
        entity.setUpdateTime(LocalDateTime.now());
        return updateById(entity);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<TenderRecord> getConditionWrapper(TenderRecordQuery query) {
        LambdaQueryWrapper<TenderRecord> queryWrapper = new LambdaQueryWrapper<TenderRecord>();
        queryWrapper.eq(TenderRecord::getPurchaseId, query.getPurchaseId())
                .orderByDesc(TenderRecord::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<TenderRecordDTO> entitiesToExportDTOs(List<TenderRecord> entities) {
        return MyBeanUtils.copyList(entities, TenderRecordDTO::new, (s, t) -> {
            t.setBiddingDocumentsPaidName(DictConstant.Payment.getPaymentByCode(s.getBiddingDocumentsPaid()));
            t.setMarginPaidName(DictConstant.Contribute.getContributeByCode(s.getMarginPaid()));
        });
    }
}
