package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.common.BusinessHouseAreaEnum;
import com.crlink.smartpark.business.common.Constant;
import com.crlink.smartpark.business.common.HouseUnitRentEnum;
import com.crlink.smartpark.business.dto.BusinessInfoReqDTO;
import com.crlink.smartpark.business.entity.BusinessHouseRelation;
import com.crlink.smartpark.business.entity.BusinessInfo;
import com.crlink.smartpark.business.entity.HouseInfo;
import com.crlink.smartpark.business.mapper.BusinessInfoMapper;
import com.crlink.smartpark.business.model.excel.BusinessInfoExcelVO;
import com.crlink.smartpark.business.query.BusinessInfoQuery;
import com.crlink.smartpark.business.query.WxBusinessInfoQuery;
import com.crlink.smartpark.business.service.BusinessHouseRelationService;
import com.crlink.smartpark.business.service.BusinessInfoService;
import com.crlink.smartpark.business.service.HouseInfoService;
import com.crlink.smartpark.business.vo.BusinessHouseVO;
import com.crlink.smartpark.business.vo.BusinessInfoVO;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.house.HouseDTO;
import com.crlink.smartpark.system.dto.house.HouseQueryDTO;
import com.crlink.smartpark.system.dto.house.HouseVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.business.common.Constant.BusinessInfoStatusEnum.*;

/**
 * <p>
 * 服务实现类
 * </p>
 * */
@Service
public class BusinessInfoServiceImpl extends ServiceImpl<BusinessInfoMapper, BusinessInfo> implements BusinessInfoService {

    public static final String FILE_NAME = "房源招商";

    @Autowired
    private BusinessHouseRelationService businessHouseRelationService;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private HouseProvider houseProvider;

    @Autowired
    private HouseInfoService houseInfoService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BusinessInfo, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> id2ValueMap = this.lambdaQuery()
                .select(BusinessInfo::getId, sValue)
                .in(BusinessInfo::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(BusinessInfo::getId, sValue));
        return id2ValueMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateBusinessInfo(BusinessInfoReqDTO dto) {
        int rentOrSaleValue = dto.getRentOrSale().size() == 2 ? 3 : dto.getRentOrSale().get(0);
        checkEffectType(dto.getEffectType(), dto.getDeadline());
        checkTitle(dto);
        BusinessInfo businessInfo = MyBeanUtils.copy(dto, BusinessInfo::new);
        EntityUserUtil.setAllDynamic(businessInfo);
        businessInfo.setRentOrSale(rentOrSaleValue);
        String id = dto.getId();
        if (StringUtils.hasText(id)) {
            //判断能不能进行编辑
            checkCanUpdateOrRemove(id, true);
            //编辑的时候删除删除原来房屋记录
            businessHouseRelationService.remove(Wrappers.<BusinessHouseRelation>lambdaQuery()
                    .eq(BusinessHouseRelation::getBusinessInfoId, id));
        }
        this.saveOrUpdate(businessInfo);
        List<BusinessHouseRelation> businessHouseRelations = dto.getHouseInfoIds().stream().map(t -> {
            BusinessHouseRelation relation = new BusinessHouseRelation();
            relation.setBusinessInfoId(businessInfo.getId());
            relation.setHouseInfoId(t);
            return relation;
        }).collect(Collectors.toList());
        return businessHouseRelationService.saveBatch(businessHouseRelations);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBusinessInfoById(String id) {
        checkCanUpdateOrRemove(id, false);
        //释放招商房屋
        businessHouseRelationService.remove(Wrappers.<BusinessHouseRelation>lambdaQuery()
                .eq(BusinessHouseRelation::getBusinessInfoId, id));
        return this.removeById(id);
    }

    /**
     * 分页查询
     *
     * @param businessInfoQuery
     * @return
     */
    @Override
    public PageInfo<BusinessInfoVO> listBusinessInfoPage(BusinessInfoQuery businessInfoQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(businessInfoQuery.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryChainWrapper<BusinessInfo> wrapper = queryBusinessInfoList(businessInfoQuery, parkOrgIds);
        businessInfoQuery.startPage();
        List<BusinessInfo> businessInfos = wrapper.list();
        List<BusinessInfoVO> result = convert(businessInfos, BusinessInfoVO::new, true);
        return businessInfoQuery.createPageInfo(result);
    }

    @Override
    public BusinessInfoVO getBusinessInfoDetailById(String id) {
        BusinessInfo businessInfo = this.getById(id);
        boolean flag = true;
        String source = SystemUtil.getInstance().getSource();
        if (Constants.USER_WX_SOURCE_C.equals((source)) || Constants.USER_WX_SOURCE_B.equals((source))) {
            flag = false;
        }
        List<BusinessInfoVO> result = convert(Lists.newArrayList(businessInfo), BusinessInfoVO::new, flag);
        return result.get(0);
    }

    @Override
    public Boolean updateStatus(UpdateStatusReqDTO statusReqDTO) {
        return this.lambdaUpdate()
                .set(BusinessInfo::getStatus, statusReqDTO.getStatus())
                .set(BusinessInfo::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .eq(BusinessInfo::getId, statusReqDTO.getId())
                .update();

    }

    @Override
    public PageInfo<BusinessInfoVO> wxListBusinessInfoPage(WxBusinessInfoQuery query) {
        String parkOrgId = query.getParkOrgId();
        String buildingId = query.getBuildingId();
        Set<String> buildingIds = null;
        Integer coverArea = query.getCoverArea();
        Integer unitRent = query.getUnitRent();
        if (!StringUtils.isEmpty(buildingId)) {
            buildingIds = Sets.newHashSet(buildingId);
        }
        HouseQueryDTO queryDTO = HouseQueryDTO.buildingIdsBuildDTO(parkOrgId, buildingIds);
        if (Objects.nonNull(coverArea) && coverArea > 0) {
            //根据面积查询
            Map<Integer, BusinessHouseAreaEnum> areaEnumMap = Arrays.stream(BusinessHouseAreaEnum.values())
                    .collect(Collectors.toMap(BusinessHouseAreaEnum::getCode, Function.identity()));
            queryDTO.setMinCoverArea(areaEnumMap.get(coverArea).getMinArea());
            queryDTO.setMaxCoverArea(areaEnumMap.get(coverArea).getMaxArea());
        }
        Set<String> houseInfoIdsByUnitRent = Sets.newHashSet();
        if (Objects.nonNull(unitRent) && unitRent > 0) {
            //根据租金查询
            Map<Integer, HouseUnitRentEnum> unitRentEnumMap = Arrays.stream(HouseUnitRentEnum.values())
                    .collect(Collectors.toMap(HouseUnitRentEnum::getCode, Function.identity()));
            Double minRent = unitRentEnumMap.get(unitRent).getMinRent();
            Double maxRent = unitRentEnumMap.get(unitRent).getMaxRent();
            //按照租金查询到的houseInfoIds
            houseInfoIdsByUnitRent = houseInfoService.lambdaQuery().select(HouseInfo::getId)
                    .ge(HouseInfo::getUnitRent, minRent)
                    .le(Objects.nonNull(maxRent), HouseInfo::getUnitRent, maxRent)
                    .eq(StringUtils.hasText(buildingId), HouseInfo::getBuildingId, buildingId)
                    .eq(HouseInfo::getParkOrgId, parkOrgId)
                    .list().parallelStream().map(HouseInfo::getId).collect(Collectors.toSet());
            MyPageInfoUtils.isEmptyPage(houseInfoIdsByUnitRent);
        }
        Map<String, Map<String, Object>> queryHouseIdMap = houseProvider.queryHouseColumns(queryDTO);
        Set<String> houseIds = queryHouseIdMap.keySet();
        MyPageInfoUtils.isEmptyPage(houseIds);
        //按照园区、楼栋、面积查询到的houseInfoIds
        Set<String> houseInfoIds = getHouseInfoIdsByHouseId(houseIds, houseInfoIdsByUnitRent);
        Collection<String> businessInfoIds = businessHouseRelationService.houseInfoId2BusinessInfoIdMap(houseInfoIds).values();
        MyPageInfoUtils.isEmptyPage(businessInfoIds);
        query.startPage();
        //查询出状态为招商中的房源招商信息
        LambdaQueryChainWrapper<BusinessInfo> queryWrapper
                = this.lambdaQuery().in(BusinessInfo::getId, businessInfoIds);
        getWrapperByStatus(queryWrapper, BUSINESS_ONGOING.status, LocalDateTime.now());
        List<BusinessInfo> businessInfos = queryWrapper
                .orderByDesc(BusinessInfo::getUpdateTime).list();
        List<BusinessInfoVO> vos = convert(businessInfos, BusinessInfoVO::new, false);
        return query.createPageInfo(vos);
    }

    /**
     * 导出
     *
     * @param query
     */
    @Override
    public void export(BusinessInfoQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        LambdaQueryChainWrapper<BusinessInfo> wrapper = queryBusinessInfoList(query, parkOrgIds);
        ExcelExportUtils.export(wrapper::list, sourceList -> MyBeanUtils.copyList(
                convert(sourceList, BusinessInfoVO::new, true), BusinessInfoExcelVO::new),
                FILE_NAME, BusinessInfoExcelVO.class);
    }


    @Override
    public List<DictVO> listBusinessInfoParkDict() {
        LambdaQueryWrapper<BusinessInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(BusinessInfo::getParkOrgId)
                .eq(BusinessInfo::getStatus, BUSINESS_ONGOING.status);
        Set<String> parkOrgIds = this.list(queryWrapper).parallelStream().map(BusinessInfo::getParkOrgId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            return null;
        }
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        List<DictVO> parkNames = parkOrgIds.parallelStream()
                .map(t -> new SimpleNode(t, parkOrgId2NameMap.get(t), SimpleNode.TypeEnum.PARK.getCode()))
                .collect(Collectors.toList());
        return parkNames;
    }


    /**
     * BusinessInfo转BusinessInfoVO
     *
     * @param list
     * @param supplier
     * @param isPC
     * @return
     */
    private List<BusinessInfoVO> convert(List<BusinessInfo> list, Supplier<BusinessInfoVO> supplier, boolean isPC) {
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Set<String> parkOrgIds = Sets.newHashSet();
        Set<String> employeeIds = Sets.newHashSet();
        Set<String> businessInfoIds = Sets.newHashSet();
        list.forEach(info -> {
            businessInfoIds.add(info.getId());
            parkOrgIds.add(info.getParkOrgId());
            employeeIds.add(info.getEmployeeId());
        });
        Map<String, Set<String>> businessInfoId2HouseInfoIdsMap = businessHouseRelationService.businessInfoId2HouseInfoIds(businessInfoIds);
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        Map<String, String> employeeId2NameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        Set<String> allHouseInfoIds = businessInfoId2HouseInfoIdsMap.values().stream().flatMap(t -> t.stream()).collect(Collectors.toSet());
        Map<String, HouseInfo> houseInfoId2HouseInfoMap = houseInfoService.houseInfoId2HouseInfo(allHouseInfoIds);
        Map<String, String> houseInfoId2HouseIdMap = houseInfoId2HouseInfoMap.values().parallelStream().collect(Collectors.toMap(HouseInfo::getId, HouseInfo::getHouseId));
        Map<String, HouseVO> houseDetailMap = houseProvider.getHouseDetailByHouseId(Sets.newHashSet(houseInfoId2HouseIdMap.values()));

        List<BusinessInfoVO> result = Lists.newArrayList();
        LocalDateTime now = LocalDateTime.now();
        Integer type = DictUtil.getValue(Constant.EffectType.TIME_LIMITED_VALIDITY);
        for (BusinessInfo t : list) {
            BusinessInfoVO businessInfoVO = supplier.get();
            BeanUtils.copyProperties(t, businessInfoVO);
            completeBusinessInfoDTO(t, businessInfoVO, parkOrgId2NameMap, employeeId2NameMap, businessInfoId2HouseInfoIdsMap, houseInfoId2HouseIdMap, houseInfoId2HouseInfoMap, houseDetailMap, isPC);
            //当为限时有效时判断状态
            if (type.equals(businessInfoVO.getEffectType())) {
                Integer businessStatus = getBusinessStatus(t, now);
                businessInfoVO.setStatus(businessStatus);
            }
            result.add(businessInfoVO);
        }
        return result;
    }

    private void completeBusinessInfoDTO(BusinessInfo t, BusinessInfoVO businessInfoVO,
                                         Map<String, String> parkOrgId2NameMap,
                                         Map<String, String> employeeId2NameMap,
                                         Map<String, Set<String>> businessInfoId2HouseInfoIdsMap,
                                         Map<String, String> houseInfoId2HouseIdMap,
                                         Map<String, HouseInfo> houseInfoId2HouseInfoMap,
                                         Map<String, HouseVO> houseDetailMap,
                                         boolean isPC) {
        BeanUtils.copyProperties(t, businessInfoVO);
        List<Integer> rentSaleValue = t.getRentOrSale() == 3 ? Lists.newArrayList(1, 2) : Lists.newArrayList(t.getRentOrSale());
        businessInfoVO.setRentOrSale(rentSaleValue);
        businessInfoVO.setParkOrgName(parkOrgId2NameMap.get(t.getParkOrgId()));
        businessInfoVO.setEmployeeName(employeeId2NameMap.get(t.getEmployeeId()));
        Set<String> houseInfoIds = businessInfoId2HouseInfoIdsMap.get(t.getId());
        List<BusinessHouseVO> businessHouses = Lists.newArrayList();
        for (String houseInfoId : houseInfoIds) {
            String houseId = houseInfoId2HouseIdMap.get(houseInfoId);
            HouseVO houseVO = houseDetailMap.get(houseId);
            HouseInfo houseInfo = houseInfoId2HouseInfoMap.get(houseInfoId);
            BusinessHouseVO vo = getBusinessHouseVO(houseId, houseInfoId, houseVO, houseInfo);
            if (Objects.nonNull(vo)) {
                businessHouses.add(vo);
            }
        }
        businessInfoVO.setBusinessHouseVO(businessHouses);
        if (isPC) {
            businessInfoVO.setAmount(houseInfoIds.size());
            BigDecimal totalArea = houseInfoIds.stream().map(houseInfoId2HouseIdMap::get).filter(Objects::nonNull)
                    .map(houseDetailMap::get).filter(Objects::nonNull).map(HouseDTO::getCoverArea)
                    .map(area -> new BigDecimal(Double.toString(area)))
                    .reduce(new BigDecimal("0"), BigDecimal::add);
            businessInfoVO.setArea(totalArea.doubleValue());
        } else {
            //C端小程序:分页查询招商信息列表设置：建筑面积、所在楼层、单位租金
//            Set<String> houseIds = businessHouses.parallelStream().map(BusinessHouseVO::getHouseInfoId).collect(Collectors.toSet());
            //当前房源招商关联的招商房屋
            Set<String> currentHouInfoIds = businessInfoId2HouseInfoIdsMap.get(t.getId());
            Collection<String> houseIds = new HashSet<>();
            currentHouInfoIds.forEach(houInfoId -> houseIds.add(houseInfoId2HouseIdMap.get(houInfoId)));
            Integer minFloor = computeMinFloor(houseIds, houseDetailMap);
            Double minCoverArea = computeMinBuildArea(houseIds, houseDetailMap);
            String minUnitRent = computeMinUnitRent(houseInfoIds);
            businessInfoVO.setArea(minCoverArea);
            businessInfoVO.setMinFloor(minFloor);
            businessInfoVO.setUnitRent(minUnitRent);
        }
    }

    private Integer computeMinFloor(Collection<String> houseIds, Map<String, HouseVO> houseDetailMap) {
        return houseIds.stream().map(houseDetailMap::get).filter(Objects::nonNull).map(HouseVO::getFloor).filter(Objects::nonNull).reduce(Integer::min).orElse(null);
    }

    private Double computeMinBuildArea(Collection<String> houseIds, Map<String, HouseVO> houseDetailMap) {
        return houseIds.stream().map(houseDetailMap::get).filter(Objects::nonNull).map(HouseVO::getBuildArea).filter(Objects::nonNull).reduce(Double::min).orElse(null);
    }

    private String computeMinUnitRent(Set<String> houseInfoIds) {
        Map<String, HouseInfo> houseInfoMap = houseInfoService.houseInfoId2HouseInfo(houseInfoIds);
        Set<HouseInfo> houseInfos = houseInfoMap.values().parallelStream().filter(t -> !t.getDiscussPersonally()).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(houseInfos)) {
            return "面议";
        } else {
            Double minUnitRent = houseInfos.stream().map(HouseInfo::getUnitRent).filter(Objects::nonNull).reduce(Double::min).orElse(null);
            return minUnitRent.toString();
        }
    }


    private BusinessHouseVO getBusinessHouseVO(String houseId, String houseInfoId, HouseVO houseVO, HouseInfo houseInfo) {
        if (Objects.isNull(houseVO)) {
            return null;
        }
        BusinessHouseVO vo = new BusinessHouseVO();
//        vo.setHouseId(houseId);
        vo.setHouseInfoId(houseInfoId);
        vo.setName(houseVO.getBuildingName() + houseVO.getName());
        vo.setBuildArea(houseVO.getBuildArea());
        Double unitRent = houseInfo.getUnitRent();
        if (Objects.nonNull(unitRent)) {
            vo.setUnitRent(unitRent.toString());
        } else if (houseInfo.getDiscussPersonally()) {
            vo.setUnitRent("面议");
        }
        vo.setHouseStatus(houseVO.getHouseStatus());
        vo.setVisitTime(houseInfo.getVisitTime());
        vo.setFreeRentMonth(houseInfo.getFreeRentMonth());
        vo.setStationNum(houseInfo.getStationNum());
        vo.setOrientation(houseVO.getOrientation());
        return vo;
    }

    /**
     * 封装查询条件
     *
     * @param businessInfoQuery
     * @param parkOrgIds
     * @return
     */
    private LambdaQueryChainWrapper<BusinessInfo> queryBusinessInfoList(BusinessInfoQuery businessInfoQuery, Set<String> parkOrgIds) {
        LambdaQueryChainWrapper<BusinessInfo> queryWrapper = this.lambdaQuery();
        Integer status = businessInfoQuery.getStatus();
        Integer effectType = businessInfoQuery.getEffectType();
        LocalDateTime now = LocalDateTime.now();
        if (Objects.nonNull(status)) {
            //类型不为空时
            if (Objects.nonNull(effectType)) {
                //长期有效类型
                Integer longTermEffectiveness = DictUtil.getValue(Constant.EffectType.LONG_TERM_EFFECTIVENESS);
                //限时有效类型
                Integer timeLimitedValidity = DictUtil.getValue(Constant.EffectType.TIME_LIMITED_VALIDITY);
                //类型为长期有效时
                if (effectType.equals(longTermEffectiveness)) {
                    queryWrapper.eq(BusinessInfo::getStatus, status)
                            .eq(BusinessInfo::getEffectType, longTermEffectiveness);
                } else {
                    //类型为限时有效
                    queryWrapper.eq(BusinessInfo::getEffectType, timeLimitedValidity);
                    if (status.equals(NOT_ON_THE_SHELF.status)) {
                        //未上架
                        queryWrapper.eq(BusinessInfo::getStatus, status);
                    } else if (status.equals(BUSINESS_ONGOING.status)) {
                        //招商中
                        queryWrapper.eq(BusinessInfo::getStatus, status).ge(BusinessInfo::getDeadline, now);
                    } else {
                        //已下架
                        String sql = new StringBuilder(" ( status = 1 and deadline < '")
                                .append(now).append("' ) ").toString();
                        queryWrapper.and(wrapper ->
                                wrapper.eq(BusinessInfo::getStatus, status)
                                        .or().apply(sql));
                    }
                }
            } else {
                getWrapperByStatus(queryWrapper, status, now);
            }
        }
        queryWrapper.like(StringUtils.hasText(businessInfoQuery.getTitle()), BusinessInfo::getTitle, businessInfoQuery.getTitle());
        queryWrapper.eq(Objects.nonNull(effectType) && Objects.isNull(status), BusinessInfo::getEffectType, effectType);
        queryWrapper.in(BusinessInfo::getParkOrgId, parkOrgIds)
                .orderByDesc(BusinessInfo::getCreateTime);
        return queryWrapper;
    }

    /**
     * 类型为限时有效时，截止时间不能为空
     *
     * @param effectType 招商时效类型,0，限时有效 1:长期有效
     * @param deadLine   截止时间
     */
    private void checkEffectType(Integer effectType, LocalDateTime deadLine) {
        if (0 == effectType && Objects.isNull(deadLine)) {
            throw new ResolinkRuntimeException("900011");
        }
    }

    /**
     * 校验招商标题唯一性：统一园区下不可重复
     *
     * @param dto
     */
    private void checkTitle(BusinessInfoReqDTO dto) {
        Integer count = this.lambdaQuery()
                .ne(StringUtils.hasText(dto.getId()), BusinessInfo::getId, dto.getId())
                .eq(BusinessInfo::getTitle, dto.getTitle())
                .eq(BusinessInfo::getParkOrgId, dto.getParkOrgId())
                .count();
        if (count > 0) {
            throw new ResolinkRuntimeException(Message.code("900013"));
        }
    }

    /**
     * 按照园区、楼栋、面积查询到的houseInfoIds
     */
    private Set<String> getHouseInfoIdsByHouseId(Set<String> houseIds, Set<String> houseInfoIdsByUnitRent) {
        Set<String> houseInfoIds = houseInfoService.lambdaQuery().select(HouseInfo::getId)
                .in(HouseInfo::getHouseId, houseIds).list()
                .parallelStream().map(HouseInfo::getId).collect(Collectors.toSet());
        MyPageInfoUtils.isEmptyPage(houseInfoIds);
        if (CollectionUtil.isNotEmpty(houseInfoIdsByUnitRent)) {
            //求交集
            houseInfoIds = CollectionUtil.intersection(houseInfoIdsByUnitRent, houseInfoIds)
                    .parallelStream().collect(Collectors.toSet());
        }
        return houseInfoIds;
    }

    /**
     * 获取限时有效的房源招商的状态
     *
     * @param
     * @param now
     * @return
     */
    @Override
    public Integer getBusinessStatus(BusinessInfo businessInfo, LocalDateTime now) {
        Integer status = businessInfo.getStatus();
        if (status.equals(BUSINESS_ONGOING.status)) {
            if (businessInfo.getDeadline().isAfter(now)) {
                return BUSINESS_ONGOING.status;
            } else {
                return REMOVE_FROM_SHELF.status;
            }
        }
        return status;
    }

    /**
     * 校验是否可以编辑,删除
     *
     * @param id
     */
    @Override
    public void checkCanUpdateOrRemove(String id, Boolean isUpdate) {
        BusinessInfo businessInfo = this.lambdaQuery()
                .select(BusinessInfo::getId, BusinessInfo::getEffectType,
                        BusinessInfo::getStatus, BusinessInfo::getDeadline)
                .eq(BusinessInfo::getId, id).one();
        Integer effectType = businessInfo.getEffectType();
        Integer status = businessInfo.getStatus();
        if (effectType.equals(DictUtil.getValue(Constant.EffectType.TIME_LIMITED_VALIDITY))) {
            //类型为限时有效
            status = getBusinessStatus(businessInfo, LocalDateTime.now());
        }
        if (status.equals(BUSINESS_ONGOING.status)) {
            if (isUpdate) {
                throw new ResolinkRuntimeException("900010");
            } else {
                throw new ResolinkRuntimeException("90001");
            }
        }
    }

    /**
     * 根据状态获取某个园区下的房源招商id集合
     *
     * @param parkOrgIds
     * @param status
     * @return
     */
    @Override
    public Set<String> getBusinessIdsByStatus(Set<String> parkOrgIds, Integer status) {
        LambdaQueryChainWrapper<BusinessInfo> queryWrapper = this.lambdaQuery().select(BusinessInfo::getId)
                .in(BusinessInfo::getParkOrgId, parkOrgIds);
        getWrapperByStatus(queryWrapper, status, LocalDateTime.now());
        List<BusinessInfo> list = queryWrapper.list();
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.parallelStream().map(BusinessInfo::getId).collect(Collectors.toSet());
    }

    /**
     * 根据状态封装查询条件，不区分房源招商类型，
     *
     * @param queryWrapper
     * @param status
     * @param now
     */
    private void getWrapperByStatus(LambdaQueryChainWrapper<BusinessInfo> queryWrapper, Integer status, LocalDateTime now) {
        //长期有效类型
        Integer longTermEffectiveness = DictUtil.getValue(Constant.EffectType.LONG_TERM_EFFECTIVENESS);
        //限时有效类型
        Integer timeLimitedValidity = DictUtil.getValue(Constant.EffectType.TIME_LIMITED_VALIDITY);
//        queryWrapper.eq(BusinessInfo::getStatus, status);
        if (status.equals(BUSINESS_ONGOING.status)) {
            //招商中
            String sql = new StringBuilder(" ( effect_type =0 AND status = 1 and deadline >= '")
                    .append(now).append("' ) ").toString();
            queryWrapper.and(wrapper -> wrapper
                    .eq(BusinessInfo::getEffectType, longTermEffectiveness).eq(BusinessInfo::getStatus, status)
                    .or().apply(sql));
//                    .eq(BusinessInfo::getEffectType, timeLimitedValidity)).eq(BusinessInfo::getStatus, status)
//                    .ge(BusinessInfo::getDeadline, now);
        } else if (status.equals(REMOVE_FROM_SHELF.status)) {
            //已下架
            String sql = new StringBuilder(" ( effect_type =0 AND status = 1 and deadline < '")
                    .append(now).append("' ) ").toString();
            queryWrapper.and(wrapper -> wrapper
                    .eq(BusinessInfo::getStatus, status)
                    .or().apply(sql));
//                    .eq(BusinessInfo::getEffectType, timeLimitedValidity)
//                    .eq(BusinessInfo::getStatus, 1)
//                    .lt(BusinessInfo::getDeadline, now));

        } else {
            queryWrapper.eq(BusinessInfo::getStatus, status);
        }
    }
}
