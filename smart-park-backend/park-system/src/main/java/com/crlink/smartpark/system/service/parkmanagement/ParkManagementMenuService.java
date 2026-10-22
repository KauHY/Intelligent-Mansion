package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.menu.MenuEditReqDTO;
import com.crlink.smartpark.system.dto.menu.MenuTreeDTO;

import java.util.List;
import java.util.Set;

public interface ParkManagementMenuService {

    Set<String> echoParkMenu(String roleId);

    List<MenuDTO> menuPageList(String userId);

    List<MenuDTO> userMenuUrlList(String userId);

    boolean editMenu(MenuEditReqDTO menuEditReqDTO);

    List<MenuTreeDTO> listMenuTree(String userId);

    boolean resetMenu();

    Boolean enableParkMenus(String roleId, Set<String> menuIds);
}
