package com.crlink.smartpark.system.service.platform.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import com.crlink.smartpark.system.entity.Menu;
import com.crlink.smartpark.system.entity.Permission;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.service.common.BaseMenuService;
import com.crlink.smartpark.system.service.platform.PlatformSysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.enums.PermissionTypeEnum.MENU;
import static com.crlink.smartpark.common.constants.enums.StatusEnum.ENABLE;

@Service
public class PlatformSysMenuServiceImpl extends BaseMenuService implements PlatformSysMenuService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public void createSysMenu(MenuReqDTO menuCreateReqDTO) {
        String parentId = menuCreateReqDTO.getParentId();
        if (StringUtils.isBlank(parentId)) {
            parentId = HEAD_MENU_PARENT_ID;
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuCreateReqDTO, menu);
        menu.setHeadOrgId(PLATFORM_SYS_MENU_ORG_ID);
        menu.setParentId(parentId);
        //menu.setStatus(StatusEnum.ENABLE.getCode());
        EntityUserUtil.setAll(menu);
        menuService.save(menu);
        //为内置超级管理员内置菜单
        String adminRoleId = roleService.lambdaQuery().select(Role::getId).eq(Role::getOrgId, PLATFORM_ORG_ID).eq(Role::getAdminFlag, true).one().getId();
        Permission permission = new Permission();
        permission.setRoleId(adminRoleId);
        permission.setPermissionType(MENU.getType());
        permission.setDestId(menu.getId());
        permission.setStatus(ENABLE.getCode());
        permissionService.save(permission);
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'sysMenuUrlList_userId:'+#userId")
    public List<MenuDTO> sysMenuUrlList(String userId) {
        Set<String> userSysMenuIds = super.getUserMenuIds(userId);
        if (CollectionUtil.isEmpty(userSysMenuIds)) {
            return Collections.emptyList();
        }
        List<Menu> menus = menuService.lambdaQuery().eq(Menu::getHeadOrgId, PLATFORM_SYS_MENU_ORG_ID).in(Menu::getId, userSysMenuIds).orderByAsc(Menu::getSort).list();
        List<MenuDTO> result = getUserMenuUrlIgnoreButton(menus);
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'platformSysMenuService.menuListPage_userId:'+#userId")
    public List<MenuDTO> menuListPage(String userId) {
        Set<String> userMenuIds = super.getUserMenuIds(userId);
        if (CollectionUtil.isEmpty(userMenuIds)) {
            return Collections.emptyList();
        }
        List<Menu> menus = menuService.lambdaQuery().eq(Menu::getHeadOrgId, PLATFORM_SYS_MENU_ORG_ID).in(Menu::getId, userMenuIds).orderByAsc(Menu::getSort).list();
        List<MenuDTO> result = super.menuPageList(menus);
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'platformSysMenuService.listMenuTree_userId:'+#userId")
    public List<MenuTreeDTO> listMenuTree(String userId) {
        Set<String> userSysMenuIds = super.getUserMenuIds(userId);
        if (CollectionUtil.isEmpty(userSysMenuIds)) {
            return Collections.emptyList();
        }
        List<Menu> menus = menuService.lambdaQuery().eq(Menu::getHeadOrgId, PLATFORM_SYS_MENU_ORG_ID).in(Menu::getId, userSysMenuIds).orderByAsc(Menu::getSort).list();
        List<MenuTreeDTO> menuTreeDTOS = super.listMenuTree(menus);
        return menuTreeDTOS;
    }


    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean updateMenu(MenuReqDTO menuCreateReqDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuCreateReqDTO, menu);
        EntityUserUtil.setUpdateBy(menu);
        return menuService.updateById(menu);
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean deleteSysMenu(String id) {
        //若该菜单有子菜单、不可删除
        Menu child = menuService.lambdaQuery().select(Menu::getId).eq(Menu::getParentId, id).last("limit 1").one();
        if (Objects.nonNull(child)) {
            throw new ResolinkRuntimeException("10027");
        }
        return menuService.removeById(id);
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'platformSysMenuService.echoSysMenu_roleId:'+#roleId")
    public Set<String> echoSysMenu(String roleId) {
        List<MenuTreeDTO> menus = this.listMenuTree(SystemUtil.getInstance().getCurrentUserId());
        Set<String> menuIds = super.echoMenus(roleId, menus);
        return menuIds;
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean enableSysMenus(String roleId, Set<String> destIds) {
        return super.enableMenus(PLATFORM_SYS_MENU_ORG_ID, roleId, destIds);
    }

}
