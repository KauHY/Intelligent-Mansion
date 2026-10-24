import { number } from 'mathjs';
/*
 * @Author: your name
 * @Date: 2022-03-22 17:36:59
 * @LastEditTime: 2022-06-06 12:27:39
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\enterprise-service\type.d.ts
 */

interface enterpriseTable{
  parkOrgName: String,
  name: String,
  companyType: number
  industryField: number
  companyTaxesType: number
  enterpriseSize: number
}

// 搜索
interface searchInfo extends BaseSearchParams{
  id: string,
  parkOrgId: String,
  name: String,
  companyType: number,
}

// 完善信息
interface LaneInfo {
  id: String,
  code: String,
  licenses: String,
  parkOrgId: String | null, // 所属园区
  parkOrgName: String, // 园区名称
  name: String, // 企业名称
  companyType: number | null, // 企业类型
  industryField: number | null, // 行业领域
  companyTaxesType: number | null, // 企业纳税类型
  enterpriseSize: number | null, // 企业规模
  atts: String, // 附件
  remark: String, // 备注
  corporateTax: number | null,
}

export {
  searchInfo,
  LaneInfo,
  enterpriseTable,
};
