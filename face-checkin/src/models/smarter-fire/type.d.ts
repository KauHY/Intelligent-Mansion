import { number } from 'mathjs';

/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:48
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-15 14:55:12
 */
interface RoleListItem {
  buildingId:string// 楼栋
  deviceType:6, // 能耗类别（6电能耗，5水能耗）
  communicationPort:string// 通信端口
  deviceId:string // 设备id
  effectProperty:number, // 表计属性
  effectType:number// 表计类别
  floors:number// 所属楼层多选
  houseIds:string // 所属房屋多选
  id:string,
  status:number// 所属楼层多选
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
interface SearchParams extends BaseSearchParams{
  buildingId:string// 楼栋
  deviceType:number, // 能耗类别（6电能耗，5水能耗）
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
interface CustomerInfoKind {
  buildingName: string,
  warnReasonCode: string,
  warnTime: string,
  warnType: null,
  parkOrgName:string,
  deviceNo:string,
  opinion:string,
  realReasonType: number,
  ip: number | null,
  adminId: string,
  brand :string,
  buildingId :number | null,
  categoryId :string,
  deviceCode :string,
  floor :number,
  id:string,
  installationDate :string,
  installationLocation :string
  manufacturer :string
  manufacturerMobile :string
  manufacturerUnit :string
  deviceName :string
  originalNumber :string
  parkOrgId :string
  pic :string
  item : string
  remark :string
  scrapDate :string
  specifications :string,
  state :boolean,
  systemType: number | null,
  deviceId: number | null,
  communicationPort: number | null,
  houseId: number | null,
  port: number | null,
  serialNumber: number | null,
  }
// 新增表计-入参
interface AddParams { // 新增接口入参
buildingId:string, // 安装楼栋
communicationPort:string, // 通信端口
createBy:string,
createTime:string,
deleted:boolean,
deviceId:string, // 设备id
deviceNo:string, // 设备编号
fireControlDeviceId:string, // 消防设备id
floor:number, // 所属楼层
houseId:string, // 房屋
id:string, // 记录id
installationLocation:string, // 安装位置
ip:string, // ip地址
name:string, // 设备名称
opinion:string, // 处理意见
parkOrgId:string, // 园区id
parkOrgName:string,
port:number, // 端口号
realReasonType:number, // 确认结果，1,真实火警，2误报，3消防演习
serialNumber:string, // SN号码
status:number, // 记录的状态,0待处理，1已处理
systemType:number, // 所属系统
updateBy:string,
updateTime:string,
warnReasonCode:number, // 报警原因
warnTime:string, // 报警时间
warnType:number,
}

interface setAddParams {
  data:boolean
}

interface updataStatusParams {
  id: string,
  status: number,
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
  deviceType: number | undefined,
  newMaxRange: number | undefined,
  newSerialNumber: number| undefined,
  newReading: number| undefined,
  oldMeterId: number| undefined,
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
  CustomerInfoKind,
};
