/*
 * @Author: your name
 * @Date: 2022-04-07 10:19:37
 * @LastEditTime: 2022-04-07 18:40:55
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\entranceGuard-management\index.ts
 */
import URLS from '~models/urlConstant/equipment-management';
import request from '~utils/axios';
import {
  EntranceGuardTable,
  EntranceGuardInfo,
} from './type';

export default class EntranceGuard {
  async equipmentModify(data:EntranceGuardInfo) {
    await request({
      url: URLS.equipmentModify,
      data,
      // method: 'put',
    });
  }
}
export * from './type';
