package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.model.query.ApartmentPeopleHouseQuery;
import com.crlink.smartpark.contract.model.query.ContractVoListQuery;
import com.crlink.smartpark.contract.model.vo.ContractHouseVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName: ContractVoService
 * @Package com.crlink.smartpark.contract.service
 * @Description: 合同视图 服务类
 * @Version 1.0
 */
public interface ContractVoService extends IService<Contract> {
    /**
     * 根据查询参数 - 查询房屋合同状态视图
     *
     * @param query:
     * @return com.github.pagehelper.PageInfo<com.crlink.smartpark.contract.dto.ContractDTO>
     * @MethodName listPageWithHouseVO
     */
    PageInfo<ContractHouseVO> listPageWithHouseVO(ContractVoListQuery query);

    void export(ContractVoListQuery contractVoListQuery);

    PageInfo<ContractHouseVO> apartmentPeopleHouse(ApartmentPeopleHouseQuery query);

    /**
     * 根据园区获取已租的房屋id集合
     *
     * @param parkOrgId
     * @return
     */
    Map<Integer,Set<String>> getRentedHouseId(String parkOrgId);
}
