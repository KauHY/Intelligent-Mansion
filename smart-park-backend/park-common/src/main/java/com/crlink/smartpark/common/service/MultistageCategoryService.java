package com.crlink.smartpark.common.service;

import com.crlink.smartpark.common.entity.MultistageCategory;
import com.crlink.smartpark.common.model.MultistageCategoryUpdateReqDTO;
import com.crlink.smartpark.common.model.query.MultistageCategoryQuery;
import com.crlink.smartpark.common.model.MultistageCategoryReqDTO;
import com.crlink.smartpark.common.dto.MultistageCategoryDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.vo.TreeDictVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 多级分类表 服务类
 * </p>
 * */
public interface MultistageCategoryService extends IService<MultistageCategory> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MultistageCategory, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveMultistageCategory(MultistageCategoryReqDTO reqDTO);

    Boolean updateMultistageCategory(MultistageCategoryUpdateReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MultistageCategoryDTO getMultistageCategoryById(String id);

    TreeDictVO listMultistageCategoryTree(String parkOrgId, String categoryType);

    Set<String> getAllSubId(String id);

    Boolean removeCategoryAndSub(String id);

    Boolean hasSub(String id);

    String getTzIdBySecondName(String name, String parkOrgId);

    Boolean hasAssociate(String type, String id);
}
