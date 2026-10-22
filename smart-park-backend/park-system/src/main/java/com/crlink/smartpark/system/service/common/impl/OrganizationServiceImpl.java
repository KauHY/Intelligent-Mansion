package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.mapper.OrganizationMapper;
import com.crlink.smartpark.system.service.common.OrganizationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织机构 服务实现类
 * </p>
 * */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {


    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Organization, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> id2ValueMap = this.lambdaQuery()
                .select(Organization::getId, sValue)
                .in(Organization::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(Organization::getId, sValue));
        return id2ValueMap;
    }

    @Override
    public Map<String, ?> name2IdMap(Collection<String> names, String parkOrgId) {
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        Map<String, String> name2IdMap = this.lambdaQuery()
                .select(Organization::getId, Organization::getOrgName)
                .eq(Organization::getHeadOrgId, headOrgId)
                .eq(StringUtils.isNotBlank(parkOrgId), Organization::getId, parkOrgId)
                .in(CollectionUtil.isNotEmpty(names), Organization::getId, names)
                .list().parallelStream().collect(Collectors.toMap(Organization::getOrgName, Organization::getId, (k1, k2) -> k2));
        return name2IdMap;
    }


    @Override
    public Set<String> getAllOrgIdByHeadOrgId(String headOrgId) {
        Set<String> result = this.lambdaQuery().select(Organization::getId).eq(Organization::getHeadOrgId, headOrgId)
                .list().parallelStream()
                .map(Organization::getId).collect(Collectors.toSet());
        return result;
    }

}
