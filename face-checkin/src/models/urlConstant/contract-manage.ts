/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-12 14:19:53
 * @LastEditTime: 2022-04-20 15:18:01
 * @LastEditors: Xucaizhi3
 * @Description:
 */
enum ContractMmanage {
  ulrPrefix = 'park-contract/contract',
  contractListPage = 'park-contract/contract/listPage', // 合同列表
  listBillingSchemePage = 'park-contract/contract/listBillingSchemePage', // 计费方案-分页查询
  contractCreate = 'park-contract/contract/create', // 合同起草
  contractSubmitForReview = 'park-contract/contract/submitAudit', // 合同提交审核
  contractReview = 'park-contract/contract/audit', // 合同审核
  contractModify = 'park-contract/contract/modify', // 合同修改
  contractNullify = 'park-contract/contract/nullify', // 合同作废
  contractRemove = 'park-contract/contract/remove', // 合同删除
  contractTermination = 'park-contract/contract/termination', // 合同终止
  contractFindById = 'park-contract/contract/findById', // 合同查找
  contractExtend = 'park-contract/contract/extend', // 合同续约
  contractExport = 'park-contract/contract/export', // 导出合同
  contractChange = 'park-contract/contract/change', // 合同变更
  contractCheckNoConflict = 'park-contract/contract/checkNoConflict', // 合同校验编号冲突
  contractGetNoByType = 'park-contract/contract/getNoByType', // 合同校验编号冲突
  contractWithdrawal = 'park-contract/contract/withdraw', // 合同退约
  contractUnavailableHouse = 'park-contract/contract/getUnavailableHouse', // 合同校验编号冲突
  calculationListByFeeType = 'park-contract/contract/getCalculationListByFeeType', // 计费方案-根据收费类型获取计算标准下拉
  // 房源状态
  houseStatusListPage = 'park-contract/contract/vo/listPage', // 房源状态列表
  houseStatusExport = 'park-contract/contract/vo/export', // 房源状态列表导出
  // 模板管理
  getTemplateField = '/park-contract/template-library/findByType/', // 查询模板字段库
  templateListPage = '/park-contract/template/listTemplatePage', // 合同模板分页查询
  templateAdd = '/park-contract/template/create', // 合同模板导入
  templateDel ='/park-contract/template/remove/', // 合同模板删除

  // 生成合同
  generateContract='park-contract/contract/generateContract', // 生成合同
}
export default ContractMmanage;
