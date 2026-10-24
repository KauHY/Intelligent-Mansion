package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.aop.ResetUserCache;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuEditReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import com.crlink.smartpark.system.entity.Menu;
import com.crlink.smartpark.system.service.common.BaseMenuService;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.*;

@Service
public class ParkManagementMenuServiceImpl extends BaseMenuService implements ParkManagementMenuService {


    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'parkManagementMenuService.echoParkMenu_roleId:'+#roleId")
    public Set<String> echoParkMenu(String roleId) {
        List<Menu> menus = super.getUserMenuList(SystemUtil.getInstance().getCurrentUserId());
        List<MenuTreeDTO> treeDTOS = super.listMenuTree(menus);
        Set<String> menuIds = super.echoMenus(roleId, treeDTOS);
        return menuIds;
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    @ResetUserCache
    public Boolean enableParkMenus(String roleId, Set<String> menuIds) {
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        Boolean result = super.enableMenus(headOrgId, roleId, menuIds);
        return result;
    }


    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'parkManagementMenuService.menuPageList_userId:'+#userId")
    public List<MenuDTO> menuPageList(String userId) {
        List<Menu> menus = super.getUserMenuList(userId);
        List<MenuDTO> result = super.menuPageList(menus);
        return result;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'parkManagementMenuService.userMenuUrlList_userId:'+#userId")
    public List<MenuDTO> userMenuUrlList(String userId) {
        List<Menu> menus = super.getUserMenuList(userId);
        List<MenuDTO> result = super.getUserMenuUrlIgnoreButton(menus);
        return result;
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean editMenu(MenuEditReqDTO menuEditReqDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuEditReqDTO, menu);
        EntityUserUtil.setUpdateBy(menu);
        return menuService.updateById(menu);
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_MENU, key = "'parkManagementMenuService.listMenuTree_userId:'+#userId")
    public List<MenuTreeDTO> listMenuTree(String userId) {
        List<Menu> menus = super.getUserMenuList(SystemUtil.getInstance().getCurrentUserId());
        List<MenuTreeDTO> treeDTOS = super.listMenuTree(menus);
        return treeDTOS;
    }

    @Override
    @CacheEvict(value = {CACHE_GROUP_USER, CACHE_GROUP_MENU, CACHE_GROUP_ROLE}, allEntries = true)
    public boolean resetMenu() {
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        List<Menu> curTenantMenus = menuService.lambdaQuery().eq(Menu::getHeadOrgId, headOrgId).list();
        Set<String> originIds = curTenantMenus.parallelStream().map(t -> t.getOriginId()).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(originIds)) {
            return true;
        }
        Map<String, Menu> templateMenusMap = menuService.lambdaQuery().in(Menu::getId, originIds).list().parallelStream().collect(Collectors.toMap(Menu::getId, Function.identity()));
        List<Menu> newMenus = curTenantMenus.stream().map(t -> {
            Menu templateMenus = templateMenusMap.get(t.getOriginId());
            t.setName(templateMenus.getName());
            t.setIcon(templateMenus.getIcon());
            t.setSort(templateMenus.getSort());
            t.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
            return t;
        }).collect(Collectors.toList());
        return menuService.saveOrUpdateBatch(newMenus);
    }


}
