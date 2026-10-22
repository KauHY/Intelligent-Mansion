/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:48
 * @LastEditTime: 2022-03-30 22:12:21
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\index.ts
 */
import URLS from '~models/urlConstant/enterprise-service';
import request from '~utils/axios';
import { searchInfo, LaneInfo } from './type';

export default class BidService {
  // 投标记录查询
  async listTenderRecordPage<T>(searchData: searchInfo) {
    const { data } = await request<{list:Array<LaneInfo>, total:number}>({
      url: URLS.listTenderRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list,
      total,
    };
  }

  // 导出
  async bidExport(searchData: any) {
    const { data, headers } = await request({
      url: URLS.bidExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 确认
  async bidConfirm<T>(confirmMsg: String) {
    const { data } = await request({
      url: `${URLS.bidConfirm}${confirmMsg}`,
      method: 'GET',
    });
    return data;
  }
}
export * from './type';
