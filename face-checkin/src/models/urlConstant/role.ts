/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:40:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-15 17:44:47
 */
enum Role {
    listRoles = '/park-system/parkManagement/role/listRoles', // 根据组织查询角色
    listPage = '/park-system/parkManagement/role/listPage', // 角色分页查询
    remove = '/park-system/parkManagement/role/remove/', // 删除角色
    add = '/park-system/parkManagement/role/save', // 创建角色
    update = '/park-system/parkManagement/role/update', // 更新角色
    updateStatus = '/park-system/parkManagement/role/updateStatus', // 更新状态
    setDataPermissionList = '/park-system/parkManagement/permission/enable/data', // 设置数据权限列表
    setMenuPermissionList = '/park-system/parkManagement/permission/enable/menu', // 设置菜单权限列表
    echoMenu = '/park-system/parkManagement/permission/echoMenu', // 获取菜单权限列表
    echoData = '/park-system/parkManagement/permission/echoData', // 获取数据权限列表
  }

export default Role;
