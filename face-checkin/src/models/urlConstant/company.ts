/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 12:41:35
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-16 17:30:30
 */
enum URLS {
    logout = '/park-system/parkManagement/auth/logout', // 登出
    resetPwd = '/park-system/parkManagement/user/resetPwd', // 用户管理-重置密码
    updatePwd = '/park-system/parkManagement/auth/updatePwd', // 修改密码
    save = '/park-system/parkManagement/user/save', // 新增用户
    update = '/park-system/parkManagement/user/update', // 更新用户
    remove = '/park-system/parkManagement/user/remove/', // 删除用户
    listPage = '/park-system/parkManagement/user/listPage', // 分页查询
    // findById = '/park-system/rs-user/findById/', // 根据id查询用户
    // enable = '/park-system/rs-user/enable/', // 启用
    // disable = '/park-system/rs-user/disable/', // 根据id查询用户
    updateStatus = '/park-system/parkManagement/user/updateStatus',
    rsUserList = '/park-system/parkManagement/user/listUsers', // 查询当前用户权限内可操作的所有用户
    resetPwdForForget = '/park-system/parkManagement/auth/resetPwdForForget', // 忘记密码
    validateIdentity = '/park-system/parkManagement/auth/validateIdentity', // 忘记密码验证身份

    login = '/park-system/business-user/login', // 登录
    register='/park-system/business-user/register', // 企业注册
    getSmsCode='/park-system/business-user/getSmsCode', // 注册获取短信验证码
    modifyInfos='/park-system/company-information-audit/modify', // 重新提交
    getLastAudit='/park-system/company-information-audit/getLastAudit', // 获取当前最新企业审核记录
    getDetail='/park-system/company-information-audit/getDetail', // -获取当前用户的企业详情
    getFlag='/park-system/company-audit/getFlag', // 企业是否完善
    settled='/park-system/company-audit/settled', // 企业是否入驻
    findByIdInfos='/park-system/company-information-audit/findById/', // 根据ID查询
    create='/park-system/company-information-audit/create', // -完善信息
    cancel='/park-system/company-information-audit/cancel/', // 取消申请
    auditInfos='/park-system/company-information-audit/audit', // 审核

    findById='/park-system/company-audit/findById/', // 入驻审核-根据ID查询
    createAudit='/park-system/company-audit/create', // 入驻申请-申请入驻
    cancelAudit='/park-system/company-audit/cancel/', // 入驻申请-取消申请
    audit='/park-system/company-audit/audit', // 入驻审核-审核

    listCompanyAuditPage='/park-system/company-audit/listCompanyAuditPage', // 企业入驻审核-分页查询
    listCompanyInformationAuditPage='/park-system/company-information-audit/listCompanyInformationAuditPage', // 入驻申请

    getMenuList='/park-system/business-menu/listBusinessMenu', // -获取配置菜单列表

    // 采购服务
    listPurchaseServicePage='/park-industrial/purchase-service/listPurchaseServicePage', // -分页查询
    findByIdPurchaseService='/park-industrial/purchase-service/findById/', // 根据ID查询
    createService='/park-industrial/purchase-service/create', // 新增
    modifyService='/park-industrial/purchase-service/modify',
    removeService='/park-industrial/purchase-service/remove/',
    listTenderRecordPage='/park-industrial/tender-record/listTenderRecordPage', // 投标记录-分页查询
    confirmTenderRecord='/park-industrial/tender-record/confirm/', // 投标记录-确定
    exportTenderRecord='/park-industrial/tender-record/export',
    changePurchaseState='/park-industrial/purchase-service/changeState',

    // 供应服务
    createSupply='/park-industrial/supply-service/create',
    findByIdSupply='/park-industrial/supply-service/findById/',
    listSupplyServicePageSupply='/park-industrial/supply-service/listSupplyServicePage',
    modifySupply='/park-industrial/supply-service/modify',
    removeSupply='/park-industrial/supply-service/remove/',
    changeState='/park-industrial/supply-service/changeState',

    // 合同费用
    listCompanyPortBillPage='/park-contract/rentContractBill/company/listCompanyPortBillPage', // 企业端账单-分页查询
    uploadFile='/park-contract/rentContractBill/company/uploadFile', // 企业端账单-上传凭证
    findByIdRentContractBill='/park-contract/rentContractBill/findById/', // 租赁合同账单-根据ID查询

    // 水电费用
    listMeterPortBillPage='/park-contract/meterConsumptionBill/company/listCompanyPortBillPage', // 企业端账单-分页查询
    uploadMeterFile='/park-contract/meterConsumptionBill/company/uploadFile', // 企业端账单-上传凭证
    findByIdConsumptionBill='/park-contract/meterConsumptionBill/findById/', // 企业水电账单管理-根据ID查询

    listParkDictByCode='park-system/parkManagement/company/listParkDictByCode', // 查询企业入驻的园区
    listCompanyPage='park-system/parkManagement/company/listCompanyPage', // 查询企业名称和id

}
export default URLS;
