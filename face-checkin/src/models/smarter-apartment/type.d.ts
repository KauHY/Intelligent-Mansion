/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-13 16:38:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-02 17:03:53
 */
interface RoleListItem {
  companyId:string// 所属企业Id
  companyName:string
  createDate:string// 创建日期
  employeeId:string// 关联员工Id
  employeeName:string// 关联员工Id
  houseList: []// 关联房屋集合
  id:string
  idCardNumber:string// 身份证号
  mobile:string// 手机号
  parkOrgId:string// 园区组织Id
  parkOrgName:string
  pics:string// 照片
  status: number | null, // 状态，0：禁用 1：启用
  cardMedium?: number | null
  cardType?: number | null
}

// 查询入参
interface SearchParams extends BaseSearchParams {
  companyId:string // 所属企业Id
  endTime:string
  mobile:string// 手机号
  name:string// 姓名
  parkOrgId:string// 园区组织Id
  startTime:string
  status: number | null, //
}

interface SearchHouseParams extends BaseSearchParams {
    buildingId: string,
    endTime: string,
    houseId: string,
    parkOrgId: string,
    startTime: ''
}

// 新增表计-入参
interface AddParams { // 新增接口入参
  companyId:string// 所属企业Id
  deviceIds:[]// 公寓门禁设备id集合
  effectEndDate:string// 生效结束日期
  effectStartDate:string// 生效开始日期
  employeeId:any// 关联员工Id
  houseIds:[]// 关联房屋id集合
  id:string
  idCardNumber:string// 身份证号
  parkOrgId:string// 园区组织Id
  remark:string// 备注
  houseNames?:[]
}

interface updataStatusParams {
  id: string,
  status: number,
}

interface HouseInfo {
    buildingId: string,
    buildingName: string,
    contractId: string,
    contractNo: string,
    floor: number | null,
    houseFunction: number | null,
    houseId: string,
    houseName: string,
    houseStatus: number | null,
    parkOrgId: string,
    parkOrgName: string,
    state: number | null,
    type: number | null,
}

interface Fingerprint {
  employeeId:string,
  fingerId?: string,
  fingers:{
    finger:string,
    fingerIndex:number
  }[]
}

export {
  RoleListItem,
  SearchParams,
  AddParams,
  updataStatusParams,
  HouseInfo,
  SearchHouseParams,
  Fingerprint,
};
