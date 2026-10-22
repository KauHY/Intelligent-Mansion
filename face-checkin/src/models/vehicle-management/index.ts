/*
 * @Author: your name
 * @Date: 2022-04-07 10:32:56
 * @LastEditTime: 2022-04-08 13:07:59
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\vehicle-management\index.ts
 */
/*
 * @Author: your name
 * @Date: 2022-04-07 10:19:37
 * @LastEditTime: 2022-04-07 10:31:34
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\entranceGuard-management\index.ts
 */
import URLS from '~models/urlConstant/equipment-management';
import request from '~utils/axios';
import {
  EquipmentSearchParams,
  VehicleInfo,
} from './type';

export default class VehicleManagement {
  async equipmentListPage<T>(searchData:EquipmentSearchParams) {
    const { data } = await request({
      url: URLS.equipmentListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;

    return {
      list: <Array<VehicleInfo>>list,
      total: <number>total,
    };
  }

  async equipmentModify(data:VehicleInfo) {
    await request({
      url: URLS.equipmentModify,
      data,
      // method: 'put',
    });
  }

  async equipmentDel(id:string) {
    await request({
      url: `${URLS.equipmentDel}${id}`,
      method: 'DELETE',
    });
  }

  async equipmentFindById(id:string) {
    const { data } = await request({
      url: `${URLS.equipmentFindById}${id}`,
      method: 'get',
    });
    return data as VehicleInfo;
  }

  async getCarLane(id:string) {
    const { data } = await request({
      url: `${URLS.getCarLane}/${id}/getByIp`,
      method: 'get',
    });
    return data as VehicleInfo;
  }
}

export * from './type';
