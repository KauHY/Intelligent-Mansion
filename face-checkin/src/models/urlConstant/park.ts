/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:11:42
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-23 11:04:27
 */
enum Park {
  getTenantParkOrgDict='/park-system/parkManagement/organization/getTenantParkOrgDict', // 获取租户下所有的园区
  listParkName = '/park-system/parkManagement/park/listParkDict', // 查看可见的园区名
  listPageByOrgId = '/park-system/parkManagement/park/listPageByOrgId', // 园区分页查询
  getParkInfoById = '/park-system/parkManagement/park/getParkInfoByParkOrgId/', // 根据id查询园区详情
  save='/park-system/parkManagement/park/completeParkInfo', // 完善园区信息

  listCompanyDictByType='/park-system/parkManagement/company/listCompanyDictByType' // 企业下拉框
}

export default Park;
