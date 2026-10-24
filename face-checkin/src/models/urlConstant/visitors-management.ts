/*
 * @Description: 访客管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 11:05:21
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-24 15:31:52
 */

enum CustomerService {
    visitorSubscribeListPage = '/park-visitor/visitorRecord/pageVisitorSubscribe', // 访客预约记录分页查询
    historyVisitorListPage = '/park-visitor/visitorRecord/pageHistoryVisitor', // 历史访客分页查询
    currentVisitorListPage = '/park-visitor/visitorRecord/pageCurrentVisitor', // 当前访客分页查询
    visitorRegister = '/park-visitor/visitorRecord/subscribe', // 访客登记
    visitorDetails = '/park-visitor/visitorRecord/', // 根据id查询访客记录详情
    visitorRecordDel = '/park-visitor/visitorRecord/', // 访客记录删除

    // 审批人配置
    auditPersonDetails = '/park-visitor/auditPersonConfig/', // 根据id查询审批人
    auditPersonAdd = '/park-visitor/auditPersonConfig', // 审批人新增
    auditPersonEdit = '/park-visitor/auditPersonConfig', // 审批人编辑
    auditPersonDel = '/park-visitor/auditPersonConfig/', // 审配人删除
    auditPersonListPage = '/park-visitor/auditPersonConfig/page', // 审批人分页
    removePerson = '/park-visitor/process-config/remove/', // 删除访客

     // 访客类型
    visitorTypeDetails = '/park-visitor/visitor-type/findById/', // 根据id查询访客类型
    visitorTypeAdd = '/park-visitor/visitor-type/create', // 访客类型新增
    visitorTypeEdit = '/park-visitor/visitor-type/modify', // 访客类型编辑
    visitorTypeDel = '/park-visitor/visitor-type/remove/', // 访客类型删除
    visitorTypeListPage = '/park-visitor/visitor-type/listVisitorTypePage', // 访客类型分页
    visitorTypeDic = '/park-visitor/visitor-type/listVisitorTypeByCompany', // 查询访客类型下拉数据

    // 访客流程
    visitorProcessDetails = '/park-visitor/process-config/findById/', // 根据id查询访客流程
    visitorProcessAdd = '/park-visitor/process-config/create', // 访客流程新增
    visitorProcessEdit = '/park-visitor/process-config/modify', // 访客流程编辑
    visitorProcessDel = '/park-visitor/process-config/remove/', // 访客流程删除
    visitorProcessListPage = '/park-visitor/process-config/listProcessConfigPage', // 访客流程分页
    visitorProcessModifyStatus = '/park-visitor/process-config/modifyStatus', // 访客流程禁用/启用

    // 公司绑定信息
    companyBindInfoEdit = '/park-system/parkManagement/company/clientBindInfo', // 企业绑定信息修改
    companyBindInfoListPage = '/park-system/parkManagement/company/clientBindInfo/page', // 企业绑定信息分页查询
    companyBindInfoDel = '/park-system/parkManagement/company/clientBindInfo/', // 企业绑定信息删除
    companyBindInfoDetails = '/park-system/parkManagement/company/findById/clientBindInfo/', // 企业绑定信息详情

     // 来访须知
     visitingInstructionsDetails = '/park-visitor/notice/findById/', // 根据id查询来访须知
     visitingInstructionsAdd = '/park-visitor/notice/create', // 来访须知新增
     visitingInstructionsEdit = '/park-visitor/notice/modify', // 来访须知编辑
     visitingInstructionsDel = '/park-visitor/notice/remove/', // 来访须知删除
     visitingInstructionsPage = '/park-visitor/notice/listNoticePage', // 来访须知分页

     visitingSignOut='park-visitor/visitorRecord/signOut' // 迁出
}
export default CustomerService;
