package com.crlink.smartpark.system.service.common;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.enums.MenuTypeEnum;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import com.crlink.smartpark.system.entity.Menu;
import com.crlink.smartpark.system.entity.Permission;
import com.crlink.smartpark.system.entity.Role;
import com.crlink.smartpark.system.entity.RsUserRoleRelation;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.HEAD_MENU_PARENT_ID;
import static com.crlink.smartpark.common.constants.enums.PermissionTypeEnum.MENU;
import static com.crlink.smartpark.common.constants.enums.StatusEnum.ENABLE;

public abstract class BaseMenuService {
    @Autowired
    public MenuService menuService;

    @Autowired
    public RsUserRoleRelationService userRoleRelationService;

    @Autowired
    public PermissionService permissionService;

    @Autowired
    public RoleService roleService;

    /**
     * 当前用户的所有菜单Id
     *
     * @param userId
     * @return
     */
    public Set<String> getUserMenuIds(String userId) {
        Set<String> menuIds = Collections.emptySet();
        Set<String> userRoleIds = userRoleRelationService.lambdaQuery()
                .select(RsUserRoleRelation::getRoleId)
                .eq(RsUserRoleRelation::getUserId, userId)
                .list().parallelStream().map(RsUserRoleRelation::getRoleId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(userRoleIds)) {
            return menuIds;
        }
        Set<String> enableRoleIds = roleService.lambdaQuery()
                .select(Role::getId).eq(Role::getStatus, ENABLE.getCode())
                .in(Role::getId, userRoleIds)
                .list().parallelStream().map(Role::getId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(enableRoleIds)) {
            return menuIds;
        }
        menuIds = permissionService.lambdaQuery()
                .select(Permission::getDestId)
                .eq(Permission::getPermissionType, MENU.getType())
                .in(Permission::getRoleId, enableRoleIds)
                .eq(Permission::getStatus, ENABLE.getCode()).list().parallelStream().map(Permission::getDestId).collect(Collectors.toSet());
        return menuIds;
    }

    /**
     * 当前用户的所有菜单
     *
     * @param userId
     * @return
     */
    public List<Menu> getUserMenuList(String userId) {
        Set<String> userMenuIds = this.getUserMenuIds(userId);
        if (userMenuIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Menu> menus = menuService.lambdaQuery()
                .in(Menu::getId, userMenuIds)
                .orderByAsc(Menu::getSort).list();

        List<String> originIds = menus.parallelStream().filter(menu -> menu.getOriginId() != null).map(Menu::getOriginId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(originIds)) {
            Map<String, Menu> id2MenuMap = menuService.lambdaQuery().in(Menu::getId, originIds).list().parallelStream().collect(Collectors.toMap(Menu::getId, Function.identity()));
            completeMenuInfo(menus, id2MenuMap);
        }
        return menus;
    }

    public List<MenuDTO> getUserMenuUrlIgnoreButton(List<Menu> menus) {
        List<MenuDTO> result = menus.parallelStream().filter(m -> !Objects.equals(MenuTypeEnum.BUTTON.getType(), m.getType()))
                .map(t -> {
                    MenuDTO menuDTO = new MenuDTO();
                    BeanUtils.copyProperties(t, menuDTO);
                    return menuDTO;
                })
                .sorted(Comparator.comparing(MenuDTO::getSort))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 菜单树，用于权限分配展示树结构
     *
     * @param menus
     * @return
     */
    public List<MenuTreeDTO> listMenuTree(List<Menu> menus) {
        Map<String, List<Menu>> menuGroup = menus.stream().collect(Collectors.groupingBy(Menu::getParentId));
        List<Menu> headMenus = menuGroup.get(HEAD_MENU_PARENT_ID);
        if (CollectionUtil.isEmpty(headMenus)) {
            return Collections.emptyList();
        }
        List<MenuTreeDTO> menuTreeDTOS = headMenus.stream().map(t -> {
            MenuTreeDTO treeDTO = new MenuTreeDTO();
            BeanUtils.copyProperties(t, treeDTO);
            return recursion(treeDTO, menuGroup);
        }).collect(Collectors.toList());
        return menuTreeDTOS;
    }

    /**
     * 分配菜单权限
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    public Boolean enableMenus(String headOrgId, String roleId, Set<String> menuIds) {
        // 菜单上级菜单加进去
        enableParentMenu(headOrgId, menuIds);
        //清除之前改角色权限记录
        LambdaQueryWrapper<Permission> removeQuery = Wrappers.<Permission>lambdaQuery().eq(Permission::getRoleId, roleId).eq(Permission::getPermissionType, MENU.getType());
        permissionService.remove(removeQuery);
        //添加菜单上级菜单
        List<Permission> permissions = menuIds.stream().map(t -> {
            Permission permission = new Permission();
            permission.setDestId(t);
            permission.setRoleId(roleId);
            permission.setPermissionType(MENU.getType());
            permission.setStatus(ENABLE.getCode());
            EntityUserUtil.setAll(permission);
            return permission;
        }).collect(Collectors.toList());
        return permissionService.saveBatch(permissions);
    }

    public Set<String> echoMenus(String roleId, List<MenuTreeDTO> menuTree) {
        Set<String> menuIds = permissionService.lambdaQuery()
                .select(Permission::getDestId)
                .eq(Permission::getRoleId, roleId)
                .eq(Permission::getPermissionType, MENU.getType())
                .list().parallelStream().map(Permission::getDestId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(menuIds)) {
            return Collections.emptySet();
        }
        Map<String, Set<String>> parentId2IdMap = menuService.lambdaQuery().select(Menu::getId, Menu::getParentId).in(Menu::getId, menuIds)
                .list().parallelStream().collect(Collectors.groupingBy(Menu::getParentId, Collectors.mapping(Menu::getId, Collectors.toSet())));

        this.recursiveDeleteParentMenuId(menuIds, menuTree, parentId2IdMap);
        //todo
        menuIds.remove("0");
        return menuIds;
    }

    /**
     * 将菜单加入权限
     *
     * @param headOrgId
     * @param menuIds
     */
    private void enableParentMenu(String headOrgId, Set<String> menuIds) {
        if (CollectionUtil.isNotEmpty(menuIds)) {
            Set<String> parentIds = menuService.lambdaQuery()
                    .select(Menu::getId, Menu::getParentId)
                    .eq(Menu::getHeadOrgId, headOrgId)
                    .in(Menu::getId, menuIds).list().parallelStream().map(Menu::getParentId).collect(Collectors.toSet());
            //递归，查找上级是否还有上级
            if (menuIds.addAll(parentIds)) {
                enableParentMenu(headOrgId, menuIds);
            }

        }
    }


    public List<MenuDTO> menuPageList(List<Menu> menus) {

        List<MenuDTO> menuDTOList = menus.parallelStream().map(menu -> {
            MenuDTO dto = new MenuDTO();
            BeanUtils.copyProperties(menu, dto);
            return dto;
        }).collect(Collectors.toList());
        Map<String, List<MenuDTO>> dtoParentGroup = menuDTOList.parallelStream().collect(Collectors.groupingBy(MenuDTO::getParentId));
        List<MenuDTO> result = dtoParentGroup.get(HEAD_MENU_PARENT_ID);
        recursion(dtoParentGroup, result);
        return result;
    }


    public void completeMenuInfo(List<Menu> menus, Map<String, Menu> originMenuMap) {
        menus.forEach(m -> {
            if (m.getOriginId() != null) {
                Menu originMenu = originMenuMap.get(m.getOriginId());
                //todo originMenu
                if (originMenu != null) {
                    m.setApiUrl(originMenu.getApiUrl());
                    m.setRoute(originMenu.getRoute());
                    m.setType(originMenu.getType());
                    m.setRouteName(originMenu.getRouteName());
                }

            }
        });

    }

    /**
     * 菜单回显时如果上级菜单没有勾选全部菜单，把上级菜单节点移除
     *
     * @param menuIds
     * @param menus
     * @param parentId2IdMap
     */
    private void recursiveDeleteParentMenuId(Set<String> menuIds, List<MenuTreeDTO> menus, Map<String, Set<String>> parentId2IdMap) {
        menus.forEach(menu -> {
            List<MenuTreeDTO> children = menu.getChildren();
            if (CollectionUtil.isNotEmpty(children)) {
                Set<String> existMenuGroup = parentId2IdMap.getOrDefault(menu.getId(), Sets.newHashSet());
                if (children.size() != existMenuGroup.size()) {
                    menuIds.remove(menu.getId());
                }
                recursiveDeleteParentMenuId(menuIds, children, parentId2IdMap);
            }
        });
    }


    private void recursion(Map<String, List<MenuDTO>> dtoParentGroup, List<MenuDTO> menuDTOs) {
        if (CollectionUtil.isNotEmpty(menuDTOs)) {
            menuDTOs.forEach(dto -> {
                List<MenuDTO> children = dtoParentGroup.get(dto.getId());
                recursion(dtoParentGroup, children);
                dto.setChildren(children);
            });
        }
    }

    private MenuTreeDTO recursion(MenuTreeDTO menuTreeDTO, Map<String, List<Menu>> menuGroup) {
        List<Menu> list = menuGroup.get(menuTreeDTO.getId());
        if (CollectionUtil.isNotEmpty(list)) {
            List<MenuTreeDTO> children = list.stream().map(t -> {
                MenuTreeDTO treeDTO = new MenuTreeDTO();
                treeDTO.setId(t.getId());
                treeDTO.setParentId(t.getParentId());
                treeDTO.setName(t.getName());
                recursion(treeDTO, menuGroup);
                return treeDTO;
            }).collect(Collectors.toList());
            menuTreeDTO.setChildren(children);
        }
        return menuTreeDTO;
    }

}
