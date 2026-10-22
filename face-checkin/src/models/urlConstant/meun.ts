/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 13:59:17
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-07 16:46:23
 */
enum Menu {
    listPage = '/park-system/parkManagement/menu/menuListPage', // 菜单列表分页查询
    menuList = '/park-system/parkManagement/menu/menuUrlList', // 当前用户菜单查询
    update = '/park-system/parkManagement/menu/updateMenu', // 编辑菜单
    remove = '/park-system/parkManagement/menu/removeMenu', // 删除菜单
    createMenu = '/park-system/parkManagement/menu/createMenu', // 创建菜单
    updateStatus='/park-system/parkManagement/menu/updateStatus', // 更该菜单装态
    rsMenuTree = '/park-system/parkManagement/menu/menuTree', // 当前用户的菜单树（用于权限分配）
    resetMenu = '/park-system/parkManagement/menu/resetMenu' // 重置菜单
  }

export default Menu;
