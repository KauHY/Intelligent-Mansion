package com.crlink.smartpark.system.service.platform;

import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;

import java.util.List;
import java.util.Set;

/**
 * @Description 租户管理平台系统菜单
 * @Created by ZHOUXINJIAN3
 */
public interface PlatformSysMenuService {
    void createSysMenu(MenuReqDTO menuCreateReqDTO);

    List<MenuDTO> sysMenuUrlList(String userId);

    List<MenuTreeDTO> listMenuTree(String userId);

    List<MenuDTO> menuListPage(String userId);

    Boolean updateMenu(MenuReqDTO menuCreateReqDTO);

    Boolean deleteSysMenu(String id);

    Set<String> echoSysMenu(String roleId);

    Boolean enableSysMenus(String roleId, Set<String> destIds);
}
