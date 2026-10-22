/*
 * @Description: 信息统计
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-24 14:22:51
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-30 13:47:57
 */
enum InformationStatistical {
    formTemplateListPage = '/park-customer/parkManagement/formTemplate/listFormTemplatePage', // 表单分页
    formTemplateAdd = '/park-customer/parkManagement/formTemplate/create', // 表单模板新增
    formTemplateDel = '/park-customer/parkManagement/formTemplate/remove/', // 表单模板删除
    formTemplateDesign = '/park-customer/parkManagement/formTemplate/edit', // 表单模板设计
    formTemplateModify = '/park-customer/parkManagement/formTemplate/modify', // 表单模板设置编辑
    formTemplateFindById = '/park-customer/parkManagement/formTemplate/findById/', // 表单模板详情
    formTemplateDic = '/park-customer/parkManagement/formTemplate/listFormTemplateDict', // 表单模板下拉数据
    // 表单管理
    formManageListPage = '/park-customer/parkManagement/form/listFormPage', // 表单管理分页
    formManageAdd = '/park-customer/parkManagement/form/create', // 表单管理新增
    formManageDel = '/park-customer/parkManagement/form/remove/', // 表单管理删除
    formManageModify = '/park-customer/parkManagement/form/modify', // 表单管理编辑
    formManageFindById = '/park-customer/parkManagement/form/findById/', // 表单管理详情
    formManageUpdateStatus = '/park-customer/parkManagement/form/updateStatus', // 表单管理详情
}
export default InformationStatistical;
