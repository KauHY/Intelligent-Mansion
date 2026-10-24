package com.crlink.smartpark.system.service.platform;

import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface PlatformTenantMenuService {
    @Transactional
    boolean createMenu(MenuReqDTO menuCreateReqDTO);

    boolean updateMenu(MenuReqDTO updateReqDTO);

    List<MenuTreeDTO> listTenantTemplateMenuTree(String tenantId);

    List<MenuDTO> listTenantTemplateMenus();

    @Transactional
    boolean deleteTenantMenu(String menuId);

    Boolean enableTenantMenu(String headOrgId, Set<String> menuIds);

    Set<String> echoTenantMenus(String headOrgId);

    @Transactional
    boolean removeMenusForTenant(String tenantId);

    @Transactional
    boolean copeMenusForTenant(String tenantId);
}
