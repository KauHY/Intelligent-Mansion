package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.dto.ApplyForHouseReqDTO;
import com.crlink.smartpark.business.dto.chance.BusinessChanceReqDTO;
import com.crlink.smartpark.business.entity.ApplyForHouse;
import com.crlink.smartpark.business.entity.HouseInfo;
import com.crlink.smartpark.business.mapper.ApplyForHouseMapper;
import com.crlink.smartpark.business.query.ApplyForHouseQuery;
import com.crlink.smartpark.business.service.ApplyForHouseService;
import com.crlink.smartpark.business.service.BusinessChanceService;
import com.crlink.smartpark.business.service.BusinessInfoService;
import com.crlink.smartpark.business.service.HouseInfoService;
import com.crlink.smartpark.business.vo.ApplyForHouseVO;
import com.crlink.smartpark.business.vo.BusinessHouseVO;
import com.crlink.smartpark.business.vo.BusinessInfoVO;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.business.common.Constant.ApplyForHouseStatusEnum.*;
import static com.crlink.smartpark.business.common.Constant.BUSINESS_SOURCE_FROM_APPLY_FOR_HOUSE;

/**
 * <p>
 * 看房申请表 服务实现类
 * </p>
 * */
@Service
public class ApplyForHouseServiceImpl extends ServiceImpl<ApplyForHouseMapper, ApplyForHouse> implements ApplyForHouseService {

    @Autowired
    CommonProvider commonProvider;

    @Autowired
    EmployeeProvider employeeProvider;

    @Autowired
    BusinessInfoService businessInfoService;

    @Autowired
    HouseInfoService houseInfoService;

    @Autowired
    BusinessChanceService businessChanceService;

    /**
     * @param applyForHouseReqDTO
     * @return
     */
    @Override
    public Boolean create(ApplyForHouseReqDTO applyForHouseReqDTO) {
        ApplyForHouse applyForHouse = MyBeanUtils.copy(applyForHouseReqDTO, ApplyForHouse::new);
        //从招商信息获取相关信息
        BusinessInfoVO businessInfo = businessInfoService.getBusinessInfoDetailById(applyForHouseReqDTO.getBusinessInfoId());
        applyForHouse.setBuildingId(applyForHouseReqDTO.getAddress());
        applyForHouse.setTitle(businessInfo.getTitle());
        applyForHouse.setEmployeeId(businessInfo.getEmployeeId());
        applyForHouse.setParkOrgId(businessInfo.getParkOrgId());
        applyForHouse.setStatus(WAIT_FOR_ACCEPT.getCode());
        EntityUserUtil.setAll(applyForHouse);
        return this.save(applyForHouse);
    }

    /**
     * 受理看房申请
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean accept(String id) {
        ApplyForHouse applyForHouse = new ApplyForHouse();
        applyForHouse.setId(id);
        applyForHouse.setStatus(FINISH.getCode());
        EntityUserUtil.setUpdateBy(applyForHouse);
        this.updateById(applyForHouse);
//        ApplyForHouse entity = this.getById(id);
//        BusinessChanceReqDTO businessChanceReqDTO = new BusinessChanceReqDTO();
//        businessChanceReqDTO.setChanceName(entity.getCompanyName());
//        businessChanceReqDTO.setContactName(entity.getUsername());
//        businessChanceReqDTO.setContactPhone(entity.getMobile());
//        businessChanceReqDTO.setParkOrgId(entity.getParkOrgId());
//        String houseId = getHouseIdByHouseInfoId(entity.getHouseInfoId());
//        businessChanceReqDTO.setHouseIds(CollectionUtil.newArrayList(houseId));
//        businessChanceReqDTO.setBusinessSource(BUSINESS_SOURCE_FROM_APPLY_FOR_HOUSE);
//        businessChanceReqDTO.setChanceStage(1);
        //受理后，新增一条公共商机
        BusinessChanceReqDTO businessChanceReqDTO = createNewCommonBusinessChance(id);
        return businessChanceService.saveOrUpdateBusinessChance(businessChanceReqDTO);
    }


    /**
     * 撤回申请
     *
     * @param id
     * @return
     */
    @Override
    public Boolean returnApply(String id) {
        ApplyForHouse applyForHouse = new ApplyForHouse();
        Integer status = this.getById(id).getStatus();
        if (WAIT_FOR_ACCEPT.getCode() != status) {
            throw new ResolinkRuntimeException("90005");
        }
        applyForHouse.setId(id);
        applyForHouse.setStatus(RETURN.getCode());
        EntityUserUtil.setUpdateBy(applyForHouse);
        return this.updateById(applyForHouse);
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public ApplyForHouseVO getApplyForHouseById(String id) {
        ApplyForHouse applyForHouse = this.getById(id);
        ApplyForHouseVO result = convert(Lists.newArrayList(applyForHouse)).get(0);
        return result;
    }

    /**
     * 后台分页查询
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<ApplyForHouseVO> listApplyForHousePage(ApplyForHouseQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        query.setParkOrgIds(parkOrgIds);
        //根据关键字查询对应的咨询顾问（员工）id集合
        if (StringUtils.isNotBlank(query.getKeyword())) {
            Map<String, Map<String, Object>> employeeColumns =
                    employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.likeNameBuildDTO(query.getKeyword()));
            query.setEmployeeIds(employeeColumns.keySet());
        }
        LambdaQueryWrapper<ApplyForHouse> wrapper = getWrapper(query, parkOrgIds);
        query.startPage();
        List<ApplyForHouse> list = this.list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<ApplyForHouseVO> result = convert(list);
        return query.createPageInfo(result);
    }

    /**
     * 小程序分页查询
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<ApplyForHouse> listApplyForHouseWxPage(BaseQuery<String> query) {
        String parkOrgId = query.getParam();
        query.startPage();
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        List<ApplyForHouse> list = this.lambdaQuery()
                .select(ApplyForHouse::getId, ApplyForHouse::getStatus, ApplyForHouse::getCreateTime)
                .eq(ApplyForHouse::getCreateBy, currentUserId)
                .eq(ApplyForHouse::getParkOrgId, parkOrgId)
                .orderByDesc(ApplyForHouse::getCreateTime)
                .list();
        MyPageInfoUtils.isEmptyPage(list);
        return query.createPageInfo(list);
    }

    /**
     * 数据处理
     * 获取园区名称、咨询顾问名称、意向房源名称
     *
     * @param list
     * @return
     */
    private List<ApplyForHouseVO> convert(List<ApplyForHouse> list) {
        Set<String> parkOrgIds = Sets.newHashSet();
        Set<String> employeeIds = Sets.newHashSet();
        Set<String> houseInfoIds = Sets.newHashSet();
        Set<String> buildingIds = Sets.newHashSet();
        list.forEach(t -> {
            if (StringUtils.isNotBlank(t.getHouseInfoId())) {
                houseInfoIds.add(t.getHouseInfoId());
            }
            if (StringUtils.isNotBlank(t.getBuildingId())) {
                buildingIds.add(t.getBuildingId());
            }
            parkOrgIds.add(t.getParkOrgId());
            employeeIds.add(t.getEmployeeId());
        });
        //首先根据看房申请中的招商房屋houseInfoId,在招商房屋中查出对应的houseId
        Map<String, String> houseInfoId2HouseIdMap = houseInfoService.houseInfoId2Value(houseInfoIds, HouseInfo::getHouseId);
        Set<String> houseIds = houseInfoId2HouseIdMap.values().parallelStream().collect(Collectors.toSet());
        //根据查询出的houseId,查询出对应房屋的基础信息
        Map<String, BusinessHouseVO> houseId2NameMap = houseInfoService
                .getHouseInfoNames(houseIds).parallelStream()
                .collect(Collectors.toMap(BusinessHouseVO::getHouseId, Function.identity()));
        //获取园区名称
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        //咨询顾问名称
        Map<String, String> employeeId2NameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        //获取楼栋名称
        Map<String, String> buildingId2NameMap = commonProvider.getNamesByIdsAndType(buildingIds, SimpleNode.TypeEnum.BUILDING.getCode());
        List<ApplyForHouseVO> result = list.parallelStream().map(t -> {
            ApplyForHouseVO vo = MyBeanUtils.copy(t, ApplyForHouseVO::new);
            vo.setParkOrgName(parkOrgId2NameMap.get(t.getParkOrgId()));
            vo.setEmployeeName(employeeId2NameMap.get(t.getEmployeeId()));
            String houseId = houseInfoId2HouseIdMap.get(t.getHouseInfoId());
            vo.setBusinessHouseVO(houseId2NameMap.get(houseId));
            String buildingName = buildingId2NameMap.get(t.getBuildingId());
            vo.setAddress(buildingName);
            vo.setBuildingName(buildingName);
            return vo;
        })
                .sorted(Comparator.comparing(ApplyForHouseVO::getCreateTime).reversed())
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 处理查询条件
     *
     * @param query
     * @param parkOrgIds
     * @return
     */
    private LambdaQueryWrapper<ApplyForHouse> getWrapper(ApplyForHouseQuery query, Set<String> parkOrgIds) {
        query.startPage();
        LambdaQueryWrapper<ApplyForHouse> queryWrapper = new LambdaQueryWrapper<>();
        if (query.getCreateStartTime() != null && query.getCreateEndTime() != null) {
            queryWrapper.ge(ApplyForHouse::getCreateTime, query.getCreateStartTime())
                    .le(ApplyForHouse::getCreateTime, query.getCreateEndTime());
        }
        if (StringUtils.isNotBlank(query.getKeyword())) {
            Set<String> employeeIds = query.getEmployeeIds();
            queryWrapper.and(wrapper -> wrapper
                    .like(ApplyForHouse::getUsername, query.getKeyword())
                    .or().like(ApplyForHouse::getTitle, query.getKeyword())
                    .or(CollectionUtil.isNotEmpty(employeeIds))
                    .in(CollectionUtil.isNotEmpty(employeeIds), ApplyForHouse::getEmployeeId, employeeIds));
        }
        queryWrapper.eq(Objects.nonNull(query.getStatus()), ApplyForHouse::getStatus, query.getStatus())
                .in(ApplyForHouse::getParkOrgId, parkOrgIds)
                .orderByDesc(ApplyForHouse::getCreateTime);
        return queryWrapper;
    }

    /**
     * 受理了看房申请后，创建一个公共商机
     *
     * @param id
     * @return
     */
    private BusinessChanceReqDTO createNewCommonBusinessChance(String id) {
        ApplyForHouse entity = this.getById(id);
        BusinessChanceReqDTO businessChanceReqDTO = new BusinessChanceReqDTO();
        businessChanceReqDTO.setChanceName(entity.getCompanyName());
        businessChanceReqDTO.setContactName(entity.getUsername());
        businessChanceReqDTO.setContactPhone(entity.getMobile());
        businessChanceReqDTO.setParkOrgId(entity.getParkOrgId());
        //获取招商房屋（houseInfoId）对应的房屋Id(houseId)
        String houseInfoId = entity.getHouseInfoId();
        if (StringUtils.isNotBlank(houseInfoId)) {
            Map<String, String> houseInfoId2HouseIdMap = houseInfoService.houseInfoId2Value(Sets.newHashSet(houseInfoId), HouseInfo::getHouseId);
            businessChanceReqDTO.setHouseIds(CollectionUtil.newArrayList(houseInfoId2HouseIdMap.get(houseInfoId)));
        }
        businessChanceReqDTO.setBusinessSource(BUSINESS_SOURCE_FROM_APPLY_FOR_HOUSE);
        businessChanceReqDTO.setChanceStage(1);
        return businessChanceReqDTO;
    }
}
