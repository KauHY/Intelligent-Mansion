/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-24 15:35:27
 * @LastEditTime: 2022-03-15 10:51:53
 * @LastEditors: xucaizhi3
 * @Description:
 */
interface SearchContractParams extends BaseSearchParams {
  endTime: string,
  orgId: string,
  startTime: string,
}

interface MeetingList {
  deviceId?:string,
  screenStatus: number|null,
  screenDeviceId: string,
  buildingName: string,
  needReview: number|null,
  status: number|null,
  houseName: string,
  meetingName: string,
  openTimeEnd: string,
  openTimeStart: string,
  capacity: number|null,
  id: string,
  orgId: string,
  parkOrgId: string,
}

interface Meeting {
  screenDeviceId: string,
  buildingId: string,
  capacity: number|null, // 容纳人数
  needReview: number|null, // 预订是否需审核 1是 0否
  floorId: string,
  houseId: string,
  id?: string,
  meetingEquipment: any,
  meetingName: string,
  meetingType: number|null, // 会议室类型 1：普通会议 2：视频会议
  openTimeEnd: string,
  openTimeStart: string,
  parkOrgId: string,
  permissionList: string[], // 权限管理列表
  reviewerList: string[], // 会议预订审核人列表
  reviewerListName: string,
  relationType: number|null, // 权限类型 1公司 2员工
  remark: string
  companyIds: any // 关联公司id 逗号分割
  employeeIds: any // 关联员工id 逗号分割
}

interface MeetingMap {
  parkOrgId: string,
  buildingId: string,
  floorId: string,
  meetingPlaneFigure: string,
  id?: string,
}

interface MeetingMapList {
  buildingName: string,
  buildingId: string,
  floorId: string,
  meetingPlaneFigure: string,
  id: string,
  orgId: string,
  parkOrgId: string,
}

export {
  SearchContractParams,
  MeetingList,
  Meeting,
  MeetingMap,
  MeetingMapList,
};
