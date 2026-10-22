/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-13 14:05:20
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-15 16:58:36
 */
enum Tenant {
    tenantsOpts = '/park-system/tenant/tenants', // 模糊查询租户下拉列表
    add = '/park-system/tenant/createTenant', // 新增租户
    listPage = '/park-system/tenant/listPage', // 分页查询
    update = '/park-system/tenant/modify', // 更新
    del = '/park-system/tenant/remove/', // 模糊查询租户下拉列表
    updateStatus='/park-system/tenant/updateStatus',
    rsTenantList = '/park-system/tenant/list', // 当前用户的所有租户
}
export default Tenant;
