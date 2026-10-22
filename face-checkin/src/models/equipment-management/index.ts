/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-05 16:18:44
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-13 14:27:21
 */
import URLS from '~models/urlConstant/equipment-management';
import request from '~utils/axios';

import {
  EquipmentSearchParams,
  EquipmentInfo,
  SearchParams,
  HouseInfo,
  stopParams,
  EquipmentTreeParams,
  AddParams,
  CustomerInfoKind,
  CustomerCreated,
} from './type';

export default class EquipmentManagement {
  /**
     * @description: 车道分页查询
     * @param {EquipmentSearchParams} searchData
     * @return {*}
     * @author: GUOCHAO82
     */
  async equipmentListPage<T>(searchData:EquipmentSearchParams) {
    const { data } = await request({
      url: URLS.equipmentListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;

    return {
      list: <Array<EquipmentInfo>>list,
      total: <number>total,
    };
  }

  async listScreenDevicePage<T>(searchData:EquipmentSearchParams) {
    const { data } = await request({
      url: URLS.listScreenDevicePage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;

    return {
      list: <Array<EquipmentInfo>>list,
      total: <number>total,
    };
  }

  async equipmentDel(id:string) {
    await request({
      url: `${URLS.equipmentDel}${id}`,
      method: 'DELETE',
    });
  }

  async equipmentAdd(data:EquipmentInfo) {
    await request({
      url: URLS.equipmentAdd,
      data,
    });
  }

  async equipmentModify(data:EquipmentInfo) {
    await request({
      url: URLS.equipmentModify,
      data,
      // method: 'put',
    });
  }

  async equipmentFindById(id:string) {
    const { data } = await request({
      url: `${URLS.equipmentFindById}${id}`,
      method: 'get',
    });
    return data as EquipmentInfo;
  }

  async newEquipmentFindById(id:string) {
    const { data } = await request({
      url: `${URLS.newEquipmentFindById}${id}`,
      method: 'get',
    });
    return data as EquipmentInfo;
  }

  /**
   * @description:
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async equipmentListDic(parkOrgId:string, deviceType?:number) {
    const { data } = await request({
      url: URLS.equipmentListDic,
      data: {
        parkOrgId,
        deviceType,
      },
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
     * @description: 设备台账
     * @param {EquipmentSearchParams} searchData
     * @return {*}
     * @author: GUOCHAO82
     */
  async listEquipmentLedgerPage<T>(searchData:SearchParams) {
    const { data } = await request({
      url: URLS.listEquipmentLedgerPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;

    return {
      list: <Array<HouseInfo>>list,
      total: <number>total,
    };
  }

  async removeEquipmentLedger(id:string) {
    await request({
      url: `${URLS.removeEquipmentLedger}/${id}`,
      method: 'DELETE',
    });
  }

  async createEquipmentLedger(data:EquipmentInfo) {
    await request({
      url: URLS.createEquipmentLedger,
      data,
    });
  }

  async modifyEquipmentLedger(data:EquipmentInfo) {
    await request({
      url: URLS.modifyEquipmentLedger,
      data,
      // method: 'put',
    });
  }

  async findByIdEquipmentLedger(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdEquipmentLedger}/${id}`,
      method: 'get',
    });
    return data as EquipmentInfo;
  }

  async scrapmultistage(id:string) {
    await request({
      url: `${URLS.scrapmultistage}?id=${id}`,
    });
  }

  /** 多级分类
     * @description:
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async listMultistageCategoryTreemultistage<T>(searchData:EquipmentTreeParams) {
    const { data } = await request({
      url: URLS.listMultistageCategoryTreemultistage,
      method: 'POST',
      params: searchData,
    });
    return <any>data;
  }

  async removemultistage(id:string) {
    await request({
      url: `${URLS.removemultistage}/${id}`,
      method: 'DELETE',
    });
  }

  async createmultistage(data:EquipmentInfo) {
    await request({
      url: URLS.createmultistage,
      data,
    });
  }

  async modifymultistage(data:EquipmentInfo) {
    await request({
      url: URLS.modifymultistage,
      data,
      // method: 'put',
    });
  }

  async findByIdmultistage(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdmultistage}${id}`,
      method: 'get',
    });
    return data as EquipmentInfo;
  }

  /**
   * 导出
   */
  async goodsListPageExport(searchData: any) {
    const { data, headers } = await request({
      url: URLS.exportEquipmentLedger,
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
   * 导入
   */
  async goodsListPageimport(searchData: any) {
    const { data } = await request({
      url: URLS.importEquipmentLedger,
      data: searchData,
    });
    return data;
  }

  /**
     * @description: 消防设备管理
     * @param {EquipmentSearchParams} searchData
     * @return {*}
     * @author: GUOCHAO82
     */
  async listFireControlDevice<T>(searchData:SearchParams) {
    const { data } = await request({
      url: URLS.listFireControlDevice,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;

    return {
      list: <Array<HouseInfo>>list,
      total: <number>total,
    };
  }

  async removeFireControlDevice(id:string) {
    await request({
      url: `${URLS.removeFireControlDevice}/${id}`,
      method: 'DELETE',
    });
  }

  async createFireControlDevice(data:CustomerCreated) {
    await request({
      url: URLS.createFireControlDevice,
      data,
    });
  }

  async modifyFireControlDevice(data:CustomerCreated) {
    await request({
      url: URLS.modifyFireControlDevice,
      data,
      // method: 'put',
    });
  }

  async findFireControlDeviceId(id:string) {
    const { data } = await request({
      url: `${URLS.findFireControlDeviceId}/${id}`,
      method: 'get',
    });
    return data as EquipmentInfo;
  }

  // 消防设备数据同步
  async fireDeviceSync() {
    const { data } = await request({
      url: URLS.fireDeviceSync,
    });
    return data;
  }

  /**
     * @description: 卡片管理
     * @param {EquipmentSearchParams} searchData
     * @return {*}
     * @author: GUOCHAO82
     */
  async listCardPageCard<T>(searchData:SearchParams) {
    const { data } = await request({
      url: URLS.listCardPageCard,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;

    return {
      list: <Array<HouseInfo>>list,
      total: <number>total,
    };
  }

  /**
   * 导出
   */
  async exportCard(searchData: any) {
    const { data, headers } = await request({
      url: URLS.exportCard,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async removeCard(id:string) {
    await request({
      url: `${URLS.removeCard}/${id}`,
      method: 'DELETE',
    });
  }

  async createCard(data:CustomerCreated) {
    await request({
      url: URLS.createCard,
      data,
    });
  }

  async modifyCard(data:CustomerCreated) {
    await request({
      url: URLS.modifyCard,
      data,
      // method: 'put',
    });
  }

  async exchangeCard(data:CustomerCreated) {
    await request({
      url: URLS.exchangeCard,
      data,
      // method: 'put',
    });
  }

  async boundCard(data:CustomerCreated) {
    await request({
      url: URLS.boundCard,
      data,
      // method: 'put',
    });
  }

  async updateStatus(data:CustomerCreated) {
    await request({
      url: URLS.updateStatus,
      data,
      // method: 'put',
    });
  }

  async findByIdCard(id:string) {
    const { data } = await request({
      url: `${URLS.findByIdCard}/${id}`,
      method: 'get',
    });
    return data as EquipmentInfo;
  }

  /**
   * 导出
   */
  async exchangeFireControlDevice(searchData: any) {
    const { data, headers } = await request({
      url: URLS.exchangeFireControlDevice,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async equipmentSync(data:any) {
    await request({
      url: `${URLS.equipmentSync}`,
      method: 'POST',
      data,
    });
  }
}

export * from './type';
