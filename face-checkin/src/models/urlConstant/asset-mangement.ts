/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 13:46:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 15:38:41
 */
enum asssetManager {
  /**
   * 物品管理
   */
  goodsListPage = '/park-customer/parkManagement/pro-goods-manager/listProGoodsManagerPage', // 分页查询
  goodsEdit = '/park-customer/parkManagement/pro-goods-manager/modify', // 巡检点编辑
  patrolPlaceAdd = '/park-customer/parkManagement/pro-goods-manager/create', // 入库新增
  patrolPlaceDel='/park-customer/parkManagement/pro-goods-manager/remove/', // 巡检点删除
  settingMinStock='/park-customer/parkManagement/pro-goods-manager/settingMinStock', // 巡检点删除
  goodsListPageExport = '/park-customer/parkManagement/pro-goods-manager/export', // 导出
  goodsListPageImport = '/park-customer/parkManagement/pro-goods-manager/import', // 导入

  /**
   * 库存管理
   */
  inventoryListPage = '/park-customer/parkManagement/pro-stock-manager/listProStockManagerPage', // 分页查询
  inventoryStockAdd = '/park-customer/parkManagement/pro-stock-manager/stockIn', // 入库
  inventoryOutInStockEdit = '/park-customer/parkManagement/pro-stock-manager/modify', // 出入库编辑
  inventoryListPageImport = '/park-customer/parkManagement/pro-stock-manager/import', // 导入
  /**
   * 领用管理
   */
  receiveListPage = '/park-customer/parkManagement/pro-receive-manager/listProReceiveManagerPage', // 分页查询
  auditReceive = '/park-customer/parkManagement/pro-receive-manager/auditReceive', // 审核出库
  auditReturn = '/park-customer/parkManagement/pro-receive-manager/auditReturn', // 审核入库
  getReceiveDetail = 'park-customer/parkManagement/pro-receive-manager/findById/', // 获取详情

  /**
   * 领用权限设置
   */
  receiveAuditListPage = '/park-customer/parkManagement/pro-receive-permission/listProReceivePermissionPage', // 分页查询
  receiveAuditSetting = '/park-customer/parkManagement/pro-receive-permission/create', // 领用权限设置

  /**
   * 出入库记录
   */
  outInstockRecordListPage = '/park-customer/parkManagement/pro-stock-record/listProStockRecordPage', // 分页查询
  outInstockRecordListPageExport = '/park-customer/parkManagement/pro-stock-record/export', // 导出

  /**
   * 盘点管理
   */
  inventoryManagerListPage = '/park-customer/parkManagement/pro-inventory-manager/listProInventoryManagerPage', // 分页
  inventoryManagerDetails = '/park-customer/parkManagement/pro-stock-manager/findByGoodsId/', // 详情
  inventoryManagerListPageExport = '/park-customer/parkManagement/pro-inventory-manager/export', // 导出

  /**
   * 文件下载
   */
  downloadFile = '/park-common/file/download?key='
}

export default asssetManager;
