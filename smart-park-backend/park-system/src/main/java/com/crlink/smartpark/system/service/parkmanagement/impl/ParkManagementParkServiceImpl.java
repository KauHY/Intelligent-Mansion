package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.OrgTypeEnum;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.park.ParkSaveDTO;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Park;
import com.crlink.smartpark.system.query.ParkListQuery;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementParkService;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.crlink.smartpark.system.vo.ParkDTO;
import com.crlink.smartpark.system.vo.ParkVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ParkManagementParkServiceImpl implements ParkManagementParkService {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private OrganizationManageService organizationManageService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeParkInfo(ParkSaveDTO parkSaveDTO) {
        String parkOrgId = parkSaveDTO.getParkOrgId();
        Organization organization = organizationService.lambdaQuery()
                .select(Organization::getId, Organization::getOrgName, Organization::getDescription, Organization::getHeadOrgId)
                .eq(Organization::getOrgType, OrgTypeEnum.PARK.getCode())
                .eq(Organization::getId, parkOrgId).one();
        AssertUtil.requireNotNullOrParamExp(organization);

        //校验同一租户园区名称不可重复
        if (!organization.getOrgName().equals(parkSaveDTO.getName())) {
            int count = organizationService.lambdaQuery().eq(Organization::getHeadOrgId, organization.getHeadOrgId()).eq(Organization::getOrgName, parkSaveDTO.getName())
                    .eq(Organization::getOrgType, OrgTypeEnum.PARK.getCode()).count();
            if (count > Constants.ZERO) {
                throw new ResolinkRuntimeException("20019");
            }
        }

        //更新组织表的园区信息
        organization.setOrgName(parkSaveDTO.getName());
        organization.setDescription(parkSaveDTO.getDescription());
        EntityUserUtil.setUpdateBy(organization);
        organizationService.updateById(organization);

        //更新园区信息表
        Park existPark = parkService.getOne(Wrappers.<Park>lambdaQuery().select(Park::getId).eq(Park::getParkOrgId, parkOrgId), false);
        Park park = new Park();
        BeanUtils.copyProperties(parkSaveDTO, park);
        park.setId(existPark.getId());
        park.setParkOrgId(parkOrgId);
        EntityUserUtil.setAll(park);
        if (existPark != null) {
            park.setCreateBy(null);
        }
        Boolean result = parkService.saveOrUpdate(park);
        return result;
    }

    @Override
    public PageInfo<ParkDTO> listParkPageByOrgId(ParkListQuery query) {
        Set<String> childParkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        query.setParkOrgIds(childParkOrgIds);
        if (CollectionUtil.isEmpty(childParkOrgIds)) {
            return new PageInfo<>();
        }
        query.startPage();
        List<ParkDTO> parkList = parkService.listParkByOrgIds(query);
        return new PageInfo<>(parkList);
    }

    @Override
    public ParkVO getParkInfoByOrgId(String parkOrgId) {
        Organization parkOrg = organizationService.lambdaQuery()
                .select(Organization::getId, Organization::getOrgName, Organization::getDescription, Organization::getParentId)
                .eq(Organization::getId, parkOrgId).one();
        Park park = parkService.lambdaQuery().eq(Park::getParkOrgId, parkOrgId).one();
        //获取上级组织名称
        String parentName = organizationService.lambdaQuery().select(Organization::getOrgName).eq(Organization::getId, parkOrg.getParentId()).one().getOrgName();
        ParkVO parkVO = new ParkVO();
        if (park != null) {
            BeanUtils.copyProperties(park, parkVO);
        }
        parkVO.setName(parkOrg.getOrgName());
        parkVO.setDescription(parkOrg.getDescription());
        parkVO.setParentOrgName(parentName);
        return parkVO;
    }
}
