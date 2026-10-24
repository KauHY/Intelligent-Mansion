/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 18:26:10
 */
interface SearchParams extends BaseSearchParams{ // 园区分页查询
    name: string, // 园区名称
    orgId:string, // 组织id
}

interface ParkListItem { // 园区列表分页查询
  address:string,
  city:string,
  id:string,
  parkOrgId:string
  latitude:string,
  longitude:string,
  name:string,
  province:string
}

interface ParkInfo { // 完善园区信息
  address: string, // 地址
  buildTime: string, // 建成时间
  buildingArea: number, // 建筑面积
  companyContacts: string, // 权属单位联系人
  companyName: string, // 权属单位
  companyTel: string, // 权属单位联系电话
  coverArea: number, // 占地面积
  description: string, // 描述
  fireExamineStatus: 0|1|1, // fireExamineStatus
  firePerson: string, // 消防责任管理人
  firePersonTel: string, // 消防责任管理人联系电话
  parkOrgId: string, // 园区ID
  latitude: number, // 纬度
  longitude: number, // 经度
  managementContacts: string, // 管理机构联系人
  managementName: string, // 管理机构
  managementTel: string, // 管理机构联系电话
  name: string, // 园区名
  parentOrgName?: string // 上级组织名
  parkFunction: number|null, // 功能用途
  parkType: 1|2|3|4|5, // 园区属性
  pics: string, // 图片
  property: 1|2|3|4, // 产权属性
  province:string,
  city:string
}

export {
  SearchParams,
  ParkListItem,
  ParkInfo,
};
