/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 17:57:48
 */
import URLS from '~models/urlConstant/park';
import request from '~utils/axios';
import { SearchParams, ParkListItem, ParkInfo } from './type';

export default class Park {
  async searchListPage<T>(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.listPageByOrgId,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<ParkListItem>>list,
      total: <number>total,
    };
  }

  async searchParkListName() { // 查询关联园区列表
    const { data } = await request({
      url: URLS.listParkName,
      method: 'post',
    });
    return <SelectOptions>data;
  }

  async getTenantParkOrgDict() { // 查询关联园区列表
    const { data } = await request({
      url: URLS.getTenantParkOrgDict,
      method: 'get',
    });
    return <SelectOptions>data;
  }

  async getParkInfoById(id:string) {
    const { data } = await request({
      url: `${URLS.getParkInfoById}${id}`,
      method: 'GET',
    });
    return data as ParkInfo;
  }

  async save(data:ParkInfo) {
    await request({
      url: URLS.save,
      data,
    });
  }

  // 查询园区下所有公司 0 物业| 1 普通企业 | 不传查询所有公司
  async listCompanyDictByType(parkOrgId:string, companyType?:0|1) {
    const { data } = await request({
      url: URLS.listCompanyDictByType,
      method: 'get',
      data: {
        parkOrgId,
        companyType,
      },
    });
    return data as SelectOptions;
  }
}
export * from './type';
