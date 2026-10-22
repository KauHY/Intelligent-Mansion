import { number } from 'mathjs';

/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-05 16:18:50
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-09 17:48:24
 */
interface SearchParams extends BaseSearchParams{ // 园区分页查询
  buildingId?: string, // 园区名称
  houseId?:string, // 组织id
  parkOrgId?:string
  notHouseProperty?:string
}

interface cardCreatedParams {
  employeeName:string,
cardMedium:number, // 卡片介质
cardNo:string, //
cardType:number,
companyId:string// 所属企业Id
effectEndDate:string// 生效结束日期
effectStartDate:string// 生效开始日期
employeeId:string// 关联员工Id
id:string
parkOrgId:string// 园区组织Id
remark:string// 备注
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

interface HouseInfo {
    buildingName:string // 楼栋名称
    parkOrgName?:string // 园区名称
    id:string,
    buildingId:string // 楼栋id
    name:string // 楼层房号
    code:string // 房屋编码
    floor:number|null // 所在楼层
    coverArea:number|null // 套内面积
    buildArea:number|null // 建筑面积
    parkOrgId:string
    rent:string // 租赁状态：false：未租 true：已租
    floorType?:number|null, // 楼层属性
    houseStatus?:number|null, // 房屋状态
    houseProperty?:number|null, // 房屋属性
    houseFunction?:number|null, // 房屋功能
    orientation?:number|null, // 朝向
}

interface EquipmentTreeParams { // 车道分页查询
  categoryType: string, // 设备名称
  parkOrgId: string, // 设备类型
}

interface EquipmentSearchParams extends BaseSearchParams { // 车道分页查询
    deviceName: string, // 设备名称
    deviceType: string, // 设备类型
    deviceId: string, // 设备id
    ipAddress: string, // id地址
    parkOrgId:string, // 园区id
    status: 1|1|null, // 状态
}

interface Extension { // 编辑设备时的扩展类型
        // ------- 门禁----------
        location: string, // 设备位
        companyId:string,
        // ------- 道闸 ----------
        parkingLotId: string, // 关联停车场
        carLane: string, // 车道
        gatewayType: number|null, // 出入口
}
interface TreeNodeItem {
  title: string;
  key: string;
  isDel: boolean;
  type: number | undefined;
  slots?: any;
  children?: TreeNodeItem[];
  [propsName: string]: any;
}
// interface EquipmentEditInfo extends Extension { // 用于编辑新增等前端操作
//   id: string,
//   deviceType: string, // 设备类型名
//   deviceId:string, // 设备id
//   status?: 0|1|null, // 状态名称
//   deviceName: string, // 设备名称
//   ipAddress: string, // ip地址
//   parkOrgId:string,
//   companyId:string,
// }
interface AddUserParams {
adminName: string,
adminId: string,
brand :string,
buildingId :string,
categoryId :string,
deviceCode :string,
floor :string,
id:string,
installationDate :string,
installationLocation :string
manufacturer :string
manufacturerMobile :string
manufacturerUnit :string
name :string
originalNumber :string
parkOrgId :string
pic :string
item : string
remark :string
scrapDate :string
specifications :string,
state :boolean,
}

interface stopParams {
  id: string,
}
interface FloorInfo {
  disabled:boolean
  expand:boolean
  hierarchy:number | undefined
  label:string
  leaf:boolean
  parentId:string
  parkOrgId:string
  selectable:boolean
  type:string
}

interface CustomerCreated {
id:string,
type?:number | null,
cardNo?:string
companyId?:string
employeeId?:string
}
// 供应服务新增参数
interface supplyInfoKind {
  parkOrgName:string,
  ip: number | null,
  buildingId :number | null,
  deviceCode :string,
  floor :number | null,
  id:string,
  installationDate :string,
  installationLocation :string
  name :string
  originalNumber :string
  parkOrgId :string
  state :boolean,
  systemType: number | null,
  deviceId: number | null,
  communicationPort: number | null,
  houseId: number | null,
  port: number | null,
  serialNumber: number | null,
  }
interface CustomerInfoKind {
ip: number | null,
buildingId :number | null,
deviceCode :string,
floor :number | null,
id:string,
installationDate :string,
installationLocation :string
name?:string,
deviceName?:string,
originalNumber :string
parkOrgId :string
state :boolean,
systemType: number | null,
deviceId: number | null,
communicationPort: number | null,
houseId: number | null,
port: number | null,
serialNumber: number | null,
}

interface EquipmentInfo {
    id: string,
    companyId?: string,
    deviceType: number|null, // 设备类型名
    deviceId:string, // 设备id
    status?: 0|1|null, // 状态名称
    deviceName: string, // 设备名称
    ipAddress: string, // ip地址
    parkOrgId:string,
    parkOrgName?: string, // 园区名称
    remark?: string, // 备注
    createTime?: string, // 创建时间
    extension: Extension
    buildingId? :string | null,
    location?: string,
    floor?:string,
    houseId?:string
    lockChanelList?:{
      channelName: string, // 通道名
      channelNo:string, // 通道号
      houseId: string, // 房屋id
    }[]
}

export {
  cardCreatedParams,
  EquipmentSearchParams,
  EquipmentInfo,
  Extension,
  SearchParams,
  HouseInfo,
  AddUserParams,
  stopParams,
  EquipmentTreeParams,
  TreeNodeItem,
  FloorInfo,
  AddParams,
  CustomerInfoKind,
  CustomerCreated,
  supplyInfoKind,
};
