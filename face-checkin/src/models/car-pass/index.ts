/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-06 15:18:01
 */
import URLS from '~models/urlConstant/car-pass';
import request from '~utils/axios';
import {
  LaneSearchParams,
  LaneInfo,
  parkingLotSearchParams,
  ParkingLotInfo,
  CarSearchParams,
  CarInfo,
  CarPassRecordSearchParams,
  CarPassRecord,
} from './type';

export default class CarPass {
  /**
   * @description: 车道分页查询
   * @param {VisitorSubscribeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async laneListPage<T>(searchData:LaneSearchParams) {
    const { data } = await request({
      url: URLS.laneListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<LaneInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 根据id查询车道信息
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async laneDetails(id:string) {
    const { data } = await request({
      url: `${URLS.laneDetails}${id}`,
      method: 'get',
    });
    return data as LaneInfo;
  }

  /**
   * @description: 车道下拉数据
   * @param {string} id 停车场id
   * @return {*}
   * @author: GUOCHAO82
   */
  async laneListDic(id:string) {
    const { data } = await request({
      url: `${URLS.laneListDic}`,
      data: {
        parkingLotId: id,
      },
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
   * @description: 删除车道
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async laneDel(id:string) {
    await request({
      url: `${URLS.laneDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
     * @description: 新增车道
     * @param {VisitorProcess} data
     * @return {*}
     * @author: GUOCHAO82
     */
  async laneAdd(data:LaneInfo) {
    await request({
      url: URLS.laneAdd,
      data,
    });
  }

  /**
     * @description: 编辑车道
     * @param {LaneInfo} data
     * @return {*}
     * @author: GUOCHAO82
     */
  async laneEdit(data:LaneInfo) {
    await request({
      url: URLS.laneEdit,
      data,
      method: 'PUT',
    });
  }

  // -----------------------------------停车场管理-------------------------------
  /**
   * @description: 停车场分页查询
   * @param {parkingLotSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async parkingLotListPage<T>(searchData:parkingLotSearchParams) {
    const { data } = await request({
      url: URLS.parkingLotListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<ParkingLotInfo>>list,
      total: <number>total,
    };
  }

  /**
     * @description: 同步车场车道信息
     * @param {LaneInfo} data
     * @return {*}
     * @author: GUOCHAO82
     */
  async syncParkingLot() {
    await request({
      url: URLS.syncParkingLot,
      method: 'get',
    });
  }

  /**
     * @description: 根据id查询停车场信息
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async parkingLotDetails(id:string) {
    const { data } = await request({
      url: `${URLS.parkingLotDetails}${id}`,
      method: 'get',
    });
    return data as ParkingLotInfo;
  }

  /**
   * @description: 根据园区id查询停车场下拉数据
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async parkingLotDic(id:string) {
    const { data } = await request({
      url: `${URLS.parkingLotDic}${id}`,
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
     * @description: 删除停车场
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async parkingLotDel(id:string) {
    await request({
      url: `${URLS.parkingLotDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
       * @description: 新增停车场
       * @param {VisitorProcess} data
       * @return {*}
       * @author: GUOCHAO82
       */
  async parkingLotAdd(data:ParkingLotInfo) {
    await request({
      url: URLS.parkingLotAdd,
      data,
    });
  }

  /**
       * @description: 停车场
       * @param {LaneInfo} data
       * @return {*}
       * @author: GUOCHAO82
       */
  async parkingLotEdit(data:ParkingLotInfo) {
    await request({
      url: URLS.parkingLotEdit,
      method: 'put',
      data,
    });
  }

  // -----------------------------------车辆信息-------------------------------
  /**
   * @description: 车辆分页查询
   * @param {CarSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async carListPage<T>(searchData:CarSearchParams) {
    const { data } = await request({
      url: URLS.carListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<CarInfo>>list,
      total: <number>total,
    };
  }

  // 导入
  async importCar(searchData: any, id:string) {
    const { data } = await request({
      url: URLS.importCar,
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 通行记录
   * @param {CarPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async carRecordListPage<T>(searchData:CarPassRecordSearchParams) {
    const { data } = await request({
      url: URLS.carRecordListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<CarPassRecord>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 抓拍记录
   * @param {CarPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async carSnapRecordListPage<T>(searchData:CarPassRecordSearchParams) {
    const { data } = await request({
      url: URLS.carSnapRecordListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<CarPassRecord>>list,
      total: <number>total,
    };
  }

  /**
     * @description: 根据id查询车辆信息
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async carDetails(id:string) {
    const { data } = await request({
      url: `${URLS.carDetails}${id}`,
      method: 'get',
    });
    return data as CarInfo;
  }

  /**
     * @description: 删除车辆
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async carDel(id:string) {
    await request({
      url: `${URLS.carDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
       * @description: 新增车辆
       * @param {VisitorProcess} data
       * @return {*}
       * @author: GUOCHAO82
       */
  async carAdd(data:CarInfo) {
    await request({
      url: URLS.carAdd,
      data,
    });
  }

  /**
       * @description: 编辑车辆
       * @param {LaneInfo} data
       * @return {*}
       * @author: GUOCHAO82
       */
  async carEdit(data:CarInfo) {
    await request({
      url: URLS.carEdit,
      data,
    });
  }

  /**
   * @description: 通行记录导出
   * @param {CarPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async carRecordExport(searchData:CarPassRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.carRecordExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  /**
   * @description: 抓拍记录导出
   * @param {CarPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async carSnapRecordExport(searchData:CarPassRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.carSnapRecordExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
}

export * from './type';
