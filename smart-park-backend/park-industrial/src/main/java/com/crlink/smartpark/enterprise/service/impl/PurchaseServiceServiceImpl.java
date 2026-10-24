package com.crlink.smartpark.enterprise.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.enterprise.entity.PurchaseService;
import com.crlink.smartpark.enterprise.entity.SupplyService;
import com.crlink.smartpark.enterprise.model.StateReqDTO;
import com.crlink.smartpark.enterprise.model.query.PurchaseServiceQuery;
import com.crlink.smartpark.enterprise.model.PurchaseServiceReqDTO;
import com.crlink.smartpark.industrial.dto.PurchaseServiceDTO;
import com.crlink.smartpark.enterprise.service.PurchaseServiceService;
import com.crlink.smartpark.enterprise.mapper.PurchaseServiceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
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
 * 采购服务 服务实现类
 * */
@Service
public class PurchaseServiceServiceImpl extends ServiceImpl<PurchaseServiceMapper, PurchaseService> implements PurchaseServiceService {

    @Autowired
    private CompanyProvider companyProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<PurchaseService, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<PurchaseService> lambdaQuery = this.lambdaQuery().in(PurchaseService::getId, ids);
        if (flag) {
            lambdaQuery.select(PurchaseService::getId, sValue);
        }
        Stream<PurchaseService> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(PurchaseService::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(PurchaseService::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdatePurchaseService(PurchaseServiceReqDTO reqDTO) {
        PurchaseService entity = MyBeanUtils.copy(reqDTO, PurchaseService::new);
        EntityUserUtil.setAllDynamic(entity);
        if (StringUtils.isBlank(entity.getId())) {
            entity.setSource(Constants.USER_BUSINESS_SOURCE.equals(SystemUtil.getInstance().getSource()) ?
                    DictConstant.AddDataSource.ENTERPRISE_ADDED.getCode() : DictConstant.AddDataSource.ADMIN_ADDED.getCode());
        } else {
            if (LocalDateTime.now().isBefore(entity.getEffectiveEndTime())) {
                entity.setState(Constants.TWO);
            }
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
    public PurchaseServiceDTO getPurchaseServiceById(String id) {
        PurchaseService entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<PurchaseServiceDTO> listPurchaseServicePage(PurchaseServiceQuery query) {
        query.startPage();
        List<PurchaseService> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<PurchaseServiceDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean changeState(StateReqDTO reqDTO) {
        PurchaseService copy = MyBeanUtils.copy(reqDTO, PurchaseService::new);
        if (DictConstant.PurchaseState.IN_PROGRESS.getCode() == reqDTO.getState()) {
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
    private LambdaQueryWrapper<PurchaseService> getConditionWrapper(PurchaseServiceQuery query) {
        LambdaQueryWrapper<PurchaseService> queryWrapper = new LambdaQueryWrapper<PurchaseService>();
        queryWrapper
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), PurchaseService::getParkOrgId, query.getParkOrgId())
                .like(StringUtils.isNotBlank(query.getName()), PurchaseService::getName, query.getName())
                .eq(Constants.USER_BUSINESS_SOURCE.equals(SystemUtil.getInstance().getSource()), PurchaseService::getCreateBy, SystemUtil.getInstance().getCurrentUserId())
                .in(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), PurchaseService::getState, Sets.newHashSet(DictConstant.PurchaseState.IN_PROGRESS.getCode(),
                        DictConstant.PurchaseState.EXPIRED.getCode()))
                .eq(Objects.nonNull(query.getPurchaseType()), PurchaseService::getPurchaseType, query.getPurchaseType())
                .orderByAsc(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), PurchaseService::getState)
                .orderByDesc(Constants.USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), PurchaseService::getPublishTime)
                .orderByDesc(PurchaseService::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<PurchaseServiceDTO> entitiesToDTOs(List<PurchaseService> entities) {
        LocalDateTime now = LocalDateTime.now();
        return MyBeanUtils.copyList(entities, PurchaseServiceDTO::new, (s, t) -> {
        });
    }
}
