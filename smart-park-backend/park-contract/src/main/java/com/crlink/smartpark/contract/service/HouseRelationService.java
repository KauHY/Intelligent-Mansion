package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.model.request.HouseRelationReqDTO;
import com.crlink.smartpark.contract.model.query.HouseRelationQuery;
import com.crlink.smartpark.contract.dto.HouseRelationDTO;
import com.crlink.smartpark.contract.entity.HouseRelation;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同/计费房屋关联 服务类
 * </p>
 * */
public interface HouseRelationService extends IService<HouseRelation> {


    Map<String, List<String>> pkId2HouseRelationsMap(Collection<String> pkIds, Integer type);

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean saveOrUpdateHouseRelation(HouseRelationReqDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    HouseRelationDTO getHouseRelationById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<HouseRelationDTO> listPage(HouseRelationQuery condition);

}
