/*
 * @Description: 入住企业管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-26 10:50:18
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-28 19:03:34
 */

enum CompanyMangement {
   importEmployeesZip='/park-system/parkManagement/employee/importEmployeesZip', // 上传压缩包
   importEmployeesNew='/park-system/parkManagement/employee/importEmployeesNew', // 员工导入
   companyListPage = '/park-system/parkManagement/company/listCompanyPage', // 入住企业分页查询
   companyDel = '/park-system/parkManagement/company/remove/', // 入住企业删除
   companyAdd = '/park-system/parkManagement/company/save', // 入住企业新增
   bindHouse='/park-system/parkManagement/house-rent-record/bindHouse/', // 绑定房屋
   companyBindHouseListPage = '/park-system/parkManagement/house-rent-record/pageCompanyHouseBind', // 企业已绑定房屋分页查询
   companyExport = '/park-system/parkManagement/company/export', // 企业导出
   importCompany='/park-system/parkManagement/company/importCompany', // 入驻企业导入
   updateStatus='/park-system/parkManagement/company/updateStatus', // 更改企业状态

   listCompanyDict = '/park-system/parkManagement/company/listCompanyDict', // 企业下拉数据
   curUserPropertyCompanyDict = '/park-system/parkManagement/company/curUserPropertyCompanyDict', // 获取当前用户的物业公司

   importEmployees='/park-system/parkManagement/employee/importEmployees',
   unbindHouse = '/park-system/parkManagement/house-rent-record/unbindHouse/', // 房屋解绑
   pageHouseBindRecord = '/park-system/parkManagement/house-rent-record/pageHouseBindRecord', // 房屋入住记录分页查询
   pageHouseRenewBindRecord = '/park-system/parkManagement/house-rent-record/pageHouseRenewBindRecord', // 房屋续租记录分页查询
   renewHouse = '/park-system/parkManagement/house-rent-record/renewHouse', // 房屋续租
   getRecordById = '/park-system/parkManagement/house-rent-record/getRecordById/', // 获取房屋租赁详情
   employeeListPage = '/park-system/parkManagement/employee/listEmployeePage', // 员工分页查询
   employeeDownload = '/park-system/parkManagement/employee/download', // 文件导出
   employeeAdd = '/park-system/parkManagement/employee/create', // 新增员工
   employeeDel = '/park-system/parkManagement/employee/remove/', // 删除员工
   employeeEdit = '/park-system/parkManagement/employee/modify', // 编辑员工信息
   employeeUpdateStatus = '/park-system/parkManagement/employee/updateStatus', // 更新员工状态
   // listEmployeeDict = '/park-system/parkManagement/employee/listEmployeeDict', // 员工下拉列表数据查询/支持模糊搜索
   // listSpecialEmployeeDict = '/park-system/parkManagement/employee/listSpecialEmployeeDict', // 根据园区id查询物业企业员工下拉列表数据查询/支持模糊搜索
   selectEmployeesList = '/park-system/parkManagement/employee/selectEmployeesList', // 查询员工下拉数据
   listHandlerDict = '/park-system/parkManagement/employee/listHandlerDict', // 查询员工下拉数据[用于投诉/报修]
   employeesDictByTagSort = '/park-system/parkManagement/employee/selectEmployees', // 员工列表下拉（按岗位分类）
   // ------------选人组件-----------------------------------
   selectTagTree = '/park-system/parkManagement/employee/selectTagTree', // 第一列获取园区公司和标签
   selectEmployeesByIdAndType = '/park-system/parkManagement/employee/selectEmployeesByIdAndType', //  根据第一列筛选条件查询员工

   listJobDict = '/park-system/job/listJobDict', // 工作岗位下拉项
   listJobPage = '/park-system/job/listJobPage', // 岗位分页查询
   jobEdit = '/park-system/job/modify', // 岗位编辑
   jobAdd = '/park-system/job/create', // 新增岗位
   jobDel = '/park-system/job/remove/', // 新增岗位

   listTagDict = '/park-system/parkManagement/tag/listTagDict', // 员工标签下拉数据
   listTagPage = '/park-system/parkManagement/tag/listPage', // 标签分页查询
   tagAdd = '/park-system/parkManagement/tag/create', // 标签新增
   tagEdit = '/park-system/parkManagement/tag/modify', // 标签修改
   tagDel = '/park-system/parkManagement/tag/remove/', // 标签删除

   companyCheckListPage = '/park-system/company-audit/listCompanyAuditPage', // 入住企业审核分页查询
   companyCheckDetail = '/park-system/company-audit/findById', // 入住企业审核详情
   companyCheckVerify = '/park-system/company-audit/audit', // 入住企业审核
   companyCheckInfoListPage = '/park-system/company-information-audit/listCompanyInformationAuditPage', // 入住企业审核信息
   companyCheckInfoDetail = '/park-system/company-information-audit/getDetail', // 入住企业审核信息详情
   companyCheckInfoVerify = '/park-system/company-information-audit/audit', // 入住企业审核信息审核

}
export default CompanyMangement;
