/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:09
 * @LastEditTime: 2022-02-21 15:47:29
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import URLS from '@/models/urlConstant/advertisement';
import request from '~utils/axios';
import {
  SearchAdvertParams,
  AdvertListKind,
} from './type';

export default class AdvertVo {
  // 广告-分页查询
  async advertListPageApi<T>(searchData:SearchAdvertParams, init:boolean) {
    const { data } = await request({
      url: URLS.advertListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AdvertListKind>>list,
      total: <number>total,
    };
  }

  // 广告-新增
  async advertCreateApi(data:AdvertListKind) {
    await request({
      url: URLS.advertAdd,
      data,
    });
  }

  // 广告-修改
  async advertModifyApi(data:AdvertListKind) {
    await request({
      url: URLS.advertModify,
      data,
    });
  }

  // 广告删除
  async advertRemoveApi(id:string) {
    await request({
      url: `${URLS.advertDel}/${id}`,
      method: 'DELETE',
    });
  }

  // 广告查找
  async advertFindByIdApi(id:string) {
    const { data } = await request({
      url: `${URLS.advertFindById}/${id}`,
      method: 'GET',
    });
    return data;
  }
}

export * from './type';
