package com.crlink.smartpark.common.mapper;

import com.crlink.smartpark.common.dto.MultistageCategoryRelationDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryRelationQueryDTO;
import com.crlink.smartpark.common.entity.MultistageCategoryRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 多级分类关联表 Mapper 接口
 * </p>
 * */
public interface MultistageCategoryRelationMapper extends BaseMapper<MultistageCategoryRelation> {

    List<MultistageCategoryRelationDTO> listBusinessToNCategoryRelation(MultistageCategoryRelationQueryDTO reqDTO);
}
