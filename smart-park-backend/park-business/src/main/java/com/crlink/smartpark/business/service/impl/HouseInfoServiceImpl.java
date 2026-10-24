package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.common.Constant;
import com.crlink.smartpark.business.dto.HouseInfoReqDTO;
import com.crlink.smartpark.business.entity.BusinessHouseRelation;
import com.crlink.smartpark.business.entity.BusinessInfo;
import com.crlink.smartpark.business.entity.HouseInfo;
import com.crlink.smartpark.business.mapper.HouseInfoMapper;
import com.crlink.smartpark.business.query.HouseInfoQuery;
import com.crlink.smartpark.business.query.HouseQuery;
import com.crlink.smartpark.business.query.WxHouseQuery;
import com.crlink.smartpark.business.service.BusinessHouseRelationService;
import com.crlink.smartpark.business.service.BusinessInfoService;
import com.crlink.smartpark.business.service.HouseInfoService;
import com.crlink.smartpark.business.vo.*;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.house.HouseListDTO;
import com.crlink.smartpark.system.dto.house.HouseQueryDTO;
import com.crlink.smartpark.system.dto.house.HouseVO;
import com.crlink.smartpark.system.enums.HouseStatusEnum;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.business.common.Constant.BusinessInfoStatusEnum.BUSINESS_ONGOING;
import static com.crlink.smartpark.system.enums.Constant.HousePropertyEnum.RENT;
import static com.crlink.smartpark.system.enums.Constant.HousePropertyEnum.SELL;
import static com.crlink.smartpark.system.enums.HouseStatusEnum.*;
import static com.crlink.smartpark.system.vo.SimpleNode.TypeEnum.BUILDING;
import static com.crlink.smartpark.system.vo.SimpleNode.TypeEnum.HOUSE;

/**
 * <p>
 * 服务实现类
 * </p>
 * */
@Service
public class HouseInfoServiceImpl extends ServiceImpl<HouseInfoMapper, HouseInfo> implements HouseInfoService {

    @Autowired
    private HouseProvider houseProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private BusinessInfoService businessInfoService;

    @Autowired
    private BusinessHouseRelationService businessHouseRelationService;

    @Autowired
    private ContractProvider contractprovider;


    /**
     * 获取意向房源名称 楼栋名+楼层房号+建筑面积
     *
     * @param houseIds
     * @return
     */
    @Override
    public List<BusinessHouseVO> getHouseInfoNames(Set<String> houseIds) {
        List<BusinessHouseVO> houseNames = new LinkedList<>();
        if (CollectionUtil.isNotEmpty(houseIds)) {
//            Map<String, String> houseInfoId2HouseIdMap = (Map<String, String>) this.houseInfoId2Value(houseInfoIds, HouseInfo::getHouseId);
            Map<String, HouseVO> houseDetailByHouseId = houseProvider.getHouseDetailByHouseId(houseIds);
            if (CollectionUtil.isEmpty(houseDetailByHouseId)) {
                return houseNames;
            }
            houseIds.forEach(t -> {
                HouseVO houseVO = houseDetailByHouseId.get(t);
                if (Objects.nonNull(houseVO)) {
                    BusinessHouseVO businessHouseVO = new BusinessHouseVO();
//                businessHouseVO.setHouseInfoId(t);
                    businessHouseVO.setHouseId(houseVO.getId());
                    BeanUtils.copyProperties(houseVO, businessHouseVO);
                    businessHouseVO.setName(houseVO.getBuildingName() + houseVO.getName());
                    houseNames.add(businessHouseVO);
                }
            });
        }
        return houseNames;
    }

    @Override
    public <T> Map<String, T> houseInfoId2Value(Set<String> houseInfoIds, SFunction<HouseInfo, T> sValue) {
        if (CollectionUtil.isEmpty(houseInfoIds)) {
            return Collections.emptyMap();
        }
        Map<String, T> houseInfoId2ValueMap = this.lambdaQuery().select(HouseInfo::getId, sValue)
                .in(HouseInfo::getId, houseInfoIds).list()
                .stream()
                .collect(HashMap::new, (map, houseInfo) -> map.put(houseInfo.getId(), sValue.apply(houseInfo)), HashMap::putAll);
        return houseInfoId2ValueMap;
    }

//    @Override
//    public Map<String, ?> houseId2Value(Set<String> houseId, SFunction<HouseInfo, ?> sValue) {
//        if (CollectionUtil.isEmpty(houseId)) {
//            return Collections.emptyMap();
//        }
//        Map<String, ?> houseId2ValueMap = this.lambdaQuery().select(HouseInfo::getHouseId, sValue)
//                .in(HouseInfo::getHouseId, houseId)
//                .list().parallelStream().collect(Collectors.toMap(HouseInfo::getHouseId, sValue));
//        return houseId2ValueMap;
//    }

    @Override
    public Map<String, HouseInfo> houseInfoId2HouseInfo(Collection<String> houseInfoIds) {
        if (CollectionUtil.isEmpty(houseInfoIds)) {
            return Collections.emptyMap();
        }
        return this.lambdaQuery().in(HouseInfo::getId, houseInfoIds)
                .list().parallelStream().collect(Collectors.toMap(HouseInfo::getId, Function.identity()));
    }

    /**
     * 新增、编辑招商房屋
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean saveOrUpdateHouseInfo(HouseInfoReqDTO dto) {
        HouseInfo houseInfo = MyBeanUtils.copy(dto, HouseInfo::new);
        String id = dto.getId();
        if (StringUtils.isEmpty(id)) {
            //新增时保存房屋相关信息
            String buildingId = queryHouseBuildingId(dto.getParkOrgId(), dto.getHouseId());
            houseInfo.setBuildingId(buildingId);
        } else {
            // 编辑房屋信息：招商中房屋不可编辑，按钮隐藏；
            checkUpdateHouseInfo(id, true);
        }
        String unitRent = dto.getUnitRent();
        if (!dto.getDiscussPersonally() && StringUtils.hasText(unitRent)) {
            //租金处理
            houseInfo.setUnitRent(Double.parseDouble(unitRent));
        }
        EntityUserUtil.setAllDynamic(houseInfo);
        return this.saveOrUpdate(houseInfo);
    }

    /**
     * 删除招商房屋
     * 招商房屋被房源招商信息所关联，不可删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteHouseInfoById(String id) {
        checkUpdateHouseInfo(id, false);
        //释放招商房屋
        businessHouseRelationService.remove(Wrappers.<BusinessHouseRelation>lambdaQuery()
                .eq(BusinessHouseRelation::getHouseInfoId, id));
        return this.removeById(id);
    }


    @Override
    public HouseInfoVO getHouseInfoById(String id) {
        HouseInfo houseInfo = this.getById(id);
        Map<String, HouseVO> detailByHouseIdMap = houseProvider.getHouseDetailByHouseId(Sets.newHashSet(houseInfo.getHouseId()));
        HouseInfoVO houseInfoVO = MyBeanUtils.copy(houseInfo, HouseInfoVO::new);
        houseInfoVO.setHouseVO(detailByHouseIdMap.get(houseInfo.getHouseId()));
        if (houseInfo.getDiscussPersonally()) {
            houseInfoVO.setUnitRent("面议");
        } else {
            String unitRent = Optional.ofNullable(houseInfo.getUnitRent()).map(Objects::toString).orElse("");
            houseInfoVO.setUnitRent(unitRent);
        }
        return houseInfoVO;
    }

    @Override
    public HouseInfoVO getHouseInfoByHouseId(String houseId) {
        HouseVO houseVO = houseProvider.getHouseDetailByHouseId(Sets.newHashSet(houseId)).get(houseId);
//        HouseInfo houseInfo = this.lambdaQuery().eq(HouseInfo::getHouseId, houseId).one();
        HouseInfo houseInfo = this.getOne(Wrappers.<HouseInfo>lambdaQuery().eq(HouseInfo::getHouseId, houseId), false);
        HouseInfoVO houseInfoVO = new HouseInfoVO();
        Optional.ofNullable(houseInfo).ifPresent(t -> BeanUtils.copyProperties(t, houseInfoVO));
        houseInfoVO.setHouseVO(houseVO);
        return houseInfoVO;
    }


    /**
     * 招商房屋分页查询
     *
     * @param houseInfoQuery
     * @return
     */
    @Override
    public PageInfo<HouseInfoVO> listHouseInfoPage(HouseInfoQuery houseInfoQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(houseInfoQuery.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        houseInfoQuery.setParkOrgIds(parkOrgIds);
        Integer effectType = houseInfoQuery.getEffectType();
        Integer businessStatus = houseInfoQuery.getBusinessStatus();
        String employeeName = houseInfoQuery.getEmployeeName();
        if (StringUtils.hasText(employeeName)) {
            //根据招商顾问名称，筛选出对应的员工Id集合（EmployeeId）
            Map<String, Map<String, Object>> employeeColumns = employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.likeNameBuildDTO(houseInfoQuery.getEmployeeName()));
            MyPageInfoUtils.isEmptyPage(employeeColumns);
            houseInfoQuery.setEmployeeIds(employeeColumns.keySet());
        }

        //根据招商类型、状态，筛选出对应的房源招商Id集合（BusinessInfoId）
        Map<String, BusinessInfo> businessInfos = getBusinessInfos(houseInfoQuery);
        LambdaQueryChainWrapper<HouseInfo> wrapper = this.lambdaQuery()
                .eq(StringUtils.hasText(houseInfoQuery.getBuildingId()), HouseInfo::getBuildingId, houseInfoQuery.getBuildingId())
                .eq(StringUtils.hasText(houseInfoQuery.getHouseId()), HouseInfo::getHouseId, houseInfoQuery.getHouseId())
                .in(HouseInfo::getParkOrgId, houseInfoQuery.getParkOrgIds())
                .orderByDesc(HouseInfo::getCreateTime);
        if (Objects.nonNull(effectType) || Objects.nonNull(businessStatus) || StringUtils.hasText(employeeName)) {
            Map<String, Set<String>> businessId2HouseInfoIdsMap =
                    businessHouseRelationService.businessInfoId2HouseInfoIds(businessInfos.keySet());
            //招商房屋中关联了房源招商的招商房屋Id集合
            Set<String> houseInfoIds = Sets.newHashSet();
            businessId2HouseInfoIdsMap.forEach((k, v) -> houseInfoIds.addAll(v));
            MyPageInfoUtils.isEmptyPage(houseInfoIds);
            wrapper.in(HouseInfo::getId, houseInfoIds);
        }
        houseInfoQuery.startPage();
        List<HouseInfo> list = wrapper.list();
        MyPageInfoUtils.isEmptyPage(list);
////        List<HouseInfoVO> houseInfos = queryHouseRecords(houseInfoQuery);
        List<HouseInfoVO> houseInfoVOS = convert(list, businessInfos);
//        completeHouseRecord(houseInfoVOS);
        return houseInfoQuery.createPageInfo(houseInfoVOS);
    }

    /**
     * 转换为HouseInfoVO
     *
     * @param list
     * @param businessInfos
     * @return
     */
    private List<HouseInfoVO> convert(List<HouseInfo> list, Map<String, BusinessInfo> businessInfos) {
        //查询出的符合条件的招商房屋Id集合
        Set<String> resultHouseInfoIds = list.parallelStream().map(HouseInfo::getId).collect(Collectors.toSet());
        //根据招商房屋Id集合查询出其关联的房源招商Id集合
        Map<String, String> houseInfoId2BusinessInfoIdsMap = businessHouseRelationService.houseInfoId2BusinessInfoIdMap(resultHouseInfoIds);
        List<HouseInfoVO> houseInfoVOS = MyBeanUtils.copyList(list, HouseInfoVO::new, (s, t) -> {
            Double unitRent = s.getUnitRent();
            if (Objects.nonNull(unitRent)) {
                t.setUnitRent(unitRent.toString());
            }
            //被房源招商信息关联的招商房屋，设置相关信息，没有关联的相关信息为空，v13迭代列表去掉截止时间
            String businessId = houseInfoId2BusinessInfoIdsMap.get(s.getId());
            if (CollectionUtil.isNotEmpty(businessInfos) && StringUtils.hasText(businessId)) {
                BusinessInfo businessInfo = businessInfos.get(businessId);
                if (Objects.nonNull(businessInfo)) {
                    t.setEffectType(businessInfo.getEffectType());
                    t.setBusinessStatus(businessInfo.getStatus());
                    t.setEmployeeId(businessInfo.getEmployeeId());
//                    t.setDeadline(businessInfo.getDeadline());
                }
            }
        });
        completeHouseRecord(houseInfoVOS);
        return houseInfoVOS;
    }


    @Override
    public List<DictVO> listBusinessHouseInfoDict(String buildingId) {
        List<HouseInfo> houseInfos = this.lambdaQuery().select(HouseInfo::getId, HouseInfo::getHouseId).eq(HouseInfo::getBuildingId, buildingId).list();
        Set<String> houseIds = houseInfos.parallelStream().map(HouseInfo::getHouseId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(houseIds)) {
            Map<String, String> houseId2HouseNameMap = commonProvider.getNamesByIdsAndType(houseIds, HOUSE.code);
            List<DictVO> result = houseIds.parallelStream().map(houseId -> {
                String houseName = houseId2HouseNameMap.get(houseId);
                return new DictVO(houseName, houseId);
            }).collect(Collectors.toList());
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<DictVO> listBuildingDict(String parkOrgId) {
        List<HouseInfo> houseInfos = this.lambdaQuery().select(HouseInfo::getId, HouseInfo::getBuildingId).eq(HouseInfo::getParkOrgId, parkOrgId).list();
        Set<String> buildingIds = houseInfos.parallelStream().map(HouseInfo::getBuildingId).collect(Collectors.toSet());
        if (CollectionUtil.isNotEmpty(buildingIds)) {
            Map<String, String> buildingId2NameMap = commonProvider.getNamesByIdsAndType(buildingIds, BUILDING.code);
            List<DictVO> result = buildingIds.parallelStream().map(buildingId -> {
                String buildingName = buildingId2NameMap.get(buildingId);
                return new DictVO(buildingName, buildingId);
            }).collect(Collectors.toList());
            // 按楼栋名称进行排序->升序
            if(CollectionUtil.isNotEmpty(result)) {
               result.sort(Comparator.comparing(DictVO::getLabel));
            }
            return result;
        }
        return Collections.emptyList();
    }


    /**
     * 运营小程序：招商详情，楼栋信息
     * v13迭代：楼栋取值：楼栋列表根据“房源招商”模块中“招商中”状态的房屋所在楼栋取值。
     * 22.8.15
     * v19迭代 {
     * 空置率=待租房间数/所属楼栋房间总数；
     * 房间总数：取值于“园区管理—房屋管理”中所属楼栋“房屋属性”为“租赁”的房间总数；
     * 已租：取值于“合同管理—房屋状态”为“已租”房间总数；
     * 待租：取值于“合同管理—房屋状态”为“待租”房间总数；
     * 已售：取值于“合同管理—房屋状态”为“已售”房间总数；
     * 待售：取值于“合同管理—房屋状态”为“待售”房间总数；
     * }
     *
     * @return
     */
    @Override
//    @Id2objHandle
    public List<WxBuildingVO> queryBuildings() {
        //当前员工所在园区，与小程序首页园区相同
        String parkOrgId = SystemUtil.getInstance().getParkOrgIdByEmployeeId();
        String[] queryColumn = new String[]{"house_status", "building_id"};
        //1、首先找出对应园区中状态为招商中的房源招商id集合
        Set<String> businessIds = businessInfoService.getBusinessIdsByStatus(Sets.newHashSet(parkOrgId), BUSINESS_ONGOING.status);
        if (CollectionUtil.isEmpty(businessIds)) {
            return Lists.newLinkedList();
        }
        //2、找出房源招商关联的招商房屋id集合
        Map<String, Set<String>> businessId2HouseInfoIdsMap =
                businessHouseRelationService.businessInfoId2HouseInfoIds(businessIds);
        Set<String> allHouseInfoIds = businessId2HouseInfoIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        //3、找出所有招商房屋所在的楼栋id集合
        Set<String> buildingIds = this.houseInfoId2Value(allHouseInfoIds, HouseInfo::getBuildingId)
                .values().parallelStream().collect(Collectors.toSet());
        HouseQueryDTO houseQueryDTO = HouseQueryDTO.buildingIdsBuildDTO(parkOrgId, buildingIds, queryColumn);
        //房屋管理”中所属楼栋“房屋属性”为“租赁”、“销售”的房间
        houseQueryDTO.setHouseProperties(Sets.newHashSet(RENT.code, SELL.code));
        Map<String, Map<String, Object>> houseColumns = houseProvider.queryHouseColumns(houseQueryDTO);
        if (CollectionUtil.isEmpty(houseColumns)) {
            return Lists.newLinkedList();
        }
        //被合同锁关联的已租、待租的房屋
        Map<Integer, Set<String>> allLinkedHouseIds = contractprovider.getRentedHouseId(parkOrgId);
        Set<String> allRentingHouseIds = allLinkedHouseIds.get(RENTING.getCode());
        Set<String> allRentedHouseIds = allLinkedHouseIds.get(RENTED.getCode());
        Map<String, WxBuildingVO> buildingMap = new HashMap<>();
        houseColumns.forEach((k, v) -> {
            int houseStatus = (Integer) v.get("house_status");
            String houseId = (String) v.get("id");
            if (houseStatus == FOR_SELF.getCode()) {
                return;
            }
            String buildingId = (String) v.get("building_id");
            buildingIds.add(buildingId);
            WxBuildingVO buildingVO = buildingMap.getOrDefault(buildingId, new WxBuildingVO(buildingId));
            buildingVO.setBuildingId(buildingId);
            if (allRentedHouseIds.contains(houseId)) {
                buildingVO.setRentedCount(buildingVO.getRentingCount() + 1);
            }
            if (allRentingHouseIds.contains(houseId)) {
                buildingVO.setRentingCount(buildingVO.getRentingCount() + 1);
            }
//            buildingVO.setRentedCount(houseStatus == RENTED.getCode() ? buildingVO.getRentedCount() + 1 : buildingVO.getRentedCount());
//            buildingVO.setRentingCount(houseStatus == RENTING.getCode() ? buildingVO.getRentingCount() + 1 : buildingVO.getRentingCount());
            buildingVO.setSoldCount(houseStatus == SOLD.getCode() ? buildingVO.getSoldCount() + 1 : buildingVO.getSoldCount());
            buildingVO.setSellingCount(houseStatus == SELLING.getCode() ? buildingVO.getSellingCount() + 1 : buildingVO.getSellingCount());
            buildingVO.setTotalCount(buildingVO.getTotalCount() + 1);
            buildingMap.put(buildingId, buildingVO);
        });
        Map<String, String> buildingId2NameMap = commonProvider.getNamesByIdsAndType(buildingIds, SimpleNode.TypeEnum.BUILDING.getCode());
        List<WxBuildingVO> vos = buildingMap.values().stream().map(t -> {
            t.setBuildingName(buildingId2NameMap.get(t.getBuildingId()));
            //空置率: 待租房间数/总房间数
            int totalCount = t.getTotalCount();
            if (totalCount > 0) {
                BigDecimal vacancyRate = new BigDecimal(t.getRentingCount()).divide(new BigDecimal(totalCount), 4, BigDecimal.ROUND_HALF_DOWN);
                t.setVacancyRate(vacancyRate.multiply(new BigDecimal(100)));
            }
            return t;
        }).sorted((o1, o2) -> new PinyinComparator().compare(o1.getBuildingName(), o2.getBuildingName())).collect(Collectors.toList());
        return vos;
    }

    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("0.0000");
        BigDecimal divide = new BigDecimal(0).divide(new BigDecimal(38), 4, BigDecimal.ROUND_HALF_DOWN);
        System.out.println(divide);

        System.out.println(new BigDecimal(format.format((double) 36 / 38)));
    }

    /**
     * 运营小程序：招商详情，楼栋房屋信息统计
     *
     * @param buildingId
     * @return
     */
    @Override
    public WxHouseVO queryHouses(String buildingId) {
        String parkOrgId = SystemUtil.getInstance().getParkOrgIdByEmployeeId();
        //被合同锁关联的已租、待租的房屋
        Map<Integer, Set<String>> allLinkedHouseIds = contractprovider.getRentedHouseId(parkOrgId);
        Set<String> allRentingHouseIds = allLinkedHouseIds.get(RENTING.getCode());
        Set<String> allRentedHouseIds = allLinkedHouseIds.get(RENTED.getCode());
        WxHouseVO wxHouseVO = computeBuildingHouseInfo(
                buildHouseColumns(parkOrgId, buildingId, null), allRentedHouseIds, allRentingHouseIds);
        return wxHouseVO;
    }

    /**
     * 运营小程序：招商详情，楼栋房屋信息
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<BusinessHouseVO> queryHousesPage(WxHouseQuery query) {
        String parkOrgId = SystemUtil.getInstance().getParkOrgIdByEmployeeId();
        //1、首先找出对应园区中状态为招商中的房源招商id集合
        Set<String> businessIds = businessInfoService.getBusinessIdsByStatus(Sets.newHashSet(parkOrgId), BUSINESS_ONGOING.status);
        MyPageInfoUtils.isEmptyPage(businessIds);
        //2、找出房源招商关联的招商房屋
        Set<String> allHouseInfoIds = businessHouseRelationService.getAllHouseInfoIds(businessIds);
        MyPageInfoUtils.isEmptyPage(allHouseInfoIds);
        //3、找出招商房屋对应的基础房屋id
//        Set<String> houseIds = houseInfoId2Value(allHouseInfoIds, HouseInfo::getHouseId).values().parallelStream().collect(Collectors.toSet());
        Set<String> houseIds = this.lambdaQuery().select(HouseInfo::getId, HouseInfo::getHouseId)
                .eq(HouseInfo::getBuildingId, query.getBuildingId())
                .in(HouseInfo::getId, allHouseInfoIds).list()
                .parallelStream().map(HouseInfo::getHouseId).collect(Collectors.toSet());
        MyPageInfoUtils.isEmptyPage(houseIds);
        Map<String, Map<String, Object>> houseColumns = buildHouseColumns(parkOrgId, query.getBuildingId(), houseIds);
        query.startPage();
        List<HouseInfo> houseInfoList = this.lambdaQuery()
                .select(HouseInfo::getId, HouseInfo::getHouseId, HouseInfo::getBuildingId)
                .in(HouseInfo::getHouseId, houseIds).list();
        MyPageInfoUtils.isEmptyPage(houseInfoList);
        List<BusinessHouseVO> businessHouseVOS = buildBusinessHouseVO(parkOrgId, houseInfoList, houseColumns, query);
        return query.createPageInfo(businessHouseVOS);
    }

    private List<BusinessHouseVO> buildBusinessHouseVO(String parkOrgId, List<HouseInfo> houseInfoList,
                                                       Map<String, Map<String, Object>> houseColumns,
                                                       WxHouseQuery query) {
        Set<String> houseInfoIds = houseInfoList.parallelStream().map(HouseInfo::getId).collect(Collectors.toSet());
        Map<String, String> houseInfoId2BusinessInfoIdMap = businessHouseRelationService.lambdaQuery()
                .select(BusinessHouseRelation::getHouseInfoId, BusinessHouseRelation::getBusinessInfoId)
                .in(BusinessHouseRelation::getHouseInfoId, houseInfoIds).list()
                .parallelStream().collect(Collectors.toMap(BusinessHouseRelation::getHouseInfoId, BusinessHouseRelation::getBusinessInfoId));
        Set<String> businessIds = houseInfoId2BusinessInfoIdMap.values().parallelStream().collect(Collectors.toSet());
        //获取招商房屋对应的招商信息Id及图片
        Map<String, String> businessInfoId2Picture = businessInfoService.lambdaQuery().select(BusinessInfo::getId, BusinessInfo::getTitlePicture)
                .in(BusinessInfo::getId, businessIds).list().parallelStream().collect(Collectors.toMap(BusinessInfo::getId, BusinessInfo::getTitlePicture));
        //获取招商房屋对应的楼栋名称及园区名
        Map<String, String> buildingId2NameMap = commonProvider.getNamesByIdsAndType(Sets.newHashSet(query.getBuildingId()), SimpleNode.TypeEnum.BUILDING.getCode());
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(Sets.newHashSet(parkOrgId), SimpleNode.TypeEnum.PARK.getCode());
        List<BusinessHouseVO> businessHouseVOS = Lists.newLinkedList();
        houseInfoList.forEach(t -> {
            String houseId = t.getHouseId();
            Map<String, Object> houseMap = houseColumns.get(houseId);
            if (CollectionUtil.isNotEmpty(houseMap)) {
                int houseStatus = (Integer) houseMap.get("house_status");
                int houseFunction = (Integer) houseMap.get("house_function");
                BigDecimal buildArea = new BigDecimal(Double.toString((Double) houseMap.get("build_area")));
                if (canAddHouseVO(houseStatus, houseFunction, query)) {
                    String houseInfoId = t.getId();
                    BusinessHouseVO businessHouseVO = new BusinessHouseVO();
                    String businessInfoId = houseInfoId2BusinessInfoIdMap.get(houseInfoId);
                    businessHouseVO.setHouseId(houseId);
                    businessHouseVO.setHouseInfoId(houseInfoId);
                    businessHouseVO.setBusinessInfoId(businessInfoId);
                    businessHouseVO.setTitlePicture(businessInfoId2Picture.get(businessInfoId));
                    String name = houseMap.get("name") + buildingId2NameMap.get(t.getBuildingId());
                    businessHouseVO.setName(name);
                    businessHouseVO.setParkOrgName(parkOrgId2NameMap.get(parkOrgId));
                    businessHouseVO.setBuildArea(buildArea.doubleValue());
                    businessHouseVO.setHouseStatus(houseStatus);
                    businessHouseVOS.add(businessHouseVO);
                }
            }
        });
        return businessHouseVOS;
    }


    /**
     * 房屋相关信息查询
     *
     * @param parkOrgId
     * @param buildingId
     * @return
     */
    private Map<String, Map<String, Object>> buildHouseColumns(String parkOrgId, String buildingId, Set<String> houseIds) {
        String[] queryColumn = new String[]{"build_area", "name", "house_status", "house_function"};
        HouseQueryDTO houseQueryDTO;
        if (CollectionUtil.isNotEmpty(houseIds)) {
            houseQueryDTO = HouseQueryDTO.houseIdsBuildDTO(parkOrgId, houseIds, queryColumn);
        } else {
            houseQueryDTO = HouseQueryDTO.buildingIdsBuildDTO(parkOrgId, Sets.newHashSet(buildingId), queryColumn);
        }
        houseQueryDTO.setHouseProperties(Sets.newHashSet(RENT.code, SELL.code));
        Map<String, Map<String, Object>> houseColumns = houseProvider.queryHouseColumns(houseQueryDTO);
        return houseColumns;
    }


    @Override
    public PageInfo<HouseInfoDictVO> selectListHouseInfoPage(HouseQuery houseQuery) {
        LambdaQueryChainWrapper<HouseInfo> wrapper = this.lambdaQuery()
                .select(HouseInfo::getHouseId, HouseInfo::getId, HouseInfo::getCreateTime,
                        HouseInfo::getDiscussPersonally, HouseInfo::getUnitRent, HouseInfo::getEarliestRentTime);
        if (StringUtils.hasText(houseQuery.getHouseId())) {
            wrapper.eq(HouseInfo::getHouseId, houseQuery.getHouseId());
        }
        if (StringUtils.hasText(houseQuery.getBuildingId())) {
            wrapper.eq(HouseInfo::getBuildingId, houseQuery.getBuildingId());
        }
        Set<String> allHouseInfoIds = businessHouseRelationService.getAllHouseInfoIds(null);
        //过滤掉被删掉的基础房屋数据
        HouseListDTO houseListDTO = new HouseListDTO();
        houseListDTO.setParkOrgId(houseQuery.getParkOrgId());
        Set<String> allDelHouseByParkOrgId = houseProvider.getDelHouseByParkOrgId(houseListDTO);
        //过滤已经关联了的招商房屋
        wrapper.notIn(ObjectUtil.isNotEmpty(allHouseInfoIds), HouseInfo::getId, allHouseInfoIds)
                .notIn(CollectionUtil.isNotEmpty(allDelHouseByParkOrgId), HouseInfo::getHouseId, allDelHouseByParkOrgId);
        wrapper.eq(HouseInfo::getParkOrgId, houseQuery.getParkOrgId()).orderByDesc(HouseInfo::getCreateTime);
        if (StringUtils.hasText(houseQuery.getBusinessInfoId())) {
            //编辑房源招商时已关联的招商房屋
            Set<String> currentBusinessHouseInfoIds = businessHouseRelationService.getAllHouseInfoIds(Sets.newHashSet(houseQuery.getBusinessInfoId()));
            if (CollectionUtil.isNotEmpty(currentBusinessHouseInfoIds)) {
                wrapper.or().in(HouseInfo::getId, currentBusinessHouseInfoIds);
            }
        }
        houseQuery.startPage();
        List<HouseInfo> list = wrapper.list();
        MyPageInfoUtils.isEmptyPage(list);
        return houseQuery.createPageInfo(houseInfo2HouseInfoDictVO(list));

    }


    /**
     * 商机意向房源分页查询
     *
     * @param houseQuery
     * @return
     */
    @Override
    @Id2objHandle
    public PageInfo<HouseBaseVO> selectIntentionHousePage(HouseQuery houseQuery) {
        houseQuery.startPage();
        List<HouseInfo> houseInfos = this.lambdaQuery()
                .eq(StringUtils.hasText(houseQuery.getBuildingId()), HouseInfo::getBuildingId, houseQuery.getBuildingId())
                .eq(StringUtils.hasText(houseQuery.getHouseId()), HouseInfo::getHouseId, houseQuery.getHouseId())
                .eq(HouseInfo::getParkOrgId, houseQuery.getParkOrgId())
                .groupBy(HouseInfo::getHouseId).orderByDesc(HouseInfo::getCreateTime).list();
        List<HouseBaseVO> list = MyBeanUtils.copyList(houseInfos, HouseBaseVO::new);
        return houseQuery.createPageInfo(list);
    }

    private List<HouseInfoDictVO> houseInfo2HouseInfoDictVO(List<HouseInfo> list) {
        Set<String> houseIds = list.parallelStream().map(HouseInfo::getHouseId).collect(Collectors.toSet());
        Map<String, HouseVO> houseDetailsMap = houseProvider.getHouseDetailByHouseId(houseIds);
        List<HouseInfoDictVO> result = list.parallelStream().map(houseInfo -> {
            String houseId = houseInfo.getHouseId();
            HouseVO houseVO = houseDetailsMap.get(houseId);
            HouseInfoDictVO dictVO = new HouseInfoDictVO();
            if (Objects.isNull(houseVO)) {
                return dictVO;
            }
            dictVO.setHouseInfoId(houseInfo.getId());
            dictVO.setName(houseVO.getName());
            dictVO.setBuildingName(houseVO.getBuildingName());
            dictVO.setBuildArea(houseVO.getBuildArea());
            //dictVO.setCreateTime(houseInfo.getCreateTime());
            dictVO.setUnitRent(Optional.ofNullable(houseInfo.getUnitRent()).map(Objects::toString).orElse(""));
            dictVO.setEarliestRentTime(houseInfo.getEarliestRentTime());
            if (houseInfo.getDiscussPersonally()) {
                dictVO.setUnitRent("面议");
            }
            return dictVO;
        }).collect(Collectors.toList());
        return result;
    }


    /**
     * 运营小程序：招商详情，楼栋房屋信息统计
     *
     * @param houseColumns
     * @return
     */
    private WxHouseVO computeBuildingHouseInfo(Map<String, Map<String, Object>> houseColumns, Set<String> allRentedHouseIds, Set<String> allRentingHouseIds) {
        AtomicInteger totalHouse = new AtomicInteger();
        AtomicDouble totalArea = new AtomicDouble(new Double("0"));
        AtomicDouble rentedArea = new AtomicDouble(new Double("0"));
        AtomicDouble rentingArea = new AtomicDouble(new Double("0"));
        AtomicDouble soldArea = new AtomicDouble(new Double("0"));
        AtomicDouble sellingArea = new AtomicDouble(new Double("0"));
        houseColumns.forEach((k, v) -> {
            int houseStatus = (Integer) v.get("house_status");
            if (houseStatus == FOR_SELF.getCode()) {
                return;
            }
            String houseId = (String) v.get("id");
//            totalHouse.incrementAndGet();
            if (allRentedHouseIds.contains(houseId)) {
                houseStatus = RENTED.getCode();
            }
            if (allRentingHouseIds.contains(houseId)) {
                houseStatus = RENTING.getCode();
            }
            BigDecimal buildArea = new BigDecimal(Double.toString((Double) v.get("build_area")));
            BigDecimal totalAreaTemp = new BigDecimal(Double.toString(totalArea.get()));
//            totalArea.set(buildArea.add(totalAreaTemp).doubleValue());
            HouseStatusEnum statusEnum = HouseStatusEnum.values()[houseStatus];
            switch (statusEnum) {
                case RENTED:
                    BigDecimal rentedAreaTemp = new BigDecimal(Double.toString(rentedArea.get()));
                    rentedArea.set(rentedAreaTemp.add(buildArea).doubleValue());
                    totalArea.set(buildArea.add(totalAreaTemp).doubleValue());
                    totalHouse.incrementAndGet();
                    break;
                case RENTING:
                    BigDecimal rentingAreaTemp = new BigDecimal(Double.toString(rentingArea.get()));
                    rentingArea.set(rentingAreaTemp.add(buildArea).doubleValue());
                    totalArea.set(buildArea.add(totalAreaTemp).doubleValue());
                    totalHouse.incrementAndGet();
                    break;
                case SELLING:
                    BigDecimal sellingAreaTemp = new BigDecimal(Double.toString(sellingArea.get()));
                    sellingArea.set(sellingAreaTemp.add(buildArea).doubleValue());
                    break;
                case SOLD:
                    BigDecimal soldAreaTemp = new BigDecimal(Double.toString(soldArea.get()));
                    soldArea.set(soldAreaTemp.add(buildArea).doubleValue());
                    break;
                default:
            }
        });
        WxHouseVO buildingVO = new WxHouseVO();
        buildingVO.setTotalHouse(totalHouse.get());
//        buildingVO.setTotalHouse(allRentedHouseIds.size() + allRentingHouseIds.size());
        buildingVO.setTotalArea(totalArea.get());
        buildingVO.setRentedArea(rentedArea.get());
        buildingVO.setRentingArea(rentingArea.get());
        buildingVO.setSoldArea(soldArea.get());
        buildingVO.setSellingArea(sellingArea.get());
        return buildingVO;
    }

    private boolean canAddHouseVO(int houseStatus, int houseFunction, WxHouseQuery query) {
        boolean flag = true;
        if (query.getHouseStatus() != null && query.getHouseStatus() != houseStatus) {
            flag = false;
        }
        if (query.getHouseFunction() != null && query.getHouseFunction() != houseFunction) {
            flag = false;
        }
        return flag;
    }


//    private List<HouseInfoVO> queryHouseRecords(HouseInfoQuery houseInfoQuery) {
//        List<HouseInfoVO> houseInfos = baseMapper.listHouseInfo(houseInfoQuery);
//        completeHouseRecord(houseInfos);
//        return houseInfos;
//    }

    /**
     * 设置基础房屋信息，咨询顾问名
     *
     * @param list
     */
    private void completeHouseRecord(List<HouseInfoVO> list) {
        Set<String> employeeIds = Sets.newHashSet();
        Set<String> houseIds = Sets.newHashSet();
        list.forEach(t -> {
            employeeIds.add(t.getEmployeeId());
            houseIds.add(t.getHouseId());
        });
        Map<String, String> employeeId2NameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        Map<String, HouseVO> houseDetailMap = houseProvider.getHouseDetailByHouseId(houseIds);
        list.forEach(t -> {
            t.setEmployeeName(employeeId2NameMap.get(t.getEmployeeId()));
            HouseVO houseVO = houseDetailMap.get(t.getHouseId());
            if (Objects.isNull(houseVO)) {
                t.setHouseVO(new HouseVO());
            } else {
                t.setHouseVO(houseVO);
            }
            System.out.println(t.getUnitRent());
            if (t.getDiscussPersonally()) {
                t.setUnitRent("面议");
            }
        });

    }

    /**
     * 根据查询条件筛选出招商房源
     *
     * @param
     * @return
     */
    private Map<String, BusinessInfo> getBusinessInfos(HouseInfoQuery houseInfoQuery) {
        Integer effectType = houseInfoQuery.getEffectType();
        Integer businessStatus = houseInfoQuery.getBusinessStatus();
        Set<String> employeeIds = houseInfoQuery.getEmployeeIds();
        List<BusinessInfo> list = businessInfoService.lambdaQuery()
                .select(BusinessInfo::getId, BusinessInfo::getEffectType, BusinessInfo::getStatus,
                        BusinessInfo::getEmployeeId, BusinessInfo::getDeadline)
                .eq(Objects.nonNull(effectType), BusinessInfo::getEffectType, effectType)
//                .eq(Objects.nonNull(businessStatus), BusinessInfo::getStatus, businessStatus)
                .in(CollectionUtil.isNotEmpty(employeeIds), BusinessInfo::getEmployeeId, employeeIds)
                .in(BusinessInfo::getParkOrgId, houseInfoQuery.getParkOrgIds()).list();
        LocalDateTime now = LocalDateTime.now();
        Integer type = DictUtil.getValue(Constant.EffectType.TIME_LIMITED_VALIDITY);
        Map<String, BusinessInfo> result = new HashMap<>();
        if (Objects.nonNull(businessStatus)) {
            list.forEach(t -> {
                if (type.equals(t.getEffectType())) {
                    Integer status = businessInfoService.getBusinessStatus(t, now);
                    t.setStatus(status);
                    if (status.equals(businessStatus)) {
                        result.put(t.getId(), t);
                    }
                } else {
                    if (t.getStatus().equals(businessStatus)) {
                        result.put(t.getId(), t);
                    }
                }
            });
            return result;
        } else {
            return list.parallelStream().map(t -> {
                if (type.equals(t.getEffectType())) {
                    t.setStatus(businessInfoService.getBusinessStatus(t, now));
                }
                return t;
            }).collect(Collectors.toMap(BusinessInfo::getId, Function.identity()));
        }
    }


    /**
     * 检验能否编辑、删除
     *
     * @param houseInfoId
     */
    private void checkUpdateHouseInfo(String houseInfoId, Boolean isUpdate) {
        Map<String, String> houseInfoId2BusinessInfoIdMap =
                businessHouseRelationService.houseInfoId2BusinessInfoIdMap(Sets.newHashSet(houseInfoId));
        String businessId = houseInfoId2BusinessInfoIdMap.get(houseInfoId);
        if (StringUtils.hasText(businessId)) {
            if (!isUpdate) {
                //招商房屋被房源招商信息所关联，不可删除
                throw new ResolinkRuntimeException("90001");
            } else {
                Integer businessInfoStatus =
                        businessInfoService.getBusinessInfoDetailById(businessId).getStatus();
                //招商房屋被房源招商信息所关联，且其状态为招商中，不可编辑
                if (Objects.equals(BUSINESS_ONGOING.status, businessInfoStatus)) {
                    throw new ResolinkRuntimeException("900010");
                }
            }
        }
    }


    private String queryHouseBuildingId(String parkOrgId, String houseId) {
        Set<String> houseIds = Sets.newHashSet(houseId);
        Map<String, Map<String, Object>> houseColumns = houseProvider.queryHouseColumns(HouseQueryDTO.houseIdsBuildDTO(parkOrgId, houseIds, "building_id"));
        Map<String, Object> houseMap = houseColumns.get(houseId);
        String buildingId = (String) houseMap.get("building_id");
        return buildingId;
    }
//    private List<HouseInfoVO> convert(List<HouseInfo> houseInfos) {
//        Set<String> houseIds = houseInfos.parallelStream().map(HouseInfo::getHouseId).collect(Collectors.toSet());
//        Map<String, HouseVO> detailByHouseIdMap = houseProvider.getHouseDetailByHouseId(houseIds);
//        List<HouseInfoVO> houseInfoVOList = houseInfos.parallelStream().map(t -> {
//            HouseInfoVO houseInfoVO = MyBeanUtils.copy(t, HouseInfoVO::new);
//            houseInfoVO.setHouseVO(detailByHouseIdMap.get(t.getHouseId()));
//            return houseInfoVO;
//        }).collect(Collectors.toList());
//        return houseInfoVOList;
//    }


    //private void checkHouseInfoCreate(HouseInfoReqDTO dto) {
    //    if (StringUtils.isEmpty(dto.getId())) {
    //        Integer count = this.lambdaQuery().eq(HouseInfo::getHouseId, dto.getHouseId()).count();
    //        if (count > 0) {
    //            throw new ResolinkRuntimeException("90009");
    //        }
    //    }
    //}

}
