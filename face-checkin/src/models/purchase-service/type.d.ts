import { number } from 'mathjs';
/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:59
 * @LastEditTime: 2022-06-06 12:26:49
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\type.d.ts
 */

interface purchaseTable{
  parkOrgName: String, // 所属园区
  companyName: String, // 企业名称
  name: String, // 服务名称，
  purchaseType: number,
  createTime: String,
  effectiveEndTime: String,
  source: number,
  state: number,
}

// 搜索
interface searchInfo extends BaseSearchParams {
  name: String,
}

// 采购发布
interface LaneInfo {
  id: String,
      effectiveEndTime: String | any, // 截至时间
      attKey: String, // 附件
  parkOrgId: String | null, // 所属园区
  companyId: String, // 企业名称
  name: String, // 服务名称
  purchaseType: number | null, // 采购类型
  detail: String, // 供应详情
  jumpType: number | null, // 跳转类型
  url: String, // 跳转地址  公众号ID
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
  purchaseTable,
  handleInfo,
};
