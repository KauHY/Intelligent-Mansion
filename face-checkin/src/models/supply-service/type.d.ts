import { number } from 'mathjs';
/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:59
 * @LastEditTime: 2022-06-06 12:27:21
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\type.d.ts
 */

interface supplyTable{
  parkOrgName: String, // 所属园区
  companyName: String, // 企业名称
  name: String, // 服务名称
  supplyType: number // 供应类型
  publishTime: String, // 发布时间
  source: number, // 来源
  state: number // 状态
}

// 搜索
interface searchInfo extends BaseSearchParams {
  parkOrgId: String,
  name: String,
  supplyType: number,
}

// 供应发布
interface LaneInfo {
  id: String,
  parkOrgId: String | null, // 所属园区
  companyId: String, // 企业名称
  name: String, // 服务名称
  supplyType: number | null, // 供应类型
  detail: String, // 供应详情
  jumpType: number | null, // 跳转类型
  url: String, // 跳转地址
  contact: String, // 联系人
  tel: String, // 联系电话
  remark: String, // 备注
}
interface handleInfo{
  id: String,
  state: number
}
export {
  searchInfo,
  LaneInfo,
  supplyTable,
  handleInfo,
};
