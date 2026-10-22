import { number } from 'mathjs';
/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:59
 * @LastEditTime: 2022-06-06 12:25:16
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\type.d.ts
 */

interface bidTable{
  companyName: String,
  contact: String,
  tel: String,
  mail: String,
  remark: String,
  biddingDocumentsPaid: number,
  marginPaid: number
}

// 搜索
interface searchInfo extends BaseSearchParams {
  id: String,
  purchaseId: String,
  companyName: String,
  contact: String,
  tel: String,
  mail: String,
  remark: String,
  biddingDocumentsPaid: number,
  marginPaid: number
}

interface LaneInfo {
  id: String,
  purchaseId: String,
  companyName: String,
  contact: String,
  tel: String,
  mail: String,
  remark: String,
  biddingDocumentsPaid: number,
  marginPaid: number
}

export {
  searchInfo,
  LaneInfo,
  bidTable,
};
