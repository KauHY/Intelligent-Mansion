/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:50
 * @LastEditTime: 2022-03-14 17:51:47
 * @LastEditors: xucaizhi3
 * @Description:
 */
enum MeetingRentalUrls {
  meetingRentalListPage = '/park-customer/parkManagement/meeting-rent/listMeetingRentPage', // 会议室分页查询
  meetingRentalAdd = '/park-customer/parkManagement/meeting-rent/create', // 会议室-新增
  meetingRentalModify = '/park-customer/parkManagement/meeting-rent/modify', // 会议室修改
  meetingRentalDel = '/park-customer/parkManagement/meeting-rent/remove', // 会议室删除
  meetingEnable = '/park-customer/parkManagement/meeting-rent/updateStatus', // 会议室-启用禁用
  meetingRentalFindById = '/park-customer/parkManagement/meeting-rent/findById', // 根据id查询会议室详情

  materialListPage = '/park-customer/parkManagement/meeting-goods/listMeetingGoodsPage', // 物资分页查询
  materialAdd = '/park-customer/parkManagement/meeting-goods/create', // 物资-新增
  materialModify = '/park-customer/parkManagement/meeting-goods/modify', // 物资修改
  materialDel = '/park-customer/parkManagement/meeting-goods/remove', // 物资删除
  materialFindById = '/park-customer/parkManagement/meeting-goods/findById', // 会议室物资管理-根据ID查询

  rentalVerifyListPage = '/park-customer/parkManagement/meeting-reserve/listMeetingReservePage', // 审核列表
  rentalVerifyFindById = '/park-customer/parkManagement/meeting-reserve/findById', // 审核详情
  rentalVerify = '/park-customer/parkManagement/meeting-reserve/review', // 审核
  orderExport = '/park-customer/parkManagement/meeting-reserve/export', // 导出

}
export default MeetingRentalUrls;
