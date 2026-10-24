package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.dto.MeetingGoodsDTO;
import com.crlink.smartpark.customer.entity.MeetingGoods;
import com.crlink.smartpark.customer.entity.MeetingRent;
import com.crlink.smartpark.customer.mapper.MeetingGoodsMapper;
import com.crlink.smartpark.customer.model.MeetingGoodsReqDTO;
import com.crlink.smartpark.customer.model.query.MeetingGoodsQuery;
import com.crlink.smartpark.customer.service.MeetingGoodsService;
import com.crlink.smartpark.customer.service.MeetingRentService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 会议室物资管理 服务实现类
 * */
@Service
public class MeetingGoodsServiceImpl extends ServiceImpl<MeetingGoodsMapper, MeetingGoods> implements MeetingGoodsService {

    @Autowired
    private MeetingRentService meetingRentService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingGoods, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingGoods> lambdaQuery = this.lambdaQuery().in(MeetingGoods::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingGoods::getId, sValue);
        }
        Stream<MeetingGoods> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingGoods::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingGoods::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingGoods(MeetingGoodsReqDTO reqDTO) {
        MeetingGoods meetingGoods = this.lambdaQuery().eq(MeetingGoods::getParkOrgId, reqDTO.getParkOrgId())
                .eq(MeetingGoods::getGoodsName, reqDTO.getGoodsName()).one();
        MeetingGoods entity = MyBeanUtils.copy(reqDTO, MeetingGoods::new);
        if (StringUtils.isBlank(reqDTO.getId())) {
            EntityUserUtil.setAll(entity);
            if (meetingGoods != null) {
                throw new ResolinkRuntimeException("50014");
            }
        } else {
            MeetingGoods goods =
                    Optional.ofNullable(getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            if (!goods.getGoodsName().equals(reqDTO.getGoodsName())) {
                int count = this.lambdaQuery().eq(MeetingGoods::getParkOrgId, reqDTO.getParkOrgId())
                        .eq(MeetingGoods::getGoodsName, reqDTO.getGoodsName()).count();
                if (count > Constant.ZERO) {
                    throw new ResolinkRuntimeException("50014");
                }
            }
            EntityUserUtil.setUpdateBy(entity);
        }
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean removeByGoodsId(String id) {
        int count = meetingRentService.lambdaQuery().like(MeetingRent::getGoodsId, id).count();
        if (count > Constant.ZERO) {
            throw new ResolinkRuntimeException("50012");
        }
        return removeById(id);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public MeetingGoodsDTO getMeetingGoodsById(String id) {
        MeetingGoods entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeetingGoodsDTO> listMeetingGoodsPage(MeetingGoodsQuery query) {
        query.startPage();
        List<MeetingGoods> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingGoodsDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingGoods> getConditionWrapper(MeetingGoodsQuery query) {
        LambdaQueryWrapper<MeetingGoods> queryWrapper = new LambdaQueryWrapper<MeetingGoods>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        queryWrapper.like(StringUtils.isNotBlank(query.getGoodsName()), MeetingGoods::getGoodsName, query.getGoodsName());
        queryWrapper.eq(query.getGoodsType() != null, MeetingGoods::getGoodsType, query.getGoodsType());
        if (Constant.ZERO.equals(query.getQueryType())) {
            queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeetingGoods::getParkOrgId, query.getParkOrgId())
                    .in(MeetingGoods::getParkOrgId, parkOrgIds).orderByAsc(MeetingGoods::getGoodsName);
        } else {
            queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeetingGoods::getParkOrgId, query.getParkOrgId())
                    .in(MeetingGoods::getParkOrgId, parkOrgIds).orderByDesc(MeetingGoods::getCreateTime);
        }

        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<MeetingGoodsDTO> entitiesToDTOs(List<MeetingGoods> entities) {
        return MyBeanUtils.copyList(entities, MeetingGoodsDTO::new, (s, t) -> {

        });
    }
}
