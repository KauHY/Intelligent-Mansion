package com.crlink.smartpark.enterprise.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.enterprise.entity.SupplyService;
import com.crlink.smartpark.enterprise.model.StateReqDTO;
import com.crlink.smartpark.enterprise.model.query.SupplyServiceQuery;
import com.crlink.smartpark.enterprise.model.SupplyServiceReqDTO;
import com.crlink.smartpark.industrial.dto.SupplyServiceDTO;
import com.crlink.smartpark.enterprise.service.SupplyServiceService;
import com.crlink.smartpark.enterprise.mapper.SupplyServiceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
 * 供应服务 服务实现类
 * */
@Service
public class SupplyServiceServiceImpl extends ServiceImpl<SupplyServiceMapper, SupplyService> implements SupplyServiceService {

    @Autowired
    private CompanyProvider companyProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<SupplyService, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<SupplyService> lambdaQuery = this.lambdaQuery().in(SupplyService::getId, ids);
        if (flag) {
            lambdaQuery.select(SupplyService::getId, sValue);
        }
        Stream<SupplyService> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(SupplyService::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(SupplyService::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateSupplyService(SupplyServiceReqDTO reqDTO) {
        SupplyService entity = MyBeanUtils.copy(reqDTO, SupplyService::new);
        EntityUserUtil.setAllDynamic(entity);
        if (StringUtils.isBlank(entity.getId())) {
            entity.setSource(Constants.USER_BUSINESS_SOURCE.equals(SystemUtil.getInstance().getSource()) ?
                    DictConstant.AddDataSource.ENTERPRISE_ADDED.getCode() : DictConstant.AddDataSource.ADMIN_ADDED.getCode());
        }
        if (Constants.USER_BUSINESS_SOURCE.equals(SystemUtil.getInstance().getSource())) {
            ResponseResult<CompanyVO> responseResult = companyProvider.findById(reqDTO.getCompanyId());
            CompanyVO companyVO = responseResult.getData();
            if (companyVO != null) {
                if (companyVO.getStatus().equals(Constants.ZERO)) {
                    throw new ResolinkRuntimeException("700007");
                }
            }
        }
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public SupplyServiceDTO getSupplyServiceById(String id) {
        SupplyService entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Id2objHandle
    @Override
    public PageInfo<SupplyServiceDTO> listSupplyServicePage(SupplyServiceQuery query) {
        query.startPage();
        List<SupplyService> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<SupplyServiceDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean changeState(StateReqDTO reqDTO) {
        SupplyService copy = MyBeanUtils.copy(reqDTO, SupplyService::new);
        if (DictConstant.SupplyPublishState.PUBLISHED.getCode() == reqDTO.getState()) {
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
    private LambdaQueryWrapper<SupplyService> getConditionWrapper(SupplyServiceQuery query) {
        LambdaQueryWrapper<SupplyService> queryWrapper = new LambdaQueryWrapper<SupplyService>();
        queryWrapper
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), SupplyService::getParkOrgId, query.getParkOrgId())
                .like(StringUtils.isNotBlank(query.getName()), SupplyService::getName, query.getName())
                .eq(Objects.nonNull(query.getSupplyType()), SupplyService::getSupplyType, query.getSupplyType())
                .eq(Constants.USER_BUSINESS_SOURCE.equals(SystemUtil.getInstance().getSource()), SupplyService::getCreateBy, SystemUtil.getInstance().getCurrentUserId())
                .eq(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), SupplyService::getState, DictConstant.SupplyPublishState.PUBLISHED.getCode())
                .orderByDesc(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), SupplyService::getPublishTime)
                .orderByDesc(SupplyService::getUpdateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<SupplyServiceDTO> entitiesToDTOs(List<SupplyService> entities) {
        return MyBeanUtils.copyList(entities, SupplyServiceDTO::new, (s, t) -> {

        });
    }
}
