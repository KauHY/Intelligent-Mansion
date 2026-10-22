package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.dto.InfoDTO;
import com.crlink.smartpark.customer.dto.InformationDTO;
import com.crlink.smartpark.customer.entity.BusinessParkRelation;
import com.crlink.smartpark.customer.entity.Information;
import com.crlink.smartpark.customer.mapper.InformationMapper;
import com.crlink.smartpark.customer.model.query.InformationQuery;
import com.crlink.smartpark.customer.service.BusinessParkRelationService;
import com.crlink.smartpark.customer.service.InformationService;
import com.crlink.smartpark.customer.vo.WxInformationListVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.Constants.Redis.INFORMATION_PAGE_VIEW_PREFIX;
import static com.crlink.smartpark.customer.common.Constant.InfoStatusEnum.EFFECTING;
import static com.crlink.smartpark.customer.common.Constant.InfoStatusEnum.UN_EFFECT;
import static com.crlink.smartpark.customer.common.Constant.putIntoTypeEnum.*;
import static com.crlink.smartpark.customer.common.enums.BusinessType.INFORMATION;


@Slf4j
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information> implements InformationService {

    @Autowired
    private BusinessParkRelationService businessParkRelationService;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 保存或更新数据
     * v18 生效时间：时间字段不做限制，可选择当前时间点之前或之后的时间段
     *
     * @param dto 存储对象
     * @return 存储对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateInformation(InfoDTO dto) {
//        if (!dto.getEffectTimeStart().isAfter(LocalDateTime.now())) {
//            throw new ResolinkRuntimeException("50019");
//        }
        Information entity = new Information();
        BeanUtils.copyProperties(dto, entity);
        //如果投放类型字段长度大于1，即代表两个都选了，所以将投放范围设为3：PUT_INTO_WX_C_AND_B
        List<Integer> putIntoType = dto.getPutIntoType();
        if (putIntoType.size() > 1) {
            entity.setPutIntoType(PUT_INTO_WX_C_AND_B.type);
        } else {
            entity.setPutIntoType(putIntoType.get(0));
        }
        if (StringUtils.isBlank(dto.getId())) {
            EntityUserUtil.setAll(entity);
        } else {
            //清空原来的推送范围
            LambdaQueryWrapper<BusinessParkRelation> removeQuery = Wrappers.<BusinessParkRelation>query().lambda()
                    .eq(BusinessParkRelation::getBusinessType, INFORMATION.getCode())
                    .eq(BusinessParkRelation::getBusinessId, dto.getId());
            businessParkRelationService.remove(removeQuery);
            EntityUserUtil.setUpdateBy(entity);
        }
        this.saveOrUpdate(entity);
        List<String> parkOrgIds = dto.getParkOrgIds();
        //保存推送范围
        List<BusinessParkRelation> businessParkRelations = parkOrgIds.stream().map(parkOrgId -> {
            BusinessParkRelation businessParkRelation = new BusinessParkRelation();
            businessParkRelation.setParkOrgId(parkOrgId);
            businessParkRelation.setBusinessId(entity.getId());
            businessParkRelation.setBusinessType(INFORMATION.getCode());
            return businessParkRelation;
        }).collect(Collectors.toList());
        return businessParkRelationService.saveBatch(businessParkRelations);
    }


    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public InformationDTO getInformationDetailById(String id) {
        Information information = this.getById(id);
        Map<String, Set<String>> infoId2parkOrgIdsMap
                = businessParkRelationService.businessIds2ParkOrgIds(Sets.newHashSet(id), INFORMATION.code);
        information.setParkOrgIds(new ArrayList<>(infoId2parkOrgIdsMap.get(id)));
        List<Information> list = Lists.newArrayList(information);
        InformationDTO result = convert(list).get(0);
        return result;
    }

    /**
     * 小程序详情
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public WxInformationListVO getWxInformationDetailById(String id) {
        Information information = this.getById(id);
        WxInformationListVO result = MyBeanUtils.copy(information, WxInformationListVO::new);
        Integer redisPageView = (Integer) Optional.ofNullable(redisUtil.getObject(INFORMATION_PAGE_VIEW_PREFIX.getBuildCode(id))).orElse(0);
        result.setViews(information.getViews() + redisPageView);
        result.setEffectTimeStart(information.getEffectTimeStart().format(DATE_FORMATTER));
        return result;
    }


    /**
     * 分页查询公告
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<InformationDTO> listInformationPage(InformationQuery condition) {
        Set<String> currentUseParkOrgIds = getParkOrgIds(condition.getParkOrgId(), SystemUtil.getInstance().getParkOrgIds());
        MyPageInfoUtils.isEmptyPage(currentUseParkOrgIds);
        Set<String> infoIds = getInfoIds(currentUseParkOrgIds);
        LambdaQueryWrapper<Information> lambdaQuery = getConditionQuery(condition, infoIds);
        condition.startPage();
        List<Information> list = this.list(lambdaQuery);
        //获取查询出的结果对应的园区：即推送范围
        Map<String, Set<String>> infoId2parkOrgIdsMap
                = businessParkRelationService.businessIds2ParkOrgIds(infoIds, INFORMATION.code);
        list.forEach(t -> t.setParkOrgIds(new ArrayList<>(infoId2parkOrgIdsMap.get(t.getId()))));
        List<InformationDTO> result = convert(list);
        return condition.createPageInfo(result);
    }

    /**
     * 小程序端分页查询
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<WxInformationListVO> listWxInformationPage(BaseQuery<String> query, String source) {
        String parkOrgId = query.getParam();
        MyPageInfoUtils.isEmptyPage(parkOrgId);
        //推送到某个园区的所有资讯
        Set<String> infoIds = getInfoIds(Sets.newHashSet(parkOrgId));
        query.startPage();
        List<Information> list = queryEffectInformation(i -> true, infoIds, LocalDateTime.now(), source);
        int currentYear = LocalDate.now().getYear();
        List<WxInformationListVO> result = MyBeanUtils.copyList(list, WxInformationListVO::new, (s, t) -> {
            Integer redisPageView = (Integer) Optional.ofNullable(redisUtil.getObject(INFORMATION_PAGE_VIEW_PREFIX.getBuildCode(s.getId()))).orElse(0);
            t.setViews(s.getViews() + redisPageView);
            LocalDateTime effectTimeStart = s.getEffectTimeStart();
            if (currentYear == effectTimeStart.getYear()) {
                //当年的资讯则显示MM-DD
                t.setEffectTimeStart(effectTimeStart.format(DEFAULT_MONTH_DAY_FORMAT));
            } else {
                //非当年的资讯则显示YYYY-MM-DD
                t.setEffectTimeStart(effectTimeStart.format(DATE_FORMATTER));
            }
        });
        return query.createPageInfo(result);
    }


    /**
     * 删除公告、资讯
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeInfoById(String id) {
        businessParkRelationService.remove(Wrappers.<BusinessParkRelation>lambdaQuery()
                .eq(BusinessParkRelation::getBusinessType, INFORMATION.getCode())
                .eq(BusinessParkRelation::getBusinessId, id));
        return this.removeById(id);
    }


    private Set<String> getParkOrgIds(String parkOrgId, Set<String> parkOrgIds) {
        if (StringUtils.isNotBlank(parkOrgId)) {
            return Sets.newHashSet(parkOrgId);
        } else {
            return parkOrgIds;
        }
    }

    private List<InformationDTO> convert(List<Information> list) {
        Set<String> allParkOrgIds = list.parallelStream().map(Information::getParkOrgIds).flatMap(orgIds -> orgIds.stream()).collect(Collectors.toSet());
        Map<String, String> orgNamesByOrgIds = commonProvider.getOrgNamesByOrgIds(allParkOrgIds);
        List<InformationDTO> informationDTOs = MyBeanUtils.copyList(list, InformationDTO::new, (s, t) -> {
            Integer status = getInformationStatus(s.getEffectTimeStart(), LocalDateTime.now());
            t.setStatus(status);
            List<String> parkOrgNames = t.getParkOrgIds().parallelStream().map(id -> orgNamesByOrgIds.get(id)).collect(Collectors.toList());
            t.setParkOrgNames(parkOrgNames);
            //设置投放小程序类型
            t.setPutIntoType(getPutIntoTypeList(s.getPutIntoType()));
        });
        return informationDTOs;

    }

    /**
     * 获取投放小程序类型
     *
     * @param putIntoType
     * @return
     */
    @Override
    public LinkedList<Integer> getPutIntoTypeList(Integer putIntoType) {
        LinkedList<Integer> putIntoTypeList = Lists.newLinkedList();
        if (Objects.nonNull(putIntoType)) {
            if (putIntoType.equals(PUT_INTO_WX_C_AND_B.type)) {
                putIntoTypeList.add(PUT_INTO_WX_B.type);
                putIntoTypeList.add(PUT_INTO_WX_C.type);
            } else {
                putIntoTypeList.add(putIntoType);
            }
        }
        return putIntoTypeList;
    }


    private List<Information> pageInformationList(InformationQuery condition, Set<String> currentUseParkOrgIds) {
        Set<String> infoIds = getInfoIds(currentUseParkOrgIds);
        Map<String, Set<String>> infoId2parkOrgIdsMap
                = businessParkRelationService.businessIds2ParkOrgIds(infoIds, INFORMATION.code);
        LambdaQueryWrapper<Information> lambdaQuery = getConditionQuery(condition, infoIds);
        condition.startPage();
        List<Information> list = this.list(lambdaQuery);
        list.forEach(t -> t.setParkOrgIds(new ArrayList<>(infoId2parkOrgIdsMap.get(t.getId()))));
        return list;
    }

    /**
     * 获取要查询的园区所关联的资讯Id
     *
     * @param parkOrgIds
     * @return
     */
    private Set<String> getInfoIds(Set<String> parkOrgIds) {
        Map<String, Set<String>> map = businessParkRelationService.parkOrgIds2BusinessIds(parkOrgIds, INFORMATION.code);
        Set<String> infoIds = map.values().parallelStream().flatMap(Collection::stream).collect(Collectors.toSet());
        MyPageInfoUtils.isEmptyPage(infoIds);
        return infoIds;
    }


    /**
     * 定时任务：浏览量入库
     */
    //一个小时执行一次
    //@Scheduled(cron = "0 0 */1 * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void updateViewsToDB() {
        log.info("资讯浏览量入库开始");
        //默认单页查询数据量
        final int MAX_PAGE_SIZE = 10000;
        LocalDateTime now = LocalDateTime.now();
        int pageNo = 1;
        while (true) {
            PageHelper.startPage(pageNo, MAX_PAGE_SIZE);
            List<Information> infos = this.lambdaQuery().select(Information::getId, Information::getViews)
                    .le(Information::getEffectTimeStart, now).list();
            infos = infos.parallelStream().map(t -> {
                Integer redisPageView = (Integer) Optional.ofNullable(redisUtil.getObject(INFORMATION_PAGE_VIEW_PREFIX.getBuildCode(t.getId()))).orElse(0);
                t.setViews(Optional.ofNullable(t.getViews()).orElse(0L) + redisPageView);
                redisUtil.setObject(INFORMATION_PAGE_VIEW_PREFIX.getBuildCode(t.getId()), 0, 24L, TimeUnit.HOURS);
                return t;
            }).filter(t -> !Objects.equals(0, t.getViews())).collect(Collectors.toList());
            this.updateBatchById(infos);
            PageInfo pageInfo = new PageInfo<>(infos);
            if (!pageInfo.isHasNextPage()) {
                break;
            }
            pageNo++;
        }
    }


    /**
     * 封装分页查询条件：后台
     *
     * @param condition
     * @param infoIds
     * @return
     */
    private LambdaQueryWrapper<Information> getConditionQuery(InformationQuery condition, Set<String> infoIds) {
        String title = condition.getTitle();
        Integer type = condition.getType();
        LocalDateTime timeStart = condition.getEffectTimeStart();
        LocalDateTime timeEnd = condition.getEffectTimeEnd();
        Integer effectStatus = condition.getEffectStatus();
        Integer putIntoType = condition.getPutIntoType();
        LambdaQueryWrapper<Information> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(StringUtils.isNotBlank(title), Information::getTitle, title)
                .eq(Objects.nonNull(type), Information::getType, type)
                .and(Objects.nonNull(putIntoType), wrapper -> wrapper
                        .eq(Information::getPutIntoType, putIntoType)
                        .or().eq(Information::getPutIntoType, PUT_INTO_WX_C_AND_B.type))
                .in(Information::getId, infoIds)
                .orderByDesc(Information::getCreateTime);
        //时间段筛选条件
        if (Objects.nonNull(timeStart) && Objects.nonNull(timeEnd)) {
            lambdaQuery.gt(Information::getEffectTimeStart, timeStart)
                    .lt(Information::getEffectTimeStart, timeEnd);
        }
        //状态筛选条件
        if (effectStatus != null) {
            if (effectStatus == UN_EFFECT.status) {
                lambdaQuery.gt(Information::getEffectTimeStart, LocalDateTime.now());
            } else if (effectStatus == EFFECTING.status) {
                lambdaQuery.lt(Information::getEffectTimeStart, LocalDateTime.now());
            }
        }
        return lambdaQuery;
    }

    /**
     * 获取生效中的资讯
     *
     * @param predicate
     * @param infoIds
     * @param now
     * @return
     */
    private List<Information> queryEffectInformation(Predicate<TableFieldInfo> predicate, Set<String> infoIds, LocalDateTime now, String source) {
        LambdaQueryWrapper<Information> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.select(Information.class, predicate)
                .in(CollectionUtil.isNotEmpty(infoIds), Information::getId, infoIds)
                .le(Information::getEffectTimeStart, now)
                .orderByDesc(Information::getEffectTimeStart);
        if (USER_WX_SOURCE_B.equals(source) || USER_WX_SOURCE_C.equals(source)) {
            //根据投放小程序类型进行筛选
            lambdaQuery.and(wrapper -> wrapper
                    .eq(USER_WX_SOURCE_B.equals(source), Information::getPutIntoType, PUT_INTO_WX_B.type)
                    .eq(USER_WX_SOURCE_C.equals(source), Information::getPutIntoType, PUT_INTO_WX_C.type)
                    .or().eq(Information::getPutIntoType, PUT_INTO_WX_C_AND_B.type));
        }
        List<Information> informationList = this.list(lambdaQuery);
        return informationList;
    }

    /**
     * 获取资讯的状态
     *
     * @param startTime
     * @param
     * @return
     */
    private Integer getInformationStatus(LocalDateTime startTime, LocalDateTime now) {
        if (startTime.isAfter(now)) {
            return UN_EFFECT.status;
        } else {
            return EFFECTING.status;
        }
    }
}
