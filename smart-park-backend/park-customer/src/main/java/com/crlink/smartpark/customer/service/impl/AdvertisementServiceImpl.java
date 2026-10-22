package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.controller.request.AdvertisementReqDTO;
import com.crlink.smartpark.customer.controller.request.query.AdvertisementQuery;
import com.crlink.smartpark.customer.dto.AdvertisementDTO;
import com.crlink.smartpark.customer.entity.Advertisement;
import com.crlink.smartpark.customer.entity.BusinessParkRelation;
import com.crlink.smartpark.customer.mapper.AdvertisementMapper;
import com.crlink.smartpark.customer.service.AdvertisementService;
import com.crlink.smartpark.customer.service.BusinessParkRelationService;
import com.crlink.smartpark.customer.vo.WxAdvertisementVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.customer.common.enums.AdvertisementStatusEnum.*;
import static com.crlink.smartpark.customer.common.enums.BusinessType.ADVERTISEMENT;

/**
 * 广告 服务实现类
 * */
@Service
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement> implements AdvertisementService {

    @Autowired
    BusinessParkRelationService businessParkRelationService;

    /**
     * 保存或更新广告
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateAdvertisement(AdvertisementReqDTO reqDTO) {
        checkName(reqDTO.getId(), reqDTO.getName());
        Advertisement entity = MyBeanUtils.copy(reqDTO, Advertisement::new);
        if (StringUtils.isBlank(reqDTO.getId())) {
            EntityUserUtil.setAll(entity);
        } else {
            //删除相应的推送范围
            LambdaQueryWrapper<BusinessParkRelation> removeQuery = Wrappers.<BusinessParkRelation>query().lambda()
                    .eq(BusinessParkRelation::getBusinessType, ADVERTISEMENT.code)
                    .eq(BusinessParkRelation::getBusinessId, reqDTO.getId());
            businessParkRelationService.remove(removeQuery);
            EntityUserUtil.setUpdateBy(entity);
        }
        this.saveOrUpdate(entity);
        //保存推送范围
        List<BusinessParkRelation> businessParkRelations = reqDTO.getParkOrgIds().stream().map(parkOrgId -> {
            BusinessParkRelation businessParkRelation = new BusinessParkRelation();
            businessParkRelation.setParkOrgId(parkOrgId);
            businessParkRelation.setBusinessId(entity.getId());
            businessParkRelation.setBusinessType(ADVERTISEMENT.code);
            return businessParkRelation;
        }).collect(Collectors.toList());
        return businessParkRelationService.saveBatch(businessParkRelations);
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public AdvertisementDTO getAdvertisementById(String id) {
        Advertisement advertisement = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AdvertisementDTO result = convert(Lists.newArrayList(advertisement), null).get(0);
        Integer status = getAdvertisementStatus(result.getEffectTimeStart(), result.getEffectTimeEnd(), result.getStatus());
        result.setStatus(status);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeAdvertisementById(String id) {
        LambdaQueryWrapper<BusinessParkRelation> removeQuery = Wrappers.<BusinessParkRelation>query().lambda()
                .eq(BusinessParkRelation::getBusinessId, id);
        businessParkRelationService.remove(removeQuery);
        return this.removeById(id);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<AdvertisementDTO> listAdvertisementPage(AdvertisementQuery query) {
        Set<String> parkOrgIds = getParkOrgIds(query.getParkOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        Integer queryStatus = query.getStatus();
        List<Advertisement> list;
        if (Objects.nonNull(queryStatus)) {
            //当状态查询条件不为空时
            Set<String> advertisementIds = getAdvertisementIds(parkOrgIds);
            MyPageInfoUtils.isEmptyPage(advertisementIds);
            LambdaQueryWrapper<Advertisement> conditionWrapper = getConditionWrapper(query, advertisementIds);
            query.startPage();
            list = this.list(conditionWrapper);
        } else {
            //当状态查询条件为空时
            query.startPage();
            list = baseMapper.pageAdvertisementList(query, parkOrgIds, LocalDateTime.now());
        }
        MyPageInfoUtils.isEmptyPage(list);
        List<AdvertisementDTO> result = convert(list, queryStatus);
        return query.createPageInfo(result);
    }

    @Override
    @Id2objHandle
    public List<WxAdvertisementVO> wxAdvertisementList(String parkOrgId) {
        Set<String> advertisementIds = getAdvertisementIds(Sets.newHashSet(parkOrgId));
        if (CollectionUtil.isEmpty(advertisementIds)) {
            return Collections.emptyList();
        }
        LocalDateTime now = LocalDateTime.now();
        List<Advertisement> list = this.lambdaQuery().select(Advertisement::getId, Advertisement::getJumpType,
                Advertisement::getJumpContent, Advertisement::getPicture, Advertisement::getPosition)
                .in(Advertisement::getId, advertisementIds)
                .lt(Advertisement::getEffectTimeStart, now)
                .gt(Advertisement::getEffectTimeEnd, now)
                .orderByAsc(Advertisement::getSort)
                .orderByDesc(Advertisement::getUpdateTime).list();
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(list, WxAdvertisementVO::new);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<Advertisement> getConditionWrapper(AdvertisementQuery query, Set<String> advertisementIds) {
        String name = query.getName();
        Integer status = query.getStatus();
        LocalDateTime queryTimeStart = query.getEffectTimeStart();
        LocalDateTime queryTimeEnd = query.getEffectTimeEnd();
        LambdaQueryWrapper<Advertisement> queryWrapper = new LambdaQueryWrapper<Advertisement>();
        queryWrapper.like(StringUtils.isNotBlank(name), Advertisement::getName, name)
                .in(Advertisement::getId, advertisementIds);
        if (queryTimeStart != null && queryTimeEnd != null) {
            //查询广告生效时间段与查询时间段存在交集的
            queryWrapper.not(wrapper -> wrapper.gt(Advertisement::getEffectTimeStart, queryTimeEnd)
                    .or().lt(Advertisement::getEffectTimeEnd, queryTimeStart));
        }
        LocalDateTime now = LocalDateTime.now();
        if (status.equals(TO_BE_EFFECTIVE.getCode())) {
            queryWrapper.gt(Advertisement::getEffectTimeStart, now);
        } else if (status.equals(EFFECTIVELY.getCode())) {
            queryWrapper.lt(Advertisement::getEffectTimeStart, now)
                    .gt(Advertisement::getEffectTimeEnd, now);
        } else if (status.equals(OUT_OF_DATE.getCode())) {
            queryWrapper.lt(Advertisement::getEffectTimeEnd, now);
        }
        queryWrapper.orderByAsc(Advertisement::getSort).orderByDesc(Advertisement::getUpdateTime);
        return queryWrapper;
    }


    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<AdvertisementDTO> convert(List<Advertisement> list, Integer queryStatus) {
        Set<String> advertisementIds = list.parallelStream().map(Advertisement::getId).collect(Collectors.toSet());
        Map<String, Set<String>> advertisementIds2ParkOgrIdsMap =
                businessParkRelationService.businessIds2ParkOrgIds(advertisementIds, ADVERTISEMENT.code);
        List<AdvertisementDTO> result = MyBeanUtils.copyList(list, AdvertisementDTO::new, (s, t) -> {
            Set<String> parkOrgIds = advertisementIds2ParkOgrIdsMap.get(s.getId());
            List<DictVO> parkOrgNames = parkOrgIds.parallelStream().map(p -> new DictVO(null, p)).collect(Collectors.toList());
            t.setParkOrgNames(parkOrgNames);
            if (Objects.nonNull(queryStatus)) {
                t.setStatus(queryStatus);
            }
        });
        return result;
    }


    /**
     * 检验名称是否重复
     *
     * @param id
     * @param name
     */
    private void checkName(String id, String name) {
        Advertisement advertisement = this.lambdaQuery().select(Advertisement::getId).eq(Advertisement::getName, name)
                .ne(StringUtils.isNotBlank(id), Advertisement::getId, id).last("limit 1").one();
        if (Objects.nonNull(advertisement)) {
            throw new ResolinkRuntimeException("50001");
        }
    }

    /**
     * 获取广告状态：0 生效中，1 待生效，2 已过期
     *
     * @param start
     * @param end
     * @param status
     * @return
     */
    private Integer getAdvertisementStatus(LocalDateTime start, LocalDateTime end, Integer status) {
        LocalDateTime now = LocalDateTime.now();
        if (start.isAfter(now)) {
            return TO_BE_EFFECTIVE.getCode();
        } else if (start.isBefore(now) && end.isAfter(now)) {
            return EFFECTIVELY.getCode();
        } else if (end.isBefore(now)) {
            return OUT_OF_DATE.getCode();
        }
        return status;
    }

    private Set<String> getParkOrgIds(String parkOrgId) {
        if (StringUtils.isNotBlank(parkOrgId)) {
            return Sets.newHashSet(parkOrgId);
        } else {
            return SystemUtil.getInstance().getParkOrgIds();
        }
    }

    private Set<String> getAdvertisementIds(Set<String> parkOrgIds) {
        Map<String, Set<String>> map = businessParkRelationService.parkOrgIds2BusinessIds(parkOrgIds, ADVERTISEMENT.code);
        return map.values().parallelStream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

}
