package com.crlink.smartpark.system.service.platform;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.platform.TenantOrganizationVO;
import com.crlink.smartpark.system.dto.platform.TenantReqDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

public interface PlatformTenantOrganizationService {
    PageInfo<TenantOrganizationVO> listTenantPage(BaseQuery condition);

    @Transactional
    Boolean removeTenantById(String id);

    @ResetUserCache
    void create(TenantReqDTO tenantCreateReqDTO);

    void update(TenantReqDTO updateReqDTO);

    TenantOrganizationVO getTenantInfoById(String id);
}
