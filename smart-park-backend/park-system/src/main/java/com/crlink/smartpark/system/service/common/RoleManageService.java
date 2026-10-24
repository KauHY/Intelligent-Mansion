package com.crlink.smartpark.system.service.common;


import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface RoleManageService {


    @Transactional
    Boolean deleteRole(String roleId);

    boolean updateStatus(UpdateStatusReqDTO reqDTO);

    PageInfo<RoleVO> pageRoles(Set<String> orgIds, RoleQuery roleQuery);

    PageInfo<DictVO> listRoleDict(Set<String> orgIds, RoleQuery roleQuery);

    boolean saveOrUpdateRole(Role role, List<String> userIds);

    void cannotUpdateOrDeleteAdminRole(String roleId);
}
