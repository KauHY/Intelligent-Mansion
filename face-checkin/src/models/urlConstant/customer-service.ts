/*
 * @Description:客户服务
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 14:12:17
 * @LastEditors: your name
 * @LastEditTime: 2022-03-15 21:58:50
 */

enum CustomerService {
  listOrderPage = '/park-customer/parkManagement/order/listOrderPage', // 报事投诉|保修分页查询
  orderFindById = '/park-customer/parkManagement/order/findById/', // 报事投诉|保修根据id查询详情
  orderAdd = '/park-customer/parkManagement/order/create', // 报事投诉|保修新增
  orderCancel = '/park-customer/parkManagement/order/cancel', // 报事投诉|保修撤销订单
  orderAssignUser = '/park-customer/parkManagement/order/assignUser', // 报事投诉|保修分派订单
  listAnnouncementPage = '/park-customer/parkManagement/information/listNoticePage', // 公告分页查询
  informationDel = '/park-customer/parkManagement/information/remove/', // 公告咨询删除

  announcementListPage = '/park-customer/parkManagement/notice/listNoticePage', // 公告分页查询
  announcementAdd = '/park-customer/parkManagement/notice/create', // 公告新增
  announcementDel = '/park-customer/parkManagement/notice/remove/',
  announcementEdit = '/park-customer/parkManagement/notice/update', // 公告编辑
  announcementFindById = '/park-customer/parkManagement/notice/findById/', // 公告根据id查询详情

  noticeEdit = '/park-customer/parkManagement/information/update', // 咨询编辑
  noticeAdd = '/park-customer/parkManagement/information/create', // 咨询新增
  noticeDel = '/park-customer/parkManagement/information/remove/', // 删除资讯
  listInfoPage = '/park-customer/parkManagement/information/listInfoPage', // 资讯分页查询
  informationFindById = '/park-customer/parkManagement/information/findById/', // 公告资讯根据id查询

  // -------物品放行----------
  goodsReleaseListPage = '/park-customer/parkManagement/goods-release/listGoodsReleasePage', // 物品放行分页查询
  goodsReleaseAudit = '/park-customer/parkManagement/goods-release/check', // 物品放行审核
  goodsReleaseFindById = '/park-customer/parkManagement/goods-release/findById/',
  exportGoodsRelease='/park-customer/parkManagement/goods-release/exportGoodsRelease', // 导出
}

// type a = keyof typeof CustomerService

export default CustomerService;
