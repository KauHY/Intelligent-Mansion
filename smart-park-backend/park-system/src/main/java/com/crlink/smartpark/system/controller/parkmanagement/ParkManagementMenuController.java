package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuEditReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;
import com.crlink.smartpark.system.service.parkmanagement.ParkManagementMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 园区运营平台-菜单管理
 * */
@Slf4j
@RestController
@Api(tags = "园区运营平台-菜单管理")
@RequestMapping("/parkManagement/menu")
public class ParkManagementMenuController {


    @Autowired
    private ParkManagementMenuService parkManagementMenuService;


    /**
     * 菜单编辑
     */
    @ApiOperation(value = "菜单编辑", tags = "菜单管理")
    @PostMapping("/updateMenu")
    public ResponseResult<Boolean> updateMenu(@RequestBody @Valid MenuEditReqDTO menuEditReqDTO) {
        boolean result = parkManagementMenuService.editMenu(menuEditReqDTO);
        return ResponseResult.success(result);
    }

    /**
     * 更改状态
     *
     * @return 菜单/按钮表实体
     */
    @ApiOperation(value = "重置菜单", tags = "菜单管理")
    @PutMapping("/resetMenu")
    public ResponseResult<Boolean> resetMenu() {
        boolean result = parkManagementMenuService.resetMenu();
        return ResponseResult.success(result);
    }


    /**
     * 菜单侧边栏
     *
     * @return 分页数据
     */
    @ApiOperation(value = "菜单侧边栏", tags = "菜单管理")
    @GetMapping("/menuUrlList")
    public ResponseResult<List<MenuDTO>> menuUrlList() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<MenuDTO> menuDTOS = parkManagementMenuService.userMenuUrlList(userId);
        return ResponseResult.success(menuDTOS);
    }

    /**
     * 菜单列表页
     *
     * @return
     */

    @ApiOperation(value = "菜单列表页", tags = "菜单管理")
    @GetMapping("/menuListPage")
    public ResponseResult<List<MenuDTO>> menuListPage() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<MenuDTO> menus = parkManagementMenuService.menuPageList(userId);
        return ResponseResult.success(menus);
    }

    /**
     * 权限分配菜单树
     *
     * @return
     */
    @ApiOperation(value = "权限分配菜单树", tags = "菜单管理")
    @GetMapping("/menuTree")
    public ResponseResult<List<MenuTreeDTO>> menuTree() {
        String userId = SystemUtil.getInstance().getCurrentUserId();
        List<MenuTreeDTO> menus = parkManagementMenuService.listMenuTree(userId);
        return ResponseResult.success(menus);
    }


}

