/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-09 16:21:55
 */
import Smarter from '~models/urlConstant/smart-apartment';
import request from '~utils/axios';
import {
  AddParams,
  RoleListItem,
  SearchParams,
  updataStatusParams,
  HouseInfo,
  SearchHouseParams,
  Fingerprint,
} from './type';

export default class SmarterMeter {
  // 分页查询
  async listApartmentPersonPage(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: Smarter.listApartmentPersonPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    const arr: any[] = [];
    let houseObj = {};
    list.forEach((item: any, index:any) => {
      item.houseList.forEach((key:any, ind:any) => {
        houseObj = {
          rowSpan: ind,
          rowSpanLength: ind === 0 ? item.houseList.length : 0,
          companyName: item.companyName,
          createDate: item.createDate,
          employeeName: item.employeeName,
          status: item.status,
          pics: item.pics,
          parkOrgName: item.parkOrgName,
          mobile: item.mobile,
          idCardNumber: item.idCardNumber,
          employeeId: item.employeeId,
          id: item.id,
          houseList: {
            houseFunction: key.houseFunction,
            houseId: key.houseId,
            houseName: key.houseName,
          },
        };
        arr.push(houseObj);
      });
    });
    // console.log(arr, 'arr');
    return {
      list: <Array<RoleListItem>>arr,
      total: <number>total,
    };
  }

  async searchHouseListPage(searchData:SearchHouseParams) {
    const { data } = await request({
      url: Smarter.findHousePage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<HouseInfo>>list,
      total: <number>total,
    };
  }

  // 设备信息-下拉列表
  async listDropDownDevices(deviceType:any, parkOrgId:any) {
    const { data } = await request({
      url: `${Smarter.listDropDownDevices}?deviceType=${deviceType}&&parkOrgId=${parkOrgId} `,
      method: 'GET',
    });
    return data;
  }

  // id查询
  async findByIdApartment(id:string) {
    const { data } = await request({
      url: `${Smarter.findByIdApartment}/${id}`,
      method: 'GET',
    });
    return data;
  }

  /**
   * @description: 新增
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async createApartment(searchData:AddParams) {
    const { data } = await request({
      url: Smarter.createApartment,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  /**
   * @description: 删除
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async removeApartment(id:string) {
    await request({
      url: `${Smarter.removeApartment}/${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 修改状态
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async updateStatusApartment(searchData:updataStatusParams) {
    await request({
      url: `${Smarter.updateStatusApartment}`,
      method: 'POST',
      data: searchData,
    });
  }

  /**
   * @description: 修改
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async modifyApartment(searchData:AddParams) {
    const { data } = await request({
      url: Smarter.modifyApartment,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  // 导出
  async exportApartment(searchData: any) {
    const { data, headers } = await request({
      url: Smarter.exportApartment,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 导入
  async importApartment(searchData: any, id:string) {
    const { data } = await request({
      url: `${Smarter.importApartment}`,
      data: searchData,
    });
    return data;
  }

  // 指纹采集
  async fingerprint(data:Fingerprint) {
    await request({
      url: Smarter.fingerprint,
      data,
    });
  }
}
export * from './type';
