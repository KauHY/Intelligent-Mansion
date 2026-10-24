/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-07 14:19:24
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-12 10:39:45
 */
enum PsersonPass {
    personPassRecordListPage = '/park-visitor/access-record/successListPage', // 通行记录分页
    personPassSnapRecordListPage = '/park-visitor/access-record/listPage', // 抓拍记录分页
    personPassRecordFindById = '/park-device/device-info/findById/', // 根据id查询通行记录详情

    personPassRecordExport = '/park-visitor/access-record/access/export', // 通行记录导出
    personPassSnapRecordExport = '/park-visitor/access-record/export', // 抓拍记录导出

    accessPolicyListPage = '/park-visitor/access-policy/listPage', // 通行策略分页
    accessPolicyAdd = '/park-visitor/access-policy/create', // 通行策略新增
    accessPolicyDel = '/park-visitor/access-policy/remove/', // 通行策略分页
    accessPolicyModify = '/park-visitor/access-policy/modify', // 通行策略分页
    accessPolicySetState = '/park-visitor/access-policy/setStatus', // 通行策略状态编辑
    accessPolicyFindById = '/park-visitor/access-policy/findById/', // 通行策略详情

    listDropDownDevices='/park-device/device-info/listDropDownDevices',

}
export default PsersonPass;
