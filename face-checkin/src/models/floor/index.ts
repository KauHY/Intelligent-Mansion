/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-11 18:24:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-03 10:54:01
 */
import { identity } from 'mathjs';
import URLS from '~models/urlConstant/floor';
import request from '~utils/axios';
import { SearchParams, HouseInfo } from './type';

export default class House {
  async searchFloorOpts(buildingId:string|number) {
    const { data } = await request<number>({
      url: `${URLS.getBuildingTotalFloor}?buildingId=${buildingId}`,
      method: 'get',
    });
    return data;
  }
}

export * from './type';
