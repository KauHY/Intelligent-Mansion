package com.crlink.smartpark.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.contract.entity.Contract;

import java.util.Set;

/**
 * <p>
 * 合同 Mapper 接口
 * </p>
 * */
public interface ContractMapper extends BaseMapper<Contract> {

    Set<String> queryEffectiveContractHouseIds();
    

}
