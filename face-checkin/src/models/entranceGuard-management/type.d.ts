/*
 * @Author: your name
 * @Date: 2022-04-07 10:19:47
 * @LastEditTime: 2022-08-09 17:32:13
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\entranceGuard-management\type.d.ts
 */

// 门禁设备管理表格
interface EntranceGuardTable{
    id: string,
    deviceType: number, // 设备类型
    deviceName: string,	// 设备名称
    deviceId: string,		// 设备ID
    ipAddress: string,		// ip地址
    parkOrgName: string,		// 关联园区
    location: string,		// 关联园区
    status: number, // 状态
}

interface EntranceGuardInfo{
    id: string,
    deviceType: number | null, // 设备类型
    deviceName: string,	// 设备名称
    deviceId: string,		// 设备ID
    ipAddress: string,		// ip地址
    parkOrgId: string,		// 关联园区
    location: string,		// 关联园区
    remark: string,		// 备注
    buildingId?:string,
    floor?:string,
    houseId?:string
    lockChanelList?:{
      channelName: string, // 通道名
      channelNo:string, // 通道号
      houseId: string, // 房屋id
    }[]
}

export {
  EntranceGuardTable,
  EntranceGuardInfo,
};
