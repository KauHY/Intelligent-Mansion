/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:50
 * @LastEditTime: 2022-03-04 15:47:06
 * @LastEditors: Xucaizhi3
 * @Description:
 */
enum CustomerUrls {
  customerListPage = '/park-business/business/customer-info/listCustomerPage', // 广告分页查询
  customerAdd = '/park-business/business/customer-info/create', // 广告-新增
  customerModify = '/park-business/business/customer-info/modify', // 广告修改
  customerDel = '/park-business/business/customer-info/remove', // 广告删除
  customerFindById = '/park-business/business/customer-info/getCustomerInfoById', // 根据id查询广告详情
  customerExport = '/park-business/business/customer-info/export', // 根据id查询广告详情
  customerTagListPage = '/park-business/business/customer-tag/listCustomerTagPage', // 客户标签分页查询
  customerTagAdd = '/park-business/business/customer-tag/create', // 客户标签-新增
  customerTagModify = '/park-business/business/customer-tag/modify', // 客户标签修改
  customerTagDel = '/park-business/business/customer-tag/remove', // 客户标签删除
  listCustomerTagDict = '/park-business/business/customer-tag/listCustomerTagDict', // 客户标签下拉
  listCustomerImport = '/park-business/business/customer-info/import', // 客户导入
}
export default CustomerUrls;
