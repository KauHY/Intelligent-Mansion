package com.crlink.smartpark.device.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.device.entity.MeterContractHouseRelation;
import com.crlink.smartpark.device.mapper.MeterContractHouseRelationMapper;
import com.crlink.smartpark.device.service.MeterContractHouseRelationService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 水电表合同房屋关系 服务实现类
 * */
@Service
public class MeterContractHouseRelationServiceImpl extends ServiceImpl<MeterContractHouseRelationMapper, MeterContractHouseRelation> implements MeterContractHouseRelationService {

    @Override
    public Map<String, Set<String>> houseId2MeterIds(Collection<String> houseIds, @NotNull String settledMonth) {
        if (CollectionUtils.isEmpty(houseIds)) {
            return Collections.emptyMap();
        }
        List<MeterContractHouseRelation> list = this.lambdaQuery().select(MeterContractHouseRelation::getMeterId, MeterContractHouseRelation::getContractHouseId)
                .eq(MeterContractHouseRelation::getSettledMonth, settledMonth)
                .in(MeterContractHouseRelation::getContractHouseId, houseIds).list();
        Map<String, Set<String>> map = list.parallelStream().collect(Collectors.groupingBy(MeterContractHouseRelation::getContractHouseId,
                Collectors.mapping(MeterContractHouseRelation::getMeterId, Collectors.toSet())));
        return map;
    }

    @Override
    public Map<String, Set<String>> meterId2houseIds(Collection<String> meterIds, @NotNull String settledMonth) {
        if (CollectionUtils.isEmpty(meterIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> map = this.lambdaQuery().select(MeterContractHouseRelation::getMeterId, MeterContractHouseRelation::getContractHouseId)
                .eq(MeterContractHouseRelation::getSettledMonth, settledMonth)
                .in(MeterContractHouseRelation::getMeterId, meterIds).list()
                .parallelStream().collect(Collectors.groupingBy(MeterContractHouseRelation::getMeterId,
                        Collectors.mapping(MeterContractHouseRelation::getContractHouseId, Collectors.toSet())));
        return map;
    }
}
