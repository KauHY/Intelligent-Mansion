/*
 * @Description: 合同管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-27 14:25:36
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-27 14:38:13
 */
enum ContractMangement {
    contractListPage = '/park-contract/contract/listPage', // 合同分页查询
    contractEdit = '/park-contract/contract/modify', // 合同修改
    contractDel = '/park-contract/contract/remove/', // 合同删除
    contractTermination = '/park-contract/contract/termination/', // 合同终止
    contractNullify = '/park-contract/contract/nullify/', // 合同作废
    contractFindById = '/park-contract/contract/findById/', // 根据id查询合同详情
    contractExtend = '/park-contract/contract/extend', // 续约
    contractAdd = '/park-contract/contract/create', // 根据id查询合同详情
    contractChange = '/park-contract/contract/change', // 合同变更

}
export default ContractMangement;
