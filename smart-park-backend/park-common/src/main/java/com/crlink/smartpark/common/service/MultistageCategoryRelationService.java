package com.crlink.smartpark.common.service;

import com.crlink.smartpark.common.dto.MultistageCategoryRelationQueryDTO;
import com.crlink.smartpark.common.entity.MultistageCategoryRelation;
import com.crlink.smartpark.common.model.MultistageCategoryRelationDelDTO;
import com.crlink.smartpark.common.model.query.MultistageCategoryRelationQuery;
import com.crlink.smartpark.common.model.MultistageCategoryRelationReqDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 多级分类关联表 服务类
 * </p>
 * */
public interface MultistageCategoryRelationService extends IService<MultistageCategoryRelation> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MultistageCategoryRelation, ?> sValue);


    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    MultistageCategoryRelationDTO getMultistageCategoryRelationById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MultistageCategoryRelationDTO> listMultistageCategoryRelationPage(MultistageCategoryRelationQuery query);

    Boolean associate(MultistageCategoryRelationReqDTO reqDTO);

    Boolean disassociate(MultistageCategoryRelationDelDTO reqDTO);

    Boolean hasAssociate(String categoryId);

    PageInfo<MultistageCategoryRelationDTO> listPageBusinessToCategoryRelation(MultistageCategoryRelationQueryDTO reqDTO);
    PageInfo<MultistageCategoryRelationDTO> listPageBusinessToNCategoryRelation(MultistageCategoryRelationQueryDTO reqDTO);

    MultistageCategoryRelationDTO getMultistageCategoryRelation(String businessId, String categoryId);

    Boolean saveOrUpdateMultistageCategoryRelation(MultistageCategoryRelationReqDTO reqDTO);

    MultistageCategoryRelationDTO getAllRelationByCategory(String categoryId);
}
