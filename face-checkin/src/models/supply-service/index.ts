/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:48
 * @LastEditTime: 2022-03-30 10:26:41
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\index.ts
 */
import URLS from '~models/urlConstant/enterprise-service';
import request from '~utils/axios';
import { searchInfo, LaneInfo, handleInfo } from './type';

export default class SupplyService {
  // 搜索
  async supplyPages<T>(searchData: searchInfo) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.supplyPages,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return data;
  }

  async saveSupplyMsg<T>(supplyMsg: LaneInfo) {
    const { data } = await request({
      url: URLS.addSupplyService,
      method: 'POST',
      data: supplyMsg,
    });
    return data;
  }

  // 修改
  async supplyServicemModify<T>(supplyMsg: LaneInfo) {
    const { data } = await request({
      url: URLS.supplyServicemModify,
      method: 'POST',
      data: supplyMsg,
    });
    return data;
  }

  async companyName(parkOrgId:string) {
    const { data } = await request({
      url: URLS.companyName,
      method: 'get',
      data: {
        parkOrgId,
      },
    });
    return data as SelectOptions;
  }

  // 删除供应服务
  async deletesupplyService<T>(supplyServiceId: String) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: `${URLS.deletesupplyService}${supplyServiceId}`,
      method: 'DELETE',
    });
    return data;
  }

  // 查询供应服务
  async querysupplyService<T>(supplyServiceId: String) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: `${URLS.querySupplyService}${supplyServiceId}`,
      method: 'GET',
    });
    return data;
  }

  async changeState<T>(supplyMsg: handleInfo) {
    const { data } = await request({
      url: URLS.changeState,
      method: 'POST',
      data: supplyMsg,
    });
    return data;
  }

  // async queryDetails<T>(queryId: String) {
  //   const { data } = await request({
  //     url: `${URLS.queryDetails}${queryId}`,
  //     method: 'POST',
  //   });
  //   const { list, total } = data;
  //   return data;
  // }
}
export * from './type';
