/*
 * @Author: your name
 * @Date: 2022-04-13 11:21:12
 * @LastEditTime: 2022-04-20 13:53:24
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\company-group\index.ts
 */
import URLS from '~models/urlConstant/enterprise-service';
import request from '~utils/axios';

import {
  searchParams,
  LaneInfo,
  EquipmentTreeParams,
  GroupLaneInfo,
  GroupAddInfo,
} from './type';

export default class CompanyGroupService {
  // 分页
  // async listGroupCompanyPage<T>(searchData:SearchParams) {
  //   const { data } = await request({
  //     url: URLS.listGroupCompanyPage,
  //     method: 'POST',
  //     data: searchData,
  //   });
  //   const { list, total } = data;
  //   return {
  //     list: <Array<LaneInfo>>list,
  //     total: <number>total,
  //   };
  // }
  async listGroupCompanyPage<T>(searchData: searchParams) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.listGroupCompanyPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list,
      total,
    };
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

  async createmultistage(data:GroupLaneInfo) {
    await request({
      url: URLS.createmultistage,
      data,
    });
  }

  async modifymultistage(data:GroupLaneInfo) {
    await request({
      url: URLS.modifymultistage,
      data,
      // method: 'put',
    });
  }

  async associateltistage(data:GroupAddInfo) {
    await request({
      url: URLS.associateltistage,
      data,
      // method: 'put',
    });
  }

  async removemultistage(id:string) {
    await request({
      url: `${URLS.removemultistage}/${id}`,
      method: 'DELETE',
    });
  }

  async disassociate(data:any) {
    await request({
      url: `${URLS.disassociate}`,
      data,
    });
  }
}
export * from './type';
