/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-16 17:25:03
 */
import Smarter from '~models/urlConstant/smarter-fire';
import request from '~utils/axios';
import {
  CustomerInfoKind, RoleListItem, SearchParams, setAddParams, updataStatusParams, modifyMeterReadingParams, warningRecordPageParams, RecordPageParams,
} from './type';

export default class SmarterMeter {
  // 分页查询
  async listMeter(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: Smarter.listMeter,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<RoleListItem>>list,
      total: <number>total,
    };
  }

  // id查询
  async findListMeterById(id:string) {
    const { data } = await request({
      url: `${Smarter.findListMeterById}/${id}`,
      method: 'GET',
    });

    return data;
  }

  /**
   * @description: 修改
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async updateMeterStatus(searchData:CustomerInfoKind) {
    const { data } = await request({
      url: Smarter.updateMeterStatus,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CustomerInfoKind>>list,
      total: <number>total,
    };
  }
}
export * from './type';
