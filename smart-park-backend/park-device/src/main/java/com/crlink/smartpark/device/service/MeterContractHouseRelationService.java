package com.crlink.smartpark.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.device.entity.MeterContractHouseRelation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 水电表合同房屋关系 服务类
 * </p>
 * */
public interface MeterContractHouseRelationService extends IService<MeterContractHouseRelation> {

    Map<String, Set<String>> houseId2MeterIds(Collection<String> houseIds, String settledMonth);

    Map<String, Set<String>> meterId2houseIds(Collection<String> meterIds, String settledMonth);
}
