/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-11 18:24:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-01 15:40:37
 */
import URLS from '~models/urlConstant/house';
import request from '~utils/axios';
import { SearchParams, HouseInfo, TreeNodeItem } from './type';

export default class House {
  async searchListPage(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.searchHouseList,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<HouseInfo>>list,
      total: <number>total,
    };
  }

  async searchHouseOptsOne(value:string) {
    const { data } = await request({
      url: `${URLS.listHouseDict}${value}`,
      method: 'get',
    });
    return <SelectOptions>data;
  }

  async searchHouseOpts(buildingId:any, floor?:string) {
    const { data } = await request({
      url: `${URLS.listHouseDict}${buildingId}`,
      method: 'get',
      data: {
        floorNum: floor,
        buildingId,
      },
    });
    return <SelectOptions>data;
  }

  async searchListBuildingTree(buildingId:string) {
    const { data } = await request({
      url: `${URLS.listBuildingTree}${buildingId}`,
      method: 'get',
    });
    return <TreeNodeItem>data;
  }

  async add(data:HouseInfo) {
    await request({
      url: URLS.save,
      data,
    });
  }

  async del(id:string) {
    await request({
      url: `${URLS.del}${id}`,
      method: 'delete',
    });
  }

  async housefindById(id:string) {
    const { data } = await request({
      url: `${URLS.housefindById}${id}`,
      method: 'get',
    });
    return data as HouseInfo;
  }
}

export * from './type';
