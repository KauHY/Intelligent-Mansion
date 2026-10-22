package com.crlink.smartpark.system.service.platform.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.enums.PermissionTypeEnum;
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
import com.crlink.smartpark.system.service.platform.PlatformTenantMenuService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.*;
import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.enums.PermissionTypeEnum.MENU;
import static com.crlink.smartpark.common.constants.enums.StatusEnum.ENABLE;

@Service
public class PlatformTenantMenuServiceImpl extends BaseMenuService implements PlatformTenantMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean createMenu(MenuReqDTO menuCreateReqDTO) {
        String parentId = menuCreateReqDTO.getParentId();
        if (StringUtils.isBlank(parentId)) {
            parentId = HEAD_MENU_PARENT_ID;
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuCreateReqDTO, menu);
        EntityUserUtil.setAll(menu);
        menu.setHeadOrgId(PLATFORM_ORG_ID);
        menu.setParentId(parentId);
        menuService.save(menu);
        //更新租户菜单
        List<Menu> copyMenus = listTenantMenusAfterReset(menu, new String[]{"id", "parentId", "apiUrl", "type", "route", "routeName"});
        menuService.saveOrUpdateBatch(copyMenus);
        //为内置超级管理员内置菜单
        Permission permission = new Permission();
        permission.setRoleId(PLATFORM_ADMIN_ROLE_ID);
        permission.setPermissionType(MENU.getType());
        permission.setDestId(menu.getId());
        permission.setStatus(ENABLE.getCode());
        permissionService.save(permission);
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean updateMenu(MenuReqDTO updateReqDTO) {
//     查询的时候体现，园区平台管理员对菜单的父菜单、类型、地址的修改，同步给所有租户；对菜单名称、顺序、图标、状态的修改，不同步给租户,查询的时候体现
        Menu menu = new Menu();
        BeanUtils.copyProperties(updateReqDTO, menu);
        menu.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        menuService.updateById(menu);

        //更新租户菜单
        List<Menu> copyMenus = listTenantMenusAfterReset(menu, new String[]{"id", "parentId", "apiUrl", "type", "route", "routeName"});
        menuService.saveOrUpdateBatch(copyMenus);
        return true;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'listTenantTemplateMenuTree_tenantId:'+#tenantId")
    public List<MenuTreeDTO> listTenantTemplateMenuTree(String tenantId) {
        //租户模板菜单
        List<Menu> menus = menuService.lambdaQuery().eq(Menu::getHeadOrgId, tenantId).orderByAsc(Menu::getSort).list();
        List<MenuTreeDTO> menuTreeDTOS = super.listMenuTree(menus);
        return menuTreeDTOS;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'listTenantTemplateMenus'")
    public List<MenuDTO> listTenantTemplateMenus() {
        List<Menu> menus = menuService.lambdaQuery().eq(Menu::getHeadOrgId, PLATFORM_ORG_ID).orderByAsc(Menu::getSort).list();
        List<MenuDTO> result = super.menuPageList(menus);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean deleteTenantMenu(String menuId) {
        //若该菜单有子菜单、不可删除
        Menu child = menuService.lambdaQuery().select(Menu::getId).eq(Menu::getParentId, menuId).last("limit 1").one();
        if (Objects.nonNull(child)) {
            throw new ResolinkRuntimeException("10027");
        }
        LambdaUpdateWrapper<Permission> lambdaUpdateWrapper = new LambdaUpdateWrapper<Permission>().eq(Permission::getDestId, menuId)
                .eq(Permission::getPermissionType, PermissionTypeEnum.MENU.getType());
        permissionService.remove(lambdaUpdateWrapper);
        //String keyMenu = String.format(USER_MENU_LIST_KEY, SystemUtil.getInstance().getCurrentUserId());
        //redisTemplate.delete(keyMenu);
        //删除租户菜单
        menuService.remove(Wrappers.<Menu>lambdaQuery().eq(Menu::getOriginId, menuId));
        return menuService.removeById(menuId);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public Boolean enableTenantMenu(String headOrgId, Set<String> menuIds) {
        String adminRoleId = roleService.lambdaQuery().select(Role::getId).eq(Role::getHeadOrgId, headOrgId).eq(Role::getAdminFlag, true).one().getId();
        return super.enableMenus(headOrgId, adminRoleId, menuIds);

    }

    @Override
    public Set<String> echoTenantMenus(String headOrgId) {
        String adminRoleId = roleService.lambdaQuery().eq(Role::getHeadOrgId, headOrgId).eq(Role::getAdminFlag, true).one().getId();
        List<MenuTreeDTO> menuTree = this.listTenantTemplateMenuTree(headOrgId);
        Set<String> menuIds = super.echoMenus(adminRoleId, menuTree);
        return menuIds;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean removeMenusForTenant(String tenantId) {
        boolean result = menuService.remove(Wrappers.<Menu>lambdaQuery().eq(Menu::getHeadOrgId, tenantId));
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean copeMenusForTenant(String tenantId) {
        List<Menu> menus = menuService.lambdaQuery().select(Menu::getId, Menu::getParentId, Menu::getName, Menu::getSort, Menu::getIcon)
                .eq(Menu::getHeadOrgId, PLATFORM_ORG_ID).list();
        List<Menu> menuEntities = menus.stream().map(t -> {
            Menu menu = new Menu();
            BeanUtils.copyProperties(t, menu, "id");
            EntityUserUtil.setAll(menu);
            menu.setHeadOrgId(tenantId);
            menu.setOriginId(t.getId());
            return menu;
        }).collect(Collectors.toList());
        menuService.saveOrUpdateBatch(menuEntities);
        //设置菜单的parentId
        List<Menu> tenantMenus = menuService.lambdaQuery().select().eq(Menu::getHeadOrgId, tenantId).list();
        Map<String, String> originId2IdMap = tenantMenus.parallelStream().collect(Collectors.toMap(Menu::getOriginId, Menu::getId));
        //更新parentId
        tenantMenus = tenantMenus.parallelStream().filter(t -> !Objects.equals(t.getParentId(), HEAD_MENU_PARENT_ID)).map(t -> {
            String afterParentId = originId2IdMap.get(t.getParentId());
            t.setParentId(afterParentId);
            return t;
        }).collect(Collectors.toList());
        menuService.saveOrUpdateBatch(tenantMenus);
        return true;
    }

    private List<Menu> listTenantMenusAfterReset(Menu templateMenu, String[] ignoreProperties) {
        Set<String> headOrgIds = menuService.lambdaQuery()
                .select(Menu::getHeadOrgId).notIn(Menu::getHeadOrgId, Lists.newArrayList(PLATFORM_ORG_ID, PLATFORM_SYS_MENU_ORG_ID))
                .groupBy(Menu::getHeadOrgId).list().stream().map(t -> t.getHeadOrgId()).collect(Collectors.toSet());
        String finalParentId = templateMenu.getParentId();
        List<Menu> copyMenus = headOrgIds.stream().map(t -> {
            Menu copyMenu = menuService.getOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getHeadOrgId, t).eq(Menu::getOriginId, templateMenu.getId()), false);
            if (copyMenu != null) {
                EntityUserUtil.setUpdateBy(copyMenu);
            } else {
                copyMenu = new Menu();
                EntityUserUtil.setAll(copyMenu);
            }
            BeanUtils.copyProperties(templateMenu, copyMenu, ignoreProperties);
            //设置ParentId
            String selfParentId = finalParentId;
            if (!Objects.equals(finalParentId, HEAD_MENU_PARENT_ID)) {
                selfParentId = menuService.lambdaQuery().select(Menu::getId).eq(Menu::getHeadOrgId, t).eq(Menu::getOriginId, finalParentId).one().getId();
            }
            copyMenu.setParentId(selfParentId);
            copyMenu.setHeadOrgId(t);
            copyMenu.setOriginId(templateMenu.getId());
            return copyMenu;
        }).collect(Collectors.toList());
        return copyMenus;
    }

}
