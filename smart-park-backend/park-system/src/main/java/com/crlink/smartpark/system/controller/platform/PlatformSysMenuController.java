package com.crlink.smartpark.system.controller.platform;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.PermissionReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import com.crlink.smartpark.system.service.common.RoleManageService;
import com.crlink.smartpark.system.service.platform.PlatformSysMenuService;
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
 * 平台-系统菜单管理
 *
 * @Description
 * @Created by ZHOUXINJIAN3
 */
@Slf4j
@RestController
@Api(tags = "平台-系统菜单管理")
@RequestMapping("/platform/sys/menu")
public class PlatformSysMenuController {
    @Autowired
    private PlatformSysMenuService platformSysMenuService;

    @Autowired
    private RoleManageService roleManageService;

    /**
     * 新增菜单
     */
    @ApiOperation(value = "新增菜单", tags = "平台系统菜单管理")
    @PostMapping("/createMenu")
    public ResponseResult<Boolean> create(@RequestBody @Valid MenuReqDTO menuCreateReqDTO) {
        menuCreateReqDTO.setId(null);
        platformSysMenuService.createSysMenu(menuCreateReqDTO);
        return ResponseResult.success();
    }


    /**
     * 编辑菜单
     */
    @ApiOperation(value = "编辑菜单", tags = "平台系统菜单管理")
    @PostMapping("/updateMenu")
    public ResponseResult<MenuReqDTO> updateMenu(@RequestBody @Valid MenuReqDTO menuCreateReqDTO) {
        platformSysMenuService.updateMenu(menuCreateReqDTO);
        return ResponseResult.success(menuCreateReqDTO);
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单", tags = "租户菜单管理")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = platformSysMenuService.deleteSysMenu(id);
        return ResponseResult.success(result);
    }


    /**
     * 系统侧边栏菜单列表
     *
     * @return
     */
    @ApiOperation(value = "系统侧边栏菜单列表", tags = "平台系统菜单管理")
    @GetMapping("/sysMenuList")
    public ResponseResult<List<MenuDTO>> sysMenuUrlList() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<MenuDTO> result = platformSysMenuService.sysMenuUrlList(userId);
        return ResponseResult.success(result);
    }

    /**
     * 菜单列表页面
     *
     * @return
     */
    @ApiOperation(value = "菜单列表页面", tags = "平台系统菜单管理")
    @GetMapping("/menuList")
    public ResponseResult<List<MenuDTO>> menuListPage() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<MenuDTO> result = platformSysMenuService.menuListPage(userId);
        return ResponseResult.success(result);
    }

    /**
     * 当前用户的系统菜单树
     *
     * @return
     */
    @ApiOperation(value = "当前用户的系统菜单树", tags = "平台系统菜单管理")
    @GetMapping("/menuTree")
    public ResponseResult<List<MenuTreeDTO>> menuTree() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<MenuTreeDTO> menus = platformSysMenuService.listMenuTree(userId);
        return ResponseResult.success(menus);
    }

    /**
     * 系统菜单树回显
     *
     * @return
     */
    @ApiOperation(value = "系统菜单树回显", tags = "平台系统菜单管理")
    @GetMapping("/echoSysMenuData/{roleId}")
    public ResponseResult<Set<String>> echoSysMenuData(@PathVariable("roleId") String roleId) {
        Set<String> menus = platformSysMenuService.echoSysMenu(roleId);
        //Set<String> menus = permissionManageService.echoMenu(roleId);
        return ResponseResult.success(menus);
    }

    /**
     * 系统菜单分配
     */
    @ApiOperation(value = "系统菜单分配", tags = "平台系统菜单管理-权限分配")
    @PostMapping("/enable/permission")
    public ResponseResult<Boolean> menuEnablePermission(@RequestBody PermissionReqDTO permissionDTO) {
        roleManageService.cannotUpdateOrDeleteAdminRole(permissionDTO.getRoleId());
        Boolean result = platformSysMenuService.enableSysMenus(permissionDTO.getRoleId(), permissionDTO.getDestIds());
        //Boolean result = permissionManageService.menuEnablePermission(PLATFORM_SYS_MENU_ORG_ID, permissionDTO.getRoleId(), permissionDTO.getDestIds());
        return ResponseResult.success(result);
    }
}
