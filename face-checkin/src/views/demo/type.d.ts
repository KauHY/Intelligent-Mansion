/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-17 18:35:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-23 14:45:35
 */
interface LabelData {
    companyName: string,
    area: number,
    houseFunction: string, // 功能
    orientation: string, // 朝向
    houseProperty: string, // 房屋属性
    rent: boolean,
    name:string, // 房间名称
    center: [number, number, number],
}

interface RoomDatas{
  [building:string]:{
    [floor:string]:{
      [room:string]:LabelData
    }
  }
}

export {
  LabelData,
  RoomDatas,
};
