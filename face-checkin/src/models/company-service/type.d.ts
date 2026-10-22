import { number } from 'mathjs';
/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:59
 * @LastEditTime: 2022-06-06 12:28:02
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\type.d.ts
 */

interface companyTable{
  name: String,
  groupType: number,
  pushRange: String,
  updateTime: String,
  state: number,
}

// 搜索
interface searchInfo extends BaseSearchParams {
  parkOrgId: String,
  name: String,
}

// 企业服务
interface LaneInfo {
  id: String,
  name: String,
  pushRange: String | Array<String>,
  groupType: number | null | string,
  remark: String,
  summary: String,
  detail: String,
  url: String,
  tel: String,
  banner: String,
  att: String,
}
interface handleInfo{
  id: String,
  state: number
}
export {
  searchInfo,
  LaneInfo,
  companyTable,
  handleInfo,
};
