/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-24 15:35:47
 * @LastEditTime: 2022-01-26 15:28:19
 * @LastEditors: Xucaizhi3
 * @Description:
 */
const ulrPrefix = 'park-meeting';

const MeetingManage = {

  // 会议室管理
  meetingListPage: `${ulrPrefix}/meeting-manage/listMeetingManagePage`, // 会议室管理-分页查询
  meetingCreate: `${ulrPrefix}/meeting-manage/create`, // 会议室管理-新增
  meetingRemove: `${ulrPrefix}/meeting-manage/remove`, // 会议室管理-删除
  meetingModify: `${ulrPrefix}/meeting-manage/modify`, // 会议室管理-修改
  meetingFindById: `${ulrPrefix}/meeting-manage/findById`, // 会议室管理-根据ID查询
  meetingChange: `${ulrPrefix}/meeting-manage/changeStatus`, // 会议室管理-启用禁用

  overviewMeeting: `${ulrPrefix}/meeting-manage/overviewMeeting`, // 会议室预订-预定概览
  getMeetingScreenshot: '/park-device/device-info/getMeetingScreenshot', // 会议室信息屏-查看会议室屏屏幕
  sleepDevice: '/park-device/device-info/sleepDevice/', // 休眠会议室屏
  wakeUpDevice: '/park-device/device-info/wakeUpDevice/', // 会议室信息屏-唤醒会议室屏

  // 会议室分布
  meetingMapListPage: `${ulrPrefix}/meeting-plane-figure/listMeetingPlaneFigurePage`,
  meetingMapCreate: `${ulrPrefix}/meeting-plane-figure/create`,
  meetingMapRemove: `${ulrPrefix}/meeting-plane-figure/remove`,
  meetingMapModify: `${ulrPrefix}/meeting-plane-figure/modify`,
  meetingMapFindById: `${ulrPrefix}/meeting-plane-figure/findById`,
};
export default MeetingManage;
