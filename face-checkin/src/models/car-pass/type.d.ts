/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:44:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 13:47:13
 */

interface LaneSearchParams extends BaseSearchParams{ // 车道分页查询
  laneName:string, // 车道名称
  parkOrgId:string
  parkingLotId:string, // 停车场id
}

interface LaneInfo {
  id:string,
  laneName:string, // 车道名称
  parkOrgId:string, // 园区id
  parkingLotId:string, // 停车场id
  updateTime?:string,
  parkingLotName?:string, // 停车场名称
  parkOrgName?:string,
  carDirectStr:string,
}

interface parkingLotSearchParams extends BaseSearchParams{ // 停车场分页查询
  parkingLotName:string, // 停车场名称
  parkOrgId:string
}

interface ParkingLotInfo {
  id:string,
  numberOfSpace?:number|null,
  totalLot?:number|null,
  parkingLotName?:string, // 停车场名称
  parkOrgId:string, // 园区id
  updateTime?:string,
  parkingLot?:string,
  parkOrgName?:string,
  parkingLotCode?:string,
  allowVisitor?:number|null,
}

interface CarSearchParams extends BaseSearchParams{ // 停车场分页查询
  authEndTime:string, // 授权结束时间
  authStartTime:string, // 授权开始时间
  carOwnerName:string, // 车主姓名
  tel:string, // 车主电话
  carPlate:string, // 车牌号
  carType:string, // 车辆类型
  companyId:string, // 企业id
  parkOrgId:string,
  status:0|1|2|'', // 状态：0-待生效；1-生效中；2-已失效
}

interface CarInfo {
  id:string,
  authEndTime:string, // 授权结束时间
  authStartTime:string, // 授权开始时间
  carNum:string, // 车牌号
  parkType:number|null, // 车辆类型
  companyId:string, // 企业id
  parkOrgId:string,
  status?:0|1|2|'', // 状态：0-待生效；1-生效中；2-已失效
  parkOrgName?:string,
  updateTime?:string,
  parkingLotIds:string,
  employeeId:string,
  carOwnerName:string,
}

interface CarPassRecordSearchParams extends BaseSearchParams{ // 停车场分页查询
  carPlate?: string, // 车牌号
  carType?:number|null, // 车辆类型
  carRegistered?:0|1|null, // 是否在册
  parkOrgId?:string,
  carLaneId?:string, // 车道id
  enterRecognizeTimeEndTime?: string, // 驶入识别结束时间
  enterRecognizeTimeStartTime?:string, // 驶入识别开始时间
  exitRecognizeTimeEndTime?:string, // 驶出识别结束时间
  exitRecognizeTimeStartTime?:string, // exitRecognizeTimeStartTime
  exitRecognizeTime?: string, // 驶离时间
}

interface CarPassRecord { // 车辆通行记录
  carPlate: string, // 车牌号
  carRegisteredLabel: string, // 是否在册,0：不在册，1：在册
  carTypeName: string, // 车辆类型名
  enterCarLaneName: string, // 驶入车道名
  enterImgUrl: string, // 抓拍图片
  enterRecognizeTime: string, // 驶入时间
  exitCarLaneName: string, // 驶出车道名
  exitImgUrl: string, // 驶出抓拍照片
  exitRecognizeTime: string, // 驶离时间
  id: string,
  parkOrgName: string, // 园区名
}

export {
  LaneSearchParams,
  LaneInfo,
  parkingLotSearchParams,
  ParkingLotInfo,
  CarSearchParams,
  CarInfo,
  CarPassRecordSearchParams,
  CarPassRecord,
};
