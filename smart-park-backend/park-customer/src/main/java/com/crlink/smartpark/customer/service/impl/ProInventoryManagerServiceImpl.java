package com.crlink.smartpark.customer.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.dto.ProStockManagerDTO;
import com.crlink.smartpark.customer.dto.ProStockRecordDTO;
import com.crlink.smartpark.customer.entity.ProInventoryManager;
import com.crlink.smartpark.customer.model.dto.ProInventoryManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProInventoryManagerDTO;
import com.crlink.smartpark.customer.model.excel.ProInventoryManagerExprotDTO;
import com.crlink.smartpark.customer.model.excel.ProStockRecordExportDTO;
import com.crlink.smartpark.customer.model.query.ProInventoryManagerQuery;
import com.crlink.smartpark.customer.service.ProInventoryManagerService;
import com.crlink.smartpark.customer.mapper.ProInventoryManagerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collections;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 盘点管理 服务实现类
 * */
@Service
public class ProInventoryManagerServiceImpl extends ServiceImpl<ProInventoryManagerMapper, ProInventoryManager>
    implements ProInventoryManagerService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProInventoryManager, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ProInventoryManager> lambdaQuery =
            this.lambdaQuery().in(ProInventoryManager::getId, ids);
        if (flag) {
            lambdaQuery.select(ProInventoryManager::getId, sValue);
        }
        Stream<ProInventoryManager> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ProInventoryManager::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ProInventoryManager::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProInventoryManager(ProInventoryManagerReqDTO reqDTO) {
        ProInventoryManager entity = MyBeanUtils.copy(reqDTO, ProInventoryManager::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     */
    @Override
    public ProInventoryManagerDTO getProInventoryManagerById(String id) {
        ProInventoryManager entity =
            Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ProInventoryManagerDTO> listProInventoryManagerPage(ProInventoryManagerQuery query) {
        query.startPage();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        List<ProInventoryManagerDTO> result =  baseMapper.listPage(query,parkOrgIds);
        MyPageInfoUtils.isEmptyPage(result);
        return query.createPageInfo(result);
    }

    @Override
    public void export(ProInventoryManagerQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        List<ProInventoryManagerDTO> result = baseMapper.listPage(query, parkOrgIds);
        ExcelExportUtils.export(() -> result, this::convertToExcel, Constant.INVENTORY_FILE_NAME, ProInventoryManagerExprotDTO.class);

    }

    private List<ProInventoryManagerExprotDTO> convertToExcel(List<ProInventoryManagerDTO> entities) {
        return MyBeanUtils.copyList(entities, ProInventoryManagerExprotDTO::new, (s, t) -> {

            t.setGoodsHouse(Constant.GOODS_HOUSE_NAME);

        });
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ProInventoryManager> getConditionWrapper(ProInventoryManagerQuery query) {
        LambdaQueryWrapper<ProInventoryManager> queryWrapper = new LambdaQueryWrapper<ProInventoryManager>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * 
     * @return DTO集合
     */
    private List<ProInventoryManagerDTO> entitiesToDTOs(List<ProInventoryManager> entities) {
        return MyBeanUtils.copyList(entities, ProInventoryManagerDTO::new, (s, t) -> {

        });
    }
}
