package com.crlink.smartpark.fee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.fee.controller.request.ChargeItemReqDTO;
import com.crlink.smartpark.fee.controller.request.StatusReqDTO;
import com.crlink.smartpark.fee.controller.request.query.ChargeItemQuery;
import com.crlink.smartpark.fee.dto.ChargeItemDTO;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.crlink.smartpark.fee.entity.ChargeItem;
import com.crlink.smartpark.fee.mapper.ChargeItemMapper;
import com.crlink.smartpark.fee.service.BillingSchemeService;
import com.crlink.smartpark.fee.service.ChargeItemService;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 收费项 服务实现类
 * */
@Service
public class ChargeItemServiceImpl extends ServiceImpl<ChargeItemMapper, ChargeItem> implements ChargeItemService {

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private BillingSchemeService billingSchemeService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ChargeItem, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ChargeItem> lambdaQuery = this.lambdaQuery().in(ChargeItem::getId, ids);
        if (flag) {
            lambdaQuery.select(ChargeItem::getId, sValue);
        }
        Stream<ChargeItem> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result;
        if (flag) {
            result = stream.collect(Collectors.toMap(ChargeItem::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ChargeItem::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateChargeItem(ChargeItemReqDTO reqDTO) {
        AssertUtil.requireTrue(this.count(Wrappers.<ChargeItem>query().lambda()
                .eq(ChargeItem::getChargeName, reqDTO.getChargeName())
                .eq(ChargeItem::getParkOrgId, reqDTO.getParkOrgId())
                .ne(StringUtils.isNotBlank(reqDTO.getId()), ChargeItem::getId, reqDTO.getId())) <= 0, "310002");
        ChargeItem entity = MyBeanUtils.copy(reqDTO, ChargeItem::new);
        if (StringUtils.isNotBlank(reqDTO.getId())) {
            AssertUtil.requireTrue(billingSchemeService.lambdaQuery().eq(BillingScheme::getChargeItemId, reqDTO.getId()).count() <= 0, "310004");
        }
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ChargeItemDTO getChargeItemById(String id) {
        ChargeItem entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<ChargeItemDTO> listChargeItemPage(ChargeItemQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        query.startPage();
        List<ChargeItem> list = this.lambdaQuery()
                .in(ChargeItem::getParkOrgId, parkOrgIds)
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), ChargeItem::getParkOrgId, query.getParkOrgId())
                .orderByDesc(ChargeItem::getCreateTime)
                .list();
        MyPageInfoUtils.isEmptyPage(list);
        List<ChargeItemDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean updateStatusById(StatusReqDTO reqDTO) {
        ChargeItem item = new ChargeItem();
        item.setId(reqDTO.getId());
        item.setStatus(reqDTO.getStatus());
        item.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return this.updateById(item);
    }

    @Override
    public List<DictVO> selectChargeItemDict(String parkOrgId) {
        List<DictVO> result = this.lambdaQuery().select(ChargeItem::getId, ChargeItem::getChargeName, ChargeItem::getTaxRate, ChargeItem::getFeeType)
                .eq(ChargeItem::getParkOrgId, parkOrgId).orderByDesc(ChargeItem::getCreateTime)
                .eq(ChargeItem::getStatus, Constants.Status.NORMAL.code)
                .list().parallelStream().map(t -> {
                    SimpleNode simpleNode = new SimpleNode(t.getId(), t.getChargeName());
                    simpleNode.setExtraInfo(t.getTaxRate());
                    simpleNode.setType(t.getFeeType());
                    return simpleNode;
                }).collect(Collectors.toList());
        return result;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ChargeItem> getConditionWrapper(ChargeItemQuery query) {
        LambdaQueryWrapper<ChargeItem> queryWrapper = new LambdaQueryWrapper<ChargeItem>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ChargeItemDTO> entitiesToDTOs(List<ChargeItem> entities) {
        HashSet<String> parkOrgIds = Sets.newHashSet();
        entities.parallelStream().map(t -> {
            parkOrgIds.add(t.getParkOrgId());
            return t;
        }).collect(Collectors.toList());
        Map<String, String> parkOrgNameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        return MyBeanUtils.copyList(entities, ChargeItemDTO::new, (s, t) -> {
            t.setParkOrgName(parkOrgNameMap.get(s.getParkOrgId()));
        });
    }
}
