/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 18:57:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-15 15:10:35
 */
enum Organization {
    organizationByParentId = '/park-system/parkManagement/organization/childOrg', // 根据用户查询组织数
    organizationTree = '/park-system/parkManagement/organization/tree',
    listPage='/park-system/parkManagement/organization/listPage', // 分页查询
    add ='/park-system/parkManagement/organization/create', // 新增组织
    del = '/park-system/parkManagement/organization/remove/', // 删除组织
    update = '/park-system/parkManagement/organization/update', // 更新组织
  }

export default Organization;
