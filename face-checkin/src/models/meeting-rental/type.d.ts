/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:23
 * @LastEditTime: 2022-04-06 15:37:44
 * @LastEditors: Xucaizhi3
 * @Description:
 */
interface SearchmeetingRentalParams extends BaseSearchParams {}

interface MeetingRentalListKind {
  meterType?:any,
  usingStatus?:any,
  initialReading:any,
  id: any
  buildingId: string
  houseId: string
  meetingName: string
  goodsName:string
  telephone: string // 咨询电话
  capacity: string // 容纳人数
  meetingEquipment: string // 1摄像机、2远程会议设备、3投影仪、4电子白板
  coverImage: string // 封面图片（单张）
  detailImage: string // 详情图片（最多五张）
  remark: string // 会议室介绍
  reserveInstruct: string // 预定须知
  charge: string // 是否收费 1是 0否
  rentPrice: string // 租赁价格
  status: number // 状态
}

interface TimeArr {
  startTime: string
  endTime: string
  timeType: 1|2|3 // 时间段类型 1上午 2下午 3晚上
}

interface EmComKind {
  companyId?: string
  companyName?: string
  relationType: number
  employeeId?: string
  employeeName?: string
}

interface MeetingRentalInfoKind {
  id?: string
  buildingId: string
  houseId: string
  parkOrgId: string
  meetingName: string
  telephone: string // 咨询电话
  capacity: string // 容纳人数
  meetingEquipment: string | string[], // 1摄像机、2远程会议设备、3投影仪、4电子白板
  coverImage: string // 封面图片（单张）
  detailImage: string // 详情图片（最多五张）
  remark: string // 会议室介绍
  reserveInstruct: string // 预定须知
  charge: number | null // 是否收费 1是 0否
  rentPrice: number | null // 租赁价格
  goodsId: string // 物资id 逗号分割
  usePermission: number | null // 0不限 1指定公司/人员
  companyIds: any[] // 关联公司id 逗号分割
  employeeIds: any[] // 关联员工id 逗号分割
  material: string
  timeArr: any[]
  timeReqDTOList: any[]
  companyList?: EmComKind[]
  employeeList?: EmComKind[]
  timeList?: TimeArr[]
  goodsList?: any[]
  meetingRentDTO?: any
}

interface EnableKind{
  id: string,
  status: number
}
export {
  SearchmeetingRentalParams, MeetingRentalListKind, MeetingRentalInfoKind, EnableKind, EmComKind, TimeArr,
};
