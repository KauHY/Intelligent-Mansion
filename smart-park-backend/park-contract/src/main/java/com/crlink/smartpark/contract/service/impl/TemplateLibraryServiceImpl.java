package com.crlink.smartpark.contract.service.impl;

import com.crlink.smartpark.contract.entity.TemplateLibrary;
import com.crlink.smartpark.contract.model.query.TemplateLibraryQuery;
import com.crlink.smartpark.contract.model.TemplateLibraryReqDTO;
import com.crlink.smartpark.contract.dto.TemplateLibraryDTO;
import com.crlink.smartpark.contract.service.TemplateLibraryService;
import com.crlink.smartpark.contract.mapper.TemplateLibraryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SortDictVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 模板字段表 服务实现类
 * */
@Service
public class TemplateLibraryServiceImpl extends ServiceImpl<TemplateLibraryMapper, TemplateLibrary>
    implements TemplateLibraryService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<TemplateLibrary, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<TemplateLibrary> lambdaQuery = this.lambdaQuery().in(TemplateLibrary::getId, ids);
        if (flag) {
            lambdaQuery.select(TemplateLibrary::getId, sValue);
        }
        Stream<TemplateLibrary> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(TemplateLibrary::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(TemplateLibrary::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateTemplateLibrary(TemplateLibraryReqDTO reqDTO) {
        TemplateLibrary entity = MyBeanUtils.copy(reqDTO, TemplateLibrary::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     */
    @Override
    public TemplateLibraryDTO getTemplateLibraryById(String id) {
        TemplateLibrary entity =
            Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<TemplateLibraryDTO> listTemplateLibraryPage(TemplateLibraryQuery query) {
        query.startPage();
        List<TemplateLibrary> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<TemplateLibraryDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Map<String, Object> findByType(String type) {
        Map<String, Object> result = Maps.newLinkedHashMap();
        List<TemplateLibrary> list = this.lambdaQuery().eq(TemplateLibrary::getGroupIds, type).list();
        // Map<String, List<TemplateLibrary>> typeGroup =
        // list.parallelStream().collect(Collectors.groupingBy(TemplateLibrary::getParentId, LinkedHashMap::new,
        // Collectors.toList()));
        Map<String,
            List<DictVO>> valueMap = list.parallelStream().sorted(Comparator.comparing(TemplateLibrary::getSort)).collect(Collectors.groupingBy(TemplateLibrary::getParentId,
                LinkedHashMap::new,
                Collectors.mapping(t -> new DictVO(t.getLabel(), t.getValue()), Collectors.toList())));
        result.putAll(valueMap);
        return result;
    }

    @Override
    public List<TemplateLibraryDTO> listTemplateLibraryByType(String type) {
        List<TemplateLibrary> list = this.lambdaQuery().eq(TemplateLibrary::getGroupIds, type).list();
        return entitiesToDTOs(list);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<TemplateLibrary> getConditionWrapper(TemplateLibraryQuery query) {
        LambdaQueryWrapper<TemplateLibrary> queryWrapper = new LambdaQueryWrapper<TemplateLibrary>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * 
     * @return DTO集合
     */
    private List<TemplateLibraryDTO> entitiesToDTOs(List<TemplateLibrary> entities) {
        return MyBeanUtils.copyList(entities, TemplateLibraryDTO::new, (s, t) -> {

        });
    }
}
