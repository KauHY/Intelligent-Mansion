package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DistanceUtil;
import com.crlink.smartpark.core.utils.SortUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Park;
import com.crlink.smartpark.system.entity.RsUserInfo;
import com.crlink.smartpark.system.mapper.ParkMapper;
import com.crlink.smartpark.system.query.ParkListQuery;
import com.crlink.smartpark.system.service.CompanyInformationService;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeManageService;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.crlink.smartpark.system.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.enums.OrgTypeEnum.PARK;

/**
 * <p>
 * 园区表 服务实现类
 * </p>
 * */
@Service
public class ParkServiceImpl extends ServiceImpl<ParkMapper, Park> implements ParkService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EmployeeManageService employeeManageService;

    @Autowired
    private OrganizationManageService organizationManageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CompanyInformationService companyInformationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ParkService parkService;

    @Override
    public List<ParkDTO> listParkByOrgIds(ParkListQuery query) {
        return baseMapper.listParkByOrgIds(query);
    }

    @Override
    public List<DictVO> listParkName() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Set<Organization> organizations = organizationManageService.listOrganizationByUserId(userId);
        List<DictVO> parkNames = organizations.stream().filter(o -> Objects.equals(PARK.getCode(), o.getOrgType()))
                //.sorted(Comparator.comparing(Organization::getCreateTime).reversed())
                .map(org -> new SimpleNode(org.getId(), org.getOrgName(), SimpleNode.TypeEnum.PARK.getCode()))
                .collect(Collectors.toList());
        List<DictVO> result = SortUtils.pinYinSort(parkNames, DictVO::getLabel);
        return result;
    }

    @Override
    public List<DictVO> listEnterpriseParkDict() {
        Set<String> codes = companyInformationService.listCodeByUserId();
        Set<String> parkOrgIds = companyService.code2CompanyMap(codes, Company::getParkOrgId).values().stream()
                .map(Company::getParkOrgId).collect(Collectors.toSet());
        Map<String, String> id2NameMap =
                (Map<String, String>) organizationService.id2ValueMap(parkOrgIds, Organization::getOrgName);
        List<DictVO> parkNames = id2NameMap.entrySet().stream()
                .map(org -> new SimpleNode(org.getKey(), org.getValue(), SimpleNode.TypeEnum.PARK.getCode()))
                .collect(Collectors.toList());
        return parkNames;
    }

    @Override
    public MobileParkVO listParksForWx(Double longitude, Double latitude, String parkOrgName, String cAppId) {
        MobileParkVO vo = new MobileParkVO();
//        Organization tenantOrg =
//                organizationService.lambdaQuery().select(Organization::getId).eq(Organization::getCAppId, cAppId).one();
//        Optional.ofNullable(tenantOrg).map(Organization::getId)
//                .orElseThrow(() -> new ResolinkRuntimeException("10018"));
        List<Organization> organizations = organizationService.lambdaQuery().select(Organization::getId).eq(Organization::getCAppId, cAppId).list();
        Optional.ofNullable(organizations).orElseThrow(() -> new ResolinkRuntimeException("10018"));
        Set<String> headOrgIds = organizations.parallelStream().map(Organization::getId).collect(Collectors.toSet());
//        Set<String> parkOrgIds = organizationManageService.getTenantParkOrgIds(headOrgIds);
        Set<String> parkOrgIds = organizationManageService.getTenantParkOrgIds();
        if (ObjectUtil.isEmpty(parkOrgIds)) {
            return vo;
        }
        List<ParkDTO> list = baseMapper.queryParksByName(parkOrgIds, parkOrgName);
        if (CollectionUtil.isEmpty(list)) {
            return vo;
        }
        // 已登录
        String userId = SystemUtil.getInstance().getCurrentUserId();
        Map<String, EmployeeInfoVO> parkOrgId2Employee = Collections.emptyMap();
        if (StringUtils.isNotBlank(userId)) {
            // 查找员工归属信息
            RsUserInfo userInfo = userInfoService.queryUserInfoByUserId(userId);
            List<EmployeeInfoVO> employeeInfoList =
                    employeeManageService.listEmployeeInfosByMobile(userInfo.getMobile());
            List<DictVO> myParks = employeeInfoList.parallelStream().map(t -> {
                SimpleNode dictVO = new SimpleNode(t.getParkOrgId(), t.getParkOrgName());
                dictVO.setExtraInfo(t);
                return dictVO;
            }).sorted((n1, n2) -> new PinyinComparator().compare(n1.getLabel(), n2.getLabel()))
                    .collect(Collectors.toList());
            vo.setMyParks(myParks);
            parkOrgId2Employee = employeeInfoList.parallelStream()
                    .collect(Collectors.toMap(EmployeeInfoVO::getParkOrgId, Function.identity()));
        }

        Map<String, EmployeeInfoVO> finalParkOrgId2Employee = parkOrgId2Employee;
        List<DictVO> parks = list.stream().map(t -> {
            SimpleNode dictVO = new SimpleNode(t.getParkOrgId(), t.getName());
            dictVO.setExtraInfo(finalParkOrgId2Employee.get(t.getParkOrgId()));
            return dictVO;
        }).sorted((n1, n2) -> new PinyinComparator().compare(n1.getLabel(), n2.getLabel()))
                .collect(Collectors.toList());
        vo.setParks(parks);
        // 位置信息已授权
        if (longitude != null && latitude != null) {
            ParkDTO nearestPark = queryNearestPark(list, longitude, latitude);
            SimpleNode dictVO = new SimpleNode(nearestPark.getParkOrgId(), nearestPark.getName());
            dictVO.setExtraInfo(finalParkOrgId2Employee.get(nearestPark.getParkOrgId()));
            vo.setNearestPark(dictVO);
        }
        return vo;
    }

    @Override
    public MobileParkInfoVO getParkInfo(String parkOrgId) {
        Organization parkOrg = organizationService.lambdaQuery().select(Organization::getId, Organization::getOrgName,
                Organization::getDescription, Organization::getParentId).eq(Organization::getId, parkOrgId).one();
        Park park = parkService.lambdaQuery().eq(Park::getParkOrgId, parkOrgId).one();
        AssertUtil.requireNotNullOrParamExp(parkOrg);
        // 获取上级组织名称
        // String parentName =
        // organizationService.lambdaQuery().select(Organization::getOrgName).eq(Organization::getId,
        // parkOrg.getParentId()).one().getOrgName();
        MobileParkInfoVO parkVO = new MobileParkInfoVO();
        if (park != null) {
            BeanUtils.copyProperties(park, parkVO);
        }
        parkVO.setName(parkOrg.getOrgName());
        parkVO.setDescription(parkOrg.getDescription());
        // parkVO.setParentOrgName(parentName);
        return parkVO;
    }

    private ParkDTO queryNearestPark(List<ParkDTO> list, Double myLongitude, Double myLatitude) {
        ParkDTO nearestPark = list.get(0);
        Double nearestDistance = Double.MAX_VALUE;
        for (ParkDTO parkDTO : list) {
            Double longitude = parkDTO.getLongitude();
            Double latitude = parkDTO.getLatitude();
            if (Objects.nonNull(longitude) && Objects.nonNull(latitude)) {
                double distance = DistanceUtil.getDistance(longitude, latitude, myLongitude, myLatitude);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPark = parkDTO;
                }
            }
        }
        return nearestPark;
    }

}
