/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-02 11:22:39
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 15:49:14
 */
enum commandBigScreen {
    exceptionalTaskList= '/park-inspection/inspection/inspection-task/3DayExceptionalTaskDetail', // 巡检异常汇总
    todayTaskCount = '/park-inspection/inspection/inspection-task/todayTaskCount', // 当天的巡检情况
    listHouseFor3D = '/park-system/parkManagement/house/listHouseFor3D',
    getActivityList = '/park-customer/parkManagement/activity/getActivityList', // 园区活动
    orderResponse = '/park-customer/parkManagement/order/orderResponse', // 工单响应
    orderStatistic = '/park-customer/parkManagement/order/orderStatistic', // 工单统计
    getHouseRentData = '/park-system/parkManagement/house/getHouseRentData', // 智慧招商
    getBillForScreen = '/park-contract/rentContractBill/getBillForScreen', // 收费管理-累计收费、当年收费
    // getBillListForScreen = '/park-contract/rentContractBill/getBillListForScreen', // 收费管理-柱状图
    getCompanyOverview = '/park-system/parkManagement/company/getCompanyOverview', // 企业总览
    getApartmentDataForScreen = '/park-contract/rentContractBill/getApartmentDataForScreen', // 公寓管理
    getWeather = '/park-common/weather/get', // 天气
    parkingStatistics = '/park-visitor/parking-statistics/', // 停车场信息 （后台得奇葩传参形式）
    meetingStatistics = '/park-customer/parkManagement/meeting-statistics/', // 会议室信息 （后台得奇葩传参形式）

    getChanceOverview='/park-business/statement/businessStatistic/getChanceOverview', // 商机概览
    getCustomerIndustry='/park-business/statement/businessStatistic/getCustomerIndustry', // 客户行业分析
    getThreadSource='/park-business/statement/businessStatistic/getThreadSource', // 线索来源分析
    getTransitionRate='/park-business/statement/businessStatistic/getTransitionRate', // 商机转换率：开始阶段 转化至 结束阶段 的百分比(
}
export default commandBigScreen;
