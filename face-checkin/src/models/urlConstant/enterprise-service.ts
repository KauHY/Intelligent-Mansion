/*
 * @Author: your name
 * @Date: 2022-03-23 19:07:04
 * @LastEditTime: 2022-04-20 13:52:30
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\urlConstant\enterprise-service.ts
 */
enum EnterpriseService {
    searchPages = '/park-system/parkManagement/company/listCompanyPage', // 搜索表格
    perfectMsg = '/park-system/parkManagement/company/save', // 完善信息保存
    queryDetails = '/park-system/parkManagement/company/findById/', // 详情
    supplyPages = '/park-industrial/supply-service/listSupplyServicePage', // 供应服务查询搜索
    addSupplyService = '/park-industrial/supply-service/create', // 新增供应发布
    companyName = '/park-system/parkManagement/company/listCompanyDictByType', // 获取企业名称
    deletesupplyService = '/park-industrial/supply-service/remove/', // 删除供应服务
    querySupplyService = '/park-industrial/supply-service/findById/', // 查询供应服务
    changeState = '/park-industrial/supply-service/changeState', // 供应服务下架
    supplyServicemModify = '/park-industrial/supply-service/modify', // 供应服务修改
    companyPages = '/park-industrial/enterprise-service/listEnterpriseServicePage', // 企业查询
    addCompanyService = '/park-industrial/enterprise-service/create', // 新增企业
    companyChangeState = '/park-industrial/enterprise-service/changeState', // 企业服务发布
    companyRemove = '/park-industrial/enterprise-service/remove/', // 企业服务删除
    companyQuery = '/park-industrial/enterprise-service/findById/', // 企业服务按id查询
    companyServiceModify = '/park-industrial/enterprise-service/modify', // 企业服务编辑修改
    listPurchaseServicePage = '/park-industrial/purchase-service/listPurchaseServicePage', // 查询采购表格
    purchaseQueryDetails = '/park-industrial/purchase-service/findById/', // 采购详情
    purchaCseChangeState = '/park-industrial/purchase-service/changeState', // 采购发布
    purchaCseRemove = '/park-industrial/purchase-service/remove/', // 采购删除
    purchaCCseCreate = '/park-industrial/purchase-service/create', // 采购新增
    purchaMCCseModify = '/park-industrial/purchase-service/modify', // 采购修改
    listTenderRecordPage = '/park-industrial/tender-record/listTenderRecordPage', // 投标记录
    bidExport = '/park-industrial/tender-record/export', // 投标导出
    bidConfirm = '/park-industrial/tender-record/confirm/', // 投标确定
    listGroupCompanyPage = '/park-industrial/group-company-relation/listGroupCompanyPage', // 企业群组分页
    listMultistageCategoryTreemultistage='/park-common/multistage-category/listMultistageCategoryTree', // 多级分类表-获取园区下对应类型的树
    createmultistage='/park-common/multistage-category/create', // 多级分类表-新增
    modifymultistage='/park-common/multistage-category/modify', // 多级分类表-修改
    removemultistage='/park-common/multistage-category/remove', // 多级分类表-删除
    associateltistage = '/park-common/multistage-category/associate', // 多级分类-关联
    disassociate = '/park-common/multistage-category/disassociate', // 多级分类-取消关联
}
export default EnterpriseService;
