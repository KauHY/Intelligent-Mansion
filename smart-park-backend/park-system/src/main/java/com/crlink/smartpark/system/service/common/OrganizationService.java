package com.crlink.smartpark.system.service.common;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.Organization;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 组织机构 服务类
 * </p>
 * */
public interface OrganizationService extends IService<Organization> {


    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Organization, ?> sValue);

    Map<String, ?> name2IdMap(Collection<String> names, String parkOrgId);

    /**
     * 根据租户id获取所有的组织ID
     *
     * @param headOrgId
     * @return
     */
    Set<String> getAllOrgIdByHeadOrgId(String headOrgId);
}
