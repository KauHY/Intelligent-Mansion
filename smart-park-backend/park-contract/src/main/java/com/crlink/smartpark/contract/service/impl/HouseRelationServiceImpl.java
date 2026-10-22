package com.crlink.smartpark.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.contract.model.request.HouseRelationReqDTO;
import com.crlink.smartpark.contract.model.query.HouseRelationQuery;
import com.crlink.smartpark.contract.dto.HouseRelationDTO;
import com.crlink.smartpark.contract.entity.HouseRelation;
import com.crlink.smartpark.contract.mapper.HouseRelationMapper;
import com.crlink.smartpark.contract.service.HouseRelationService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同/计费房屋关联 服务实现类
 * </p>
 * */
@Service
public class HouseRelationServiceImpl extends ServiceImpl<HouseRelationMapper, HouseRelation> implements HouseRelationService {


    @Override
    public Map<String, List<String>> pkId2HouseRelationsMap(Collection<String> pkIds, Integer type) {
        if (ObjectUtils.isEmpty(pkIds)) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> result = this.lambdaQuery()
                .select(HouseRelation::getPkId, HouseRelation::getHouseId)
                .eq(Objects.nonNull(type), HouseRelation::getType, type)
                .in(HouseRelation::getPkId, pkIds).list()
                .parallelStream().collect(Collectors.groupingBy(HouseRelation::getPkId,
                        Collectors.mapping(HouseRelation::getHouseId, Collectors.toList())));
        return result;
    }

    /**
     * 保存或更新数据
     *
     * @param reqDTO 存储对象
     * @return 存储对象
     */
    @Override
    public Boolean saveOrUpdateHouseRelation(HouseRelationReqDTO reqDTO) {
        HouseRelation entity = MyBeanUtils.copy(reqDTO, HouseRelation::new);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public HouseRelationDTO getHouseRelationById(String id) {
        if (StringUtils.isNotBlank(id)) {
            HouseRelation entity = getById(id);
            if (entity != null) {
                HouseRelationDTO dto = new HouseRelationDTO();
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
    public PageInfo<HouseRelationDTO> listPage(HouseRelationQuery condition) {
        PageInfo<HouseRelationDTO> pageResult = new PageInfo<HouseRelationDTO>();
        if (condition != null) {
            Page<HouseRelation> page;
            if (condition.getPageSize() != -1) {
                page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
            } else {
                page = new Page<HouseRelation>();
            }
            PageHelper.orderBy("create_time desc");
            baseMapper.selectList(getConditionWrapper(condition));
            PageInfo<HouseRelation> pageInfo = new PageInfo<HouseRelation>(page);
            List<HouseRelationDTO> dataList = listEntityToDto(pageInfo.getList());
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
    private LambdaQueryWrapper<HouseRelation> getConditionWrapper(HouseRelationQuery condition) {
        LambdaQueryWrapper<HouseRelation> queryWrapper = new LambdaQueryWrapper<HouseRelation>();
        if (condition != null) {
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<HouseRelationDTO> listEntityToDto(List<HouseRelation> list) {
        return list.stream().map(entity -> {
            HouseRelationDTO temp = new HouseRelationDTO();
            BeanUtils.copyProperties(entity, temp);
            return temp;
        }).collect(Collectors.toList());
    }
}
