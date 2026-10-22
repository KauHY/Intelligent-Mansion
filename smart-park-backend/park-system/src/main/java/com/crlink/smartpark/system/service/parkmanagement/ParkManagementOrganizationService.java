package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.organization.OrgDTO;
import com.crlink.smartpark.system.dto.organization.OrgQuery;
import com.crlink.smartpark.system.dto.organization.OrgRespVO;
import com.github.pagehelper.PageInfo;

public interface ParkManagementOrganizationService {
    @ResetUserCache
    boolean createOrganization(OrgDTO orgDTO);

    @ResetUserCache
    boolean updateOrganization(OrgDTO orgDTO);

    Boolean removeOrganizationById(String id);

    PageInfo<OrgRespVO> listOrganizationPage(OrgQuery query);
}
