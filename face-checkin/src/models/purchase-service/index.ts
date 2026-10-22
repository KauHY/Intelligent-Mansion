/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:48
 * @LastEditTime: 2022-03-30 12:14:37
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\index.ts
 */
import URLS from '~models/urlConstant/enterprise-service';
import request from '~utils/axios';
import { searchInfo, LaneInfo, handleInfo } from './type';

export default class PurchaseService {
  // 采购搜索
  async listPurchaseServicePage<T>(searchData: searchInfo) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.listPurchaseServicePage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return data;
  }

  // 采购发布
  async purchaCseChangeState<T>(supplyMsg: handleInfo) {
    const { data } = await request({
      url: URLS.purchaCseChangeState,
      method: 'POST',
      data: supplyMsg,
    });
    return data;
  }

  // 修改采购
  async purchaMCCseModify<T>(supplyMsg: LaneInfo) {
    const { data } = await request({
      url: URLS.purchaMCCseModify,
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

  // 新增
  async purchaCCseCreate<T>(purchaCMsg: LaneInfo) {
    const { data } = await request({
      url: URLS.purchaCCseCreate,
      method: 'POST',
      data: purchaCMsg,
    });
    return data;
  }

  // 删除采购服务
  async purchaCseRemove<T>(supplyServiceId: String) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: `${URLS.purchaCseRemove}${supplyServiceId}`,
      method: 'DELETE',
    });
    return data;
  }

  // 查询采购服务详情
  async purchaseQueryDetails<T>(supplyServiceId: String) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: `${URLS.purchaseQueryDetails}${supplyServiceId}`,
      method: 'GET',
    });
    return data;
  }
}
export * from './type';
