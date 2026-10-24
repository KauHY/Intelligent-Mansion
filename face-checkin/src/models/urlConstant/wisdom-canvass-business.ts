/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 18:26:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-23 17:47:37
 */
enum businessInvitation {
    customerManagementListpage = '/park-business/business/customer-info/listCustomerPage', // 招商客户管理
    housingBusinessListPage = '/park-business/business/business-info/listBusinessInfoPage', // 房源招商分页查询
    housingBusinessExport='/park-business/business/business-info/export', // 导出
    housingBusinessAdd = '/park-business/business/business-info/create', // 新增招商信息
    housingBusinessEdit = '/park-business/business/business-info/modify', // 招商信息编辑
    housingBusinessDel = '/park-business/business/business-info/remove/', // 删除招商信息
    housingBusinessById = '/park-business/business/business-info/findById/', // 根据id查询招商信息
    housingUpdateStatus = '/park-business/business/business-info/updateStatus', // 根据id查询招商信息

    // 招商房屋
    houseInfoListPage = '/park-business/business/house-info/listHouseInfoPage', // 房屋招商分页查询
    houseModify = '/park-business/business/house-info/modify', // 修改房屋信息
    housefindById = '/park-business/business/house-info/findById/', // 根据招商房源id查询
    houseDel= '/park-business/business/house-info/remove/', // 根据招商房源id查询

    listHouseDict = '/park-business/business/house-info/listHouseDict/', // 根据园区id查询意向房源下拉数据
    houseIntentionListPage = '/park-business/business/house-info/selectIntentionHousePage', // 根据园区id查询意向房源列表(去重之后)
    selectListHouseInfoPage = '/park-business/business/house-info/selectListHouseInfoPage', // 意向房源下拉(用于房源招商选择招商房屋)
    addBusinessHouse = '/park-business/business/house-info/create', // 新增招商房屋
    businessBuildingDict = '/park-business/business/house-info/listBuildingDict/', // 根据园区id查询招商楼栋
    businessHouseInfoDict = '/park-business/business/house-info/listHouseInfoDict/', // 根据楼栋id查询招商楼栋

    // 商机管理
    businessInfoListPage = '/park-business/business/business-chance/listBusinessInfoPage/', // 商机分页查询
    businessInfoDel = '/park-business/business/business-chance/remove/', // 商机删除
    businessInfoOperate = '/park-business/business/business-chance/operate', // 商机操作 2分配商机、3领取商机、7转移商机、8释放商机
    businessInfoEdit = '/park-business/business/business-chance/modify', // 商机编辑
    businessInfoDistribution = '/park-business/business/business-chance/distribution', // 商机分配
    businessInfoAdd = '/park-business/business/business-chance/create', // 商机新增
    businessInfoFollow = '/park-business/business/business-chance/follow', // 商机跟进
    businessInfoRemind = '/park-business/business/business-chance/remind', // 商机提醒
    businessInfoConvertToCustomer = '/park-business/business/business-chance/convertToCustomer', // 商机转成客户
    businessInfoFindById = '/park-business/business/business-chance/findById/', // 跟进id查询商机信息
    followRecordFindById = '/park-business/business/business-chance/getFollowRecordsById/', // 跟进id查询商机信息
    stageProcessById = '/park-business/business/business-chance/getStageProcessById/', // 根据id查询阶段流程
    businessInfoExport='/park-business/business/business-chance/export/', // 商机-导出:type,0全部商机，1公共商机，2我的商机
    // 看房申请
    houseApplyForListPage = '/park-business/business/apply-for-house/listApplyForHousePage', // 看房申请分页查询
    houseApplyForAccept = '/park-business/business/apply-for-house/accept', // 看房申请受理
    houseApplyForFindById = '/park-business/business/apply-for-house/findById/', // 看房申请根据id查询详情

}

export default businessInvitation;
