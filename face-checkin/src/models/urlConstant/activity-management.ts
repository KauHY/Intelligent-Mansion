/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-11 11:13:25
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 10:56:19
 */
enum ActivityManagement {
    activityListPage = '/park-customer/parkManagement/activity/listActivityPage', // 活动信息分页
    activityAdd = '/park-customer/parkManagement/activity/create', // 活动信息新增
    activityDel = '/park-customer/parkManagement/activity/removeActivity/', // 活动信息分页
    activityModify = '/park-customer/parkManagement/activity/modify', // 活动信息分页

    activityFindById = '/park-customer/parkManagement/activity/findById/', // 活动信息详情
    toppingActivity = '/park-customer/parkManagement/activity/toppingActivity', // 置顶/取消置顶
    listActivityRegisterRecordPage = '/park-customer/parkManagement/activity-register-record/listActivityRegisterRecordPage', // 报名记录分页查询
    exportActivityRegisterRecord = '/park-customer/parkManagement/activity-register-record/export', // 报名记录分页查询
}
export default ActivityManagement;
