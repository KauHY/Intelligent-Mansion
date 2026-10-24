/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:48
 * @LastEditTime: 2022-03-30 10:37:17
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\index.ts
 */
import URLS from '~models/urlConstant/enterprise-service';
import request from '~utils/axios';
import { searchInfo, LaneInfo, handleInfo } from './type';

export default class CompanyService {
  // 搜索
  async companyPages<T>(searchData: searchInfo) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.companyPages,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list,
      total,
    };
  }

  // 发布
  async companyChangeState<T>(companyMsg: handleInfo) {
    const { data } = await request({
      url: URLS.companyChangeState,
      method: 'POST',
      data: companyMsg,
    });
    return data;
  }

  // 新增
  async addCompanyService<T>(supplyMsg: LaneInfo) {
    const { data } = await request({
      url: URLS.addCompanyService,
      method: 'POST',
      data: supplyMsg,
    });
    return data;
  }

  // 删除企业服务
  async companyRemove<T>(supplyServiceId: String) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: `${URLS.companyRemove}${supplyServiceId}`,
      method: 'DELETE',
    });
    return data;
  }

  // 查询企业服务
  async companyQuery<T>(supplyServiceId: String) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: `${URLS.companyQuery}${supplyServiceId}`,
      method: 'GET',
    });
    return data;
  }

  // 修改
  async companyServiceModify<T>(supplyMsg: LaneInfo) {
    const { data } = await request({
      url: URLS.companyServiceModify,
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
}
export * from './type';
