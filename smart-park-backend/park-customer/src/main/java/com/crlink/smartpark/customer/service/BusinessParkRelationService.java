package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.dto.BusinessParkRelationDTO;
import com.crlink.smartpark.customer.entity.BusinessParkRelation;
import com.crlink.smartpark.customer.model.query.BusinessParkRelationQuery;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业务和园区多对多关系表 服务类
 * </p>
 * */
public interface BusinessParkRelationService extends IService<BusinessParkRelation> {


    Map<String, Set<String>> parkOrgIds2BusinessIds(Collection<String> parkOrgIds, Integer type);

    Map<String, Set<String>> businessIds2ParkOrgIds(Collection<String> businessIds, Integer type);

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    BusinessParkRelationDTO saveOrUpdate(BusinessParkRelationDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    BusinessParkRelationDTO getBusinessParkRelationById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<BusinessParkRelationDTO> listPage(BusinessParkRelationQuery condition);
}
