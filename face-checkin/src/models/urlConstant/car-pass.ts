/*
 * @Description: 访客管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 11:05:21
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 18:34:17
 */

enum CarPass {
    // 车道配置
    laneDetails = '/park-visitor/carLane/', // 根据id查询车道
    laneAdd = '/park-visitor/carLane', // 车道新增
    laneEdit = '/park-visitor/carLane', // 车道编辑
    laneDel = '/park-visitor/carLane/', // 车道删除
    laneListPage = '/park-visitor/carLane/page', // 车道分页
    laneListDic = '/park-visitor/carLane/selectCarLane', // 车道下拉列表

    // 停车场管理
    parkingLotDetails = '/park-visitor/parkingLot/', // 根据id查询停车场
    parkingLotAdd = '/park-visitor/parkingLot', // 停车场新增
    parkingLotEdit = '/park-visitor/parkingLot', // 停车场编辑
    parkingLotDel = '/park-visitor/parkingLot/', // 停车场删除
    parkingLotListPage = '/park-visitor/parkingLot/page', // 停车场分页
    parkingLotDic = '/park-visitor/parkingLot/getAllByParkOrgId/', // 根据园区id查询停车场下拉数据
    syncParkingLot='/park-visitor/parkingLot/sync', // 同步车场车道信息
     // 车辆管理
     carDetails = '/park-visitor/car/findById/', // 根据id查询停车场
     carAdd = '/park-visitor/car/create', // 停车场新增
     carEdit = '/park-visitor/car/modify', // 停车场编辑
     carDel = '/park-visitor/car/remove/', // 停车场删除
     carListPage = '/park-visitor/car/listPage', // 停车场分页
     carRecordListPage = '/park-visitor/carAccessRecord/pageListFullRecord', // 车辆通行记录
     carSnapRecordListPage = '/park-visitor/carAccessRecord/pageListRecord', // 车辆抓拍记录

     importCar='/park-visitor/car/import', // 车辆导入

     // 导入导出
     carRecordExport = '/park-visitor/carAccessRecord/export/fullRecord', // 车辆通行记录导出
     carSnapRecordExport = '/park-visitor/carAccessRecord/export/record', // 车辆抓拍记录导出

}
export default CarPass;
