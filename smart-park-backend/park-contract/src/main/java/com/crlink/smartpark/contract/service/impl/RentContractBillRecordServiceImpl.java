package com.crlink.smartpark.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.RentContractBillRecordDTO;
import com.crlink.smartpark.contract.entity.RentContractBillRecord;
import com.crlink.smartpark.contract.mapper.RentContractBillRecordMapper;
import com.crlink.smartpark.contract.model.request.RentContractBillRecordReqDTO;
import com.crlink.smartpark.contract.model.query.RentContractBillRecordQuery;
import com.crlink.smartpark.contract.service.RentContractBillRecordService;
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
public class RentContractBillRecordServiceImpl extends ServiceImpl<RentContractBillRecordMapper, RentContractBillRecord> implements RentContractBillRecordService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<RentContractBillRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<RentContractBillRecord> lambdaQuery = this.lambdaQuery().in(RentContractBillRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(RentContractBillRecord::getId, sValue);
        }
        Stream<RentContractBillRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(RentContractBillRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(RentContractBillRecord::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateRentContractBillRecord(RentContractBillRecordReqDTO reqDTO) {
        RentContractBillRecord entity = MyBeanUtils.copy(reqDTO, RentContractBillRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public RentContractBillRecordDTO getRentContractBillRecordById(String id) {
        RentContractBillRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<RentContractBillRecordDTO> listRentContractBillRecordPage(RentContractBillRecordQuery query) {
        query.startPage();
        List<RentContractBillRecord> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<RentContractBillRecordDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<RentContractBillRecord> getConditionWrapper(RentContractBillRecordQuery query) {
        LambdaQueryWrapper<RentContractBillRecord> queryWrapper = new LambdaQueryWrapper<RentContractBillRecord>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<RentContractBillRecordDTO> entitiesToDTOs(List<RentContractBillRecord> entities) {
        return MyBeanUtils.copyList(entities, RentContractBillRecordDTO::new, (s, t) -> {

        });
    }
}
