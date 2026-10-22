/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:48
 * @LastEditTime: 2022-03-29 15:27:26
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\index.ts
 */
import URLS from '~models/urlConstant/enterprise-service';
import request from '~utils/axios';
import { searchInfo, LaneInfo } from './type';

export default class EnterpriseService {
  // 搜索
  async equipmentListPage<T>(searchData: searchInfo) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.searchPages,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return data;
  }

  async savePerfectMsg<T>(perfectMsg: LaneInfo) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.perfectMsg,
      method: 'POST',
      data: perfectMsg,
    });
    // const { list, total } = data;
    return data;
  }

  async queryDetails<T>(queryId: String) {
    const { data } = await request({
      url: `${URLS.queryDetails}${queryId}`,
      method: 'GET',
    });
    const { list, total } = data;
    return data;
  }

  // async equipmentDel(id:string) {
  //   await request({
  //     url: `${URLS.equipmentDel}id`,
  //     method: 'DELETE',
  //   });
  // }

  // async equipmentAdd(data:EquipmentInfo) {
  //   await request({
  //     url: URLS.equipmentAdd,
  //     data,
  //   });
  // }

  // async equipmentModify(data:EquipmentInfo) {
  //   await request({
  //     url: URLS.equipmentModify,
  //     data,
  //   });
  // }

  // async equipmentFindById(id:string) {
  //   const { data } = await request({
  //     url: `${URLS.equipmentFindById}id`,
  //     method: 'get',
  //   });
  //   return data as EquipmentInfo;
  // }
}
export * from './type';
