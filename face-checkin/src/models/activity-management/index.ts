/*
 * @Description: 活动管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-11 10:43:10
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-22 18:12:40
 */
import URLS from '~models/urlConstant/activity-management';
import request from '~utils/axios';
import {
  ActivitySearchParams,
  ActivityInfo,
  SignUpRecordSearchParams,
  SignUpRecord,
} from './type';

export default class {
  /**
   * @description: 活动分页
   * @param {ActivitySearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async activityListPage<T>(searchData:ActivitySearchParams) {
    const { data } = await request({
      url: URLS.activityListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<ActivityInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 活动删除
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async activityDel(id:string) {
    await request({
      url: `${URLS.activityDel}${id}`,
      method: 'DELETE',
    });
  }

  async activityAdd(data:ActivityInfo) {
    await request({
      url: URLS.activityAdd,
      data,
    });
  }

  async activityModify(data:ActivityInfo) {
    await request({
      url: URLS.activityModify,
      data,
    });
  }

  async activityFindById(id:string) {
    const { data } = await request({
      url: `${URLS.activityFindById}${id}`,
      method: 'get',
    });

    // const { parkOrgNames } = data as ActivityInfo;
    // data.parkOrgIds = parkOrgNames?.map(({ value }:{value:string}) => value);
    return data;
  }

  async toppingActivity(data:{activityId:string, topping:boolean}) {
    await request({
      url: URLS.toppingActivity,
      data,
    });
  }

  /**
   * @description: 报名记录分页查询
   * @param {ActivitySearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async listActivityRegisterRecordPage<T>(searchData:SignUpRecordSearchParams) {
    const { data } = await request({
      url: URLS.listActivityRegisterRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<SignUpRecord>>list,
      total: <number>total,
    };
  }

  async exportActivityRegisterRecord(searchData:SignUpRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.exportActivityRegisterRecord,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
}
export * from './type';
