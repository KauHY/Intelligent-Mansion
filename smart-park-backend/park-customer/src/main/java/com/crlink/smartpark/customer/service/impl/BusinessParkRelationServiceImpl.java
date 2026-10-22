package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.customer.dto.BusinessParkRelationDTO;
import com.crlink.smartpark.customer.entity.BusinessParkRelation;
import com.crlink.smartpark.customer.mapper.BusinessParkRelationMapper;
import com.crlink.smartpark.customer.model.query.BusinessParkRelationQuery;
import com.crlink.smartpark.customer.service.BusinessParkRelationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务和园区多对多关系表 服务实现类
 * </p>
 * */
@Service
public class BusinessParkRelationServiceImpl extends ServiceImpl<BusinessParkRelationMapper, BusinessParkRelation> implements BusinessParkRelationService {

    @Override
    public Map<String, Set<String>> parkOrgIds2BusinessIds(Collection<String> parkOrgIds, Integer type) {
        if (ObjectUtil.isEmpty(parkOrgIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> result = this.lambdaQuery().select(BusinessParkRelation::getBusinessId, BusinessParkRelation::getParkOrgId)
                .eq(BusinessParkRelation::getBusinessType, type)
                .in(BusinessParkRelation::getParkOrgId, parkOrgIds).list()
                .parallelStream().collect(Collectors.groupingBy(BusinessParkRelation::getParkOrgId,
                        Collectors.mapping(BusinessParkRelation::getBusinessId, Collectors.toSet())));
        return result;
    }


    @Override
    public Map<String, Set<String>> businessIds2ParkOrgIds(Collection<String> businessIds, Integer type) {
        if (ObjectUtil.isEmpty(businessIds)) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> result = this.lambdaQuery().select(BusinessParkRelation::getBusinessId, BusinessParkRelation::getParkOrgId)
                .eq(BusinessParkRelation::getBusinessType, type)
                .in(BusinessParkRelation::getBusinessId, businessIds).list()
                .parallelStream().collect(Collectors.groupingBy(BusinessParkRelation::getBusinessId,
                        Collectors.mapping(BusinessParkRelation::getParkOrgId, Collectors.toSet())));
        return result;
    }

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 存储对象
     */
    @Override
    public BusinessParkRelationDTO saveOrUpdate(BusinessParkRelationDTO dto) {
        if (dto != null) {
            BusinessParkRelation entity = new BusinessParkRelation();
            BeanUtils.copyProperties(dto, entity);
            if (saveOrUpdate(entity)) {
                dto.setId(entity.getId());
            }
        }
        return dto;
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public BusinessParkRelationDTO getBusinessParkRelationById(String id) {
        if (StringUtils.isNotBlank(id)) {
            BusinessParkRelation entity = getById(id);
            if (entity != null) {
                BusinessParkRelationDTO dto = new BusinessParkRelationDTO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<BusinessParkRelationDTO> listPage(BusinessParkRelationQuery condition) {
        PageInfo<BusinessParkRelationDTO> pageResult = new PageInfo<BusinessParkRelationDTO>();
        if (condition != null) {
            Page<BusinessParkRelation> page;
            if (condition.getPageSize() != -1) {
                page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
            } else {
                page = new Page<BusinessParkRelation>();
            }
            PageHelper.orderBy("create_time desc");
            baseMapper.selectList(getConditionWrapper(condition));
            PageInfo<BusinessParkRelation> pageInfo = new PageInfo<BusinessParkRelation>(page);
            List<BusinessParkRelationDTO> dataList = listEntityToDto(pageInfo.getList());
            BeanUtils.copyProperties(pageInfo, pageResult, "list");
            pageResult.setList(dataList);
        }
        return pageResult;
    }

    /**
     * 封装查询条件
     *
     * @param condition 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<BusinessParkRelation> getConditionWrapper(BusinessParkRelationQuery condition) {
        LambdaQueryWrapper<BusinessParkRelation> queryWrapper = new LambdaQueryWrapper<BusinessParkRelation>();
        if (condition != null) {
            if (CollectionUtils.isNotEmpty(condition.getIds())) {
                queryWrapper = queryWrapper.in(BusinessParkRelation::getId, condition.getIds());
            }
//            if (condition.getStartTime() != null) {
//                queryWrapper = queryWrapper.ge(BusinessParkRelation::getCreateTime, condition.getStartTime());
//            }
//            if (condition.getEndTime() != null) {
//                queryWrapper = queryWrapper.le(BusinessParkRelation::getCreateTime, condition.getEndTime());
//            }
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<BusinessParkRelationDTO> listEntityToDto(List<BusinessParkRelation> list) {
        return list.stream().map(entity -> {
            BusinessParkRelationDTO temp = new BusinessParkRelationDTO();
            BeanUtils.copyProperties(entity, temp);
            return temp;
        }).collect(Collectors.toList());
    }
}
