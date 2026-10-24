package com.crlink.smartpark.device.service.impl;

import com.crlink.smartpark.device.entity.ClientDevice;
import com.crlink.smartpark.device.model.query.ClientDeviceQuery;
import com.crlink.smartpark.device.model.ClientDeviceReqDTO;
import com.crlink.smartpark.device.dto.ClientDeviceDTO;
import com.crlink.smartpark.device.service.ClientDeviceService;
import com.crlink.smartpark.device.mapper.ClientDeviceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.EntityUserUtil;
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
 * 设备管理 服务实现类
 * */
@Service
public class ClientDeviceServiceImpl extends ServiceImpl<ClientDeviceMapper, ClientDevice> implements ClientDeviceService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ClientDevice, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ClientDevice> lambdaQuery = this.lambdaQuery().in(ClientDevice::getId, ids);
        if (flag) {
            lambdaQuery.select(ClientDevice::getId, sValue);
        }
        Stream<ClientDevice> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ClientDevice::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ClientDevice::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateClientDevice(ClientDeviceReqDTO reqDTO) {
        ClientDevice entity = MyBeanUtils.copy(reqDTO, ClientDevice::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ClientDeviceDTO getClientDeviceById(String id) {
        ClientDevice entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<ClientDeviceDTO> listClientDevicePage(ClientDeviceQuery query) {
        query.startPage();
        List<ClientDevice> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<ClientDeviceDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ClientDevice> getConditionWrapper(ClientDeviceQuery query) {
        LambdaQueryWrapper<ClientDevice> queryWrapper = new LambdaQueryWrapper<ClientDevice>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ClientDeviceDTO> entitiesToDTOs(List<ClientDevice> entities) {
        return MyBeanUtils.copyList(entities, ClientDeviceDTO::new, (s, t) -> {

        });
    }
}
