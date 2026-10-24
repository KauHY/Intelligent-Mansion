package com.crlink.smartpark.system.controller.platform;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import com.crlink.smartpark.system.service.platform.PlatformTenantMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 平台-租户菜单管理
 * */
@Slf4j
@RestController
@Api(tags = "平台-租户菜单管理")
@RequestMapping("/platform/menu")
public class PlatformTenantMenuController {


    @Autowired
    private PlatformTenantMenuService platformTenantMenuService;

    /**
     * 新增菜单
     */
    @ApiOperation(value = "新增菜单", tags = "租户菜单管理")
    @PostMapping("/createMenu")
    public ResponseResult create(@RequestBody @Valid MenuReqDTO menuCreateReqDTO) {
        menuCreateReqDTO.setId(null);
        platformTenantMenuService.createMenu(menuCreateReqDTO);
        return ResponseResult.success();
    }


    /**
     * 编辑菜单
     */
    @ApiOperation(value = "编辑菜单", tags = "租户菜单管理")
    @PostMapping("/update")
    public ResponseResult<Boolean> updateMenu(@RequestBody @Valid MenuReqDTO updateReqDTO) {
        boolean result = platformTenantMenuService.updateMenu(updateReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单", tags = "租户菜单管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = platformTenantMenuService.deleteTenantMenu(id);
        return ResponseResult.success(result);
    }

    /**
     * 租户菜单列表页
     */
    @ApiOperation(value = "租户菜单列表页", tags = "租户菜单管理")
    @GetMapping("/listTenantMenus")
    public ResponseResult<List<MenuDTO>> listTenantMenus() {
        List<MenuDTO> menuDTOS = platformTenantMenuService.listTenantTemplateMenus();
        return ResponseResult.success(menuDTOS);
    }

    /**
     * 租户权限分配时菜单树
     */
    @ApiOperation(value = "租户权限分配时菜单树", tags = "租户菜单管理")
    @GetMapping("/tenantMenuTemplateTree/{headOrgId}")
    public ResponseResult<List<MenuTreeDTO>> tenantMenuTemplateTree(@PathVariable("headOrgId") String headOrgId) {
        List<MenuTreeDTO> tree = platformTenantMenuService.listTenantTemplateMenuTree(headOrgId);
        return ResponseResult.success(tree);
    }

    /**
     * 租户权限分配
     */
    @ApiOperation(value = "租户权限分配", tags = "平台系统菜单管理-权限分配")
    @PostMapping("/enableTenantMenu/{headOrgId}")
    public ResponseResult<Boolean> menuEnablePermission(@PathVariable("headOrgId") String headOrgId, @RequestBody Set<String> menuIds) {
        Boolean result = platformTenantMenuService.enableTenantMenu(headOrgId, menuIds);
        return ResponseResult.success(result);
    }

    /**
     * 租户权限分配菜单回显示
     */
    @ApiOperation(value = "租户权限分配菜单回显示", tags = "平台系统菜单管理-权限分配")
    @GetMapping("/echoTenantMenus/{headOrgId}")
    public ResponseResult<Set<String>> echoTenantMenus(@PathVariable("headOrgId") String headOrgId) {
        Set<String> result = platformTenantMenuService.echoTenantMenus(headOrgId);
        return ResponseResult.success(result);
    }


}

