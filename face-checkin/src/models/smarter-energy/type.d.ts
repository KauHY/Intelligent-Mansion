import { number } from 'mathjs';

/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:48
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-06 12:25:50
 */
interface RoleListItem {
  status:number | null,
  buildingId:string// 楼栋
  deviceType:6, // 能耗类别（6电能耗，5水能耗）
  communicationPort:string// 通信端口
  deviceId:string // 设备id
  effectProperty:number, // 表计属性
  effectType:number// 表计类别
  floors:number// 所属楼层多选
  houseIds:string // 所属房屋多选
  id:string,
  initialReading:number// 初始读数
  initialTime:string, // 初始时间
  installationLocation:string// 安装位置
  ip:string, // ip地址
  maxRange:number, // 最大量程
  meterType:number, // 表类型
  name:string// 名称
  parkOrgId:string// 园区id
  port:number// 端口号
  serialNumber:string// 表计号码
}

// 查询入参
interface SearchParams extends BaseSearchParams {
  status:number// 所属楼层多选
  buildingId:string// 楼栋
  deviceType:number, // 能耗类别（5电能耗，6水能耗）
  communicationPort:string// 通信端口
  deviceId:string // 设备id
  effectProperty:number, // 表计属性
  effectType:number// 表计类别
  floors:number// 所属楼层多选
  houseIds:string// 所属房屋多选
  id:string,
  initialReading:number// 初始读数
  initialTime:string, // 初始时间
  installationLocation:string// 安装位置
  ip:string, // ip地址
  maxRange:number, // 最大量程
  meterType:number// 表类型
  name:string// 名称
  parkOrgId:string// 园区id
  port:number// 端口号
  serialNumber:string// 表计号码
}

// 新增表计-入参
interface AddParams { // 新增接口入参
  parkOrgId: string, //
  deviceType: number | null, //
  effectProperty: number | null, //
  effectType: number | null, //
  meterType: number | null, //
  deviceName: string, //
  id:string,
  deviceId: string, //
  maxRange: number | null, //
  location: string, //
  buildingId: string, //
  floors: any, //
  meterNo?: string, //
  houseIds: any, //
  initialReading: number| null, //
  parkOrgName?: string,
  initialTime: string, //
  remark: string, //
}

interface setAddParams {
  data:boolean
}

interface updataStatusParams {
  id: string,
  usingStatus?: number,
  deviceType?:number,
  shareAmount?:any
}

interface modifyMeterReadingParams {
  correctedReading: number | undefined;
  id: string | undefined;
  remark: string;
}

// 告警入参
interface warningRecordPageParams {
  parkOrgId: string, //
  warningCode: number, //
  warningTimeEnd: string, //
  warningTimeStart: string, //
  name: string, //
  warningLevel: number, //
}

interface lConsumptionAnalysisParams {
buildingId:string, // 楼栋
deviceType:number, // 能耗类别（1电能耗，2水能耗）
meterId:string, // 表计名称
month:number, // 月份
parkOrgId:string, // 园区id
year:number
}

// 用量入参
interface RecordPageParams {
  parkOrgId: string, //
  warningCode: number, //
  warningTimeEnd: string, //
  warningTimeStart: string, //
  name: string, //
  warningLevel: number, //
}

// 用量入参
interface changeParams {
  oldReading: number | undefined,
  deviceType: number | undefined,
  newMaxRange: number | undefined,
  newReading: number| undefined,
  oldMeterId: number| undefined,
  newDeviceId?:string,
}

interface AnalysisItem {
  consumption:number,
  time:string,
}

export {
  changeParams,
  warningRecordPageParams,
  RoleListItem,
  SearchParams,
  AddParams,
  setAddParams,
  updataStatusParams,
  RecordPageParams,
  modifyMeterReadingParams,
  lConsumptionAnalysisParams,
  AnalysisItem,
};
