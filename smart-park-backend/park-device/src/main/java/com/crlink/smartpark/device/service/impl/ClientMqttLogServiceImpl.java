package com.crlink.smartpark.device.service.impl;

import com.crlink.smartpark.device.entity.ClientMqttLog;
import com.crlink.smartpark.device.model.query.ClientMqttLogQuery;
import com.crlink.smartpark.device.model.ClientMqttLogReqDTO;
import com.crlink.smartpark.device.dto.ClientMqttLogDTO;
import com.crlink.smartpark.device.service.ClientMqttLogService;
import com.crlink.smartpark.device.mapper.ClientMqttLogMapper;
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
 * mqtt日志记录 服务实现类
 * */
@Service
public class ClientMqttLogServiceImpl extends ServiceImpl<ClientMqttLogMapper, ClientMqttLog> implements ClientMqttLogService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ClientMqttLog, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ClientMqttLog> lambdaQuery = this.lambdaQuery().in(ClientMqttLog::getId, ids);
        if (flag) {
            lambdaQuery.select(ClientMqttLog::getId, sValue);
        }
        Stream<ClientMqttLog> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ClientMqttLog::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ClientMqttLog::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateClientMqttLog(ClientMqttLogReqDTO reqDTO) {
        ClientMqttLog entity = MyBeanUtils.copy(reqDTO, ClientMqttLog::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ClientMqttLogDTO getClientMqttLogById(String id) {
        ClientMqttLog entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<ClientMqttLogDTO> listClientMqttLogPage(ClientMqttLogQuery query) {
        query.startPage();
        List<ClientMqttLog> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<ClientMqttLogDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ClientMqttLog> getConditionWrapper(ClientMqttLogQuery query) {
        LambdaQueryWrapper<ClientMqttLog> queryWrapper = new LambdaQueryWrapper<ClientMqttLog>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ClientMqttLogDTO> entitiesToDTOs(List<ClientMqttLog> entities) {
        return MyBeanUtils.copyList(entities, ClientMqttLogDTO::new, (s, t) -> {

        });
    }

    @Override
    public void saveMqttLog(String topic, String msg, Integer type) {
        ClientMqttLog clientMqttLog = new ClientMqttLog();
        clientMqttLog.setTopic(topic);
        clientMqttLog.setMsg(msg);
        clientMqttLog.setType(type);
        save(clientMqttLog);
    }
}
