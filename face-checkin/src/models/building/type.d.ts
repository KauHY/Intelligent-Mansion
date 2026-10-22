/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:11:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-25 17:48:28
 */
interface SearchParams extends BaseSearchParams{ // 园区分页查询
    parkOrgId: string, // 园区id
    buildingId:string, // 楼栋id
    houseId:string, // 房屋id
}

interface FloorListItem {
    label:string
    value:string
    type:string
}

interface FloorInfo {
    remark:string
    property:number,
    name:string
    id:string,
    code:string
    totalFloor:string
    buildTime:string
    elevator:number
    coverArea:string
    buildArea:string
    parkOrgId:string
    parkOrgName?:string
    buildingId?:string
}

export {
  SearchParams,
  FloorListItem,
  FloorInfo,
};
