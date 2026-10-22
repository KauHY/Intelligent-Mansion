package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.system.dto.role.CreateRoleReqDTO;
import com.crlink.smartpark.system.dto.role.RoleVO;
import com.crlink.smartpark.system.query.RoleQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

public interface ParkManagementRoleService {
    PageInfo<DictVO> listPlatformRoles(RoleQuery roleQuery);

    @Transactional
    Boolean createOrUpdateSysRole(CreateRoleReqDTO reqDTO);

    PageInfo<RoleVO> pageRoles(RoleQuery roleQuery);
}
