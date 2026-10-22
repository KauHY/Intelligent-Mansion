/*
 * @Author: your name
 * @Date: 2022-04-07 10:19:47
 * @LastEditTime: 2022-04-08 14:32:38
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

// 车辆通行设备管理表格
interface VehicleTable{
    id: string,
    deviceType: number, // 设备类型
    deviceName: string,	// 设备名称
    deviceId: string,		// 设备ID
    ipAddress: string,		// ip地址
    parkOrgName: string,		// 关联园区
    location: string,		// 关联园区
    status: number, // 状态
    extension?: Extension
}
interface Extension { // 编辑设备时的扩展类型
    // ------- 门禁----------
    // ------- 道闸 ----------
    parkingLotId: string, // 关联停车场
    carLaneId: string, // 车道
    gatewayType: number|null, // 出入口
  }

interface VehicleInfo{
    id: string,
    deviceType: number | null, // 设备类型
    deviceName: string,	// 设备名称
    deviceId: string,		// 设备ID
    ipAddress: string,		// ip地址
    parkOrgId: string
    gatewayType?: 0|number,
    location: string,		// 位置
    remark: string,		// 备注
    extension: Extension
}
interface EquipmentSearchParams extends BaseSearchParams { // 车道分页查询
    deviceName: string, // 设备名称
    deviceType: string, // 设备类型
    deviceId: string, // 设备id
    ipAddress: string, // id地址
    parkOrgId:string, // 园区id
    status: 1|1|null, // 状态
}
export {
  VehicleTable,
  VehicleInfo,
  EquipmentSearchParams,
  Extension,
};
