package com.crlink.smartpark.system.service.common;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.organization.OrgDTO;
import com.crlink.smartpark.system.dto.organization.OrgRespVO;
import com.crlink.smartpark.system.dto.organization.OrgStructureVO;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.vo.DictVO;

import java.util.List;
import java.util.Set;

public interface OrganizationManageService {


    Set<String> getAllParkOrgIdsByOrgId(String orgId,String userId);

    Set<String> listOrgIdsByUserId(String userId);

    Set<String> listParkOrgIdsByUserId(String userId);

    //Set<String> listParkIdsByUserId(String userId);

    List<Organization> listChildOrganizationByUserId(String orgId,String userId);

    List<OrgDTO> listOrgStructureByUserId(String parentId);

    OrgStructureVO listOrganization(BaseQuery<String> query);

    Set<Organization> listOrganizationByUserId(String userId);

    //Set<Organization> listStructureByRole(String roleId);

    Boolean updateStatus(UpdateStatusReqDTO statusDTO);

    Boolean removeUserRelationInfoByOrgId(String orgId);

    Set<String> getTenantParkOrgIds(Set<String> tenantOrgId);

    Set<String> getTenantParkOrgIds();

    OrgRespVO getOrganizationById(String id);

    List<DictVO> getTenantParkOrgDict(String headOrgId);
}
