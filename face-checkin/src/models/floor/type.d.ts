/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:11:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 15:30:52
 */
interface SearchParams extends BaseSearchParams{ // 园区分页查询
  buildingId?: string, // 园区名称
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

export {
  SearchParams,
  HouseInfo,
};
