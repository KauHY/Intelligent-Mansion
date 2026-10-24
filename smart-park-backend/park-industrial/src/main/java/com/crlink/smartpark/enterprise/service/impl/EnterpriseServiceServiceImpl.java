package com.crlink.smartpark.enterprise.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.enterprise.entity.EnterpriseService;
import com.crlink.smartpark.enterprise.model.StateReqDTO;
import com.crlink.smartpark.enterprise.model.query.EnterpriseServiceQuery;
import com.crlink.smartpark.enterprise.model.EnterpriseServiceReqDTO;
import com.crlink.smartpark.industrial.dto.EnterpriseServiceDTO;
import com.crlink.smartpark.enterprise.service.EnterpriseServiceService;
import com.crlink.smartpark.enterprise.mapper.EnterpriseServiceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.time.LocalDateTime;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 企业服务 服务实现类
 * */
@Service
public class EnterpriseServiceServiceImpl extends ServiceImpl<EnterpriseServiceMapper, EnterpriseService> implements EnterpriseServiceService {

    @Autowired
    private CommonProvider commonProvider;
    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<EnterpriseService, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<EnterpriseService> lambdaQuery = this.lambdaQuery().in(EnterpriseService::getId, ids);
        if (flag) {
            lambdaQuery.select(EnterpriseService::getId, sValue);
        }
        Stream<EnterpriseService> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(EnterpriseService::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(EnterpriseService::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateEnterpriseService(EnterpriseServiceReqDTO reqDTO) {

        if (StringUtils.isNotBlank(reqDTO.getId())) {
            EnterpriseService enterpriseService = Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            AssertUtil.requireTrue(enterpriseService.getName().equals(reqDTO.getName())|| this.count(Wrappers.<EnterpriseService>query().lambda()
                    .eq(EnterpriseService::getName, reqDTO.getName())) <= 0, "700001");
        }else {
            AssertUtil.requireTrue(this.count(Wrappers.<EnterpriseService>query().lambda()
                    .eq(EnterpriseService::getName, reqDTO.getName())) <= 0, "700001");
        }
        EnterpriseService entity = MyBeanUtils.copy(reqDTO, EnterpriseService::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public EnterpriseServiceDTO getEnterpriseServiceById(String id) {
        EnterpriseService entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<EnterpriseServiceDTO> listEnterpriseServicePage(EnterpriseServiceQuery query) {
        query.startPage();
        List<EnterpriseService> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<EnterpriseServiceDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean changeState(StateReqDTO reqDTO) {
        EnterpriseService copy = MyBeanUtils.copy(reqDTO, EnterpriseService::new);
        if (DictConstant.EnterprisePublishState.PUBLISHED.getCode() == reqDTO.getState()) {
            copy.setPublishTime(LocalDateTime.now());
        }
        EntityUserUtil.setUpdateBy(copy);
        return this.updateById(copy);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<EnterpriseService> getConditionWrapper(EnterpriseServiceQuery query) {
        LambdaQueryWrapper<EnterpriseService> queryWrapper = new LambdaQueryWrapper<EnterpriseService>();
        queryWrapper.like(StringUtils.isNotBlank(query.getName()), EnterpriseService::getName, query.getName())
                .apply(StringUtils.isNotBlank(query.getParkOrgId()) ,"JSON_CONTAINS(push_range->'$[*]', '\""+ query.getParkOrgId() +"\"', '$')")
                .eq(Objects.nonNull(query.getGroupType()), EnterpriseService::getGroupType, query.getGroupType())
                .eq(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), EnterpriseService::getState, DictConstant.EnterprisePublishState.PUBLISHED.getCode())
                .orderByDesc(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), EnterpriseService::getPublishTime)
                .orderByDesc(EnterpriseService::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<EnterpriseServiceDTO> entitiesToDTOs(List<EnterpriseService> entities) {
        Set<String> parkOrgIds = Sets.newHashSet();
        for (EnterpriseService entity : entities) {
            parkOrgIds.addAll(entity.getPushRange());
        }
        Map<String, String> namesByIdsAndType = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        return MyBeanUtils.copyList(entities, EnterpriseServiceDTO::new, (s, t) -> {
            String pushRangeName = "";
            for (String parkOrgId : s.getPushRange()) {
                pushRangeName += namesByIdsAndType.get(parkOrgId) + ";";
            }
            t.setPushRangeName(pushRangeName);
        });
    }
}
