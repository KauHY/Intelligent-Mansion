/*
 * @Description: 设备管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-05 17:33:09
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-13 10:09:24
 */
enum EquipmentManagement {
    getCarLane='/park-visitor/carLane/', // 获取停车场信息
    equipmentListPage = '/park-device/device-info/listPage', // 设备信息分页
    equipmentSync = '/park-device/device-info/sync', // 设备信息分页
    equipmentAdd = '/park-device/device-info/create', // 设备信息新增
    equipmentDel = '/park-device/device-info/remove/', // 设备信息分页
    equipmentModify = '/park-device/device-info/modify', // 设备信息分页
    equipmentListDic = '/park-device/device-info/listDropDownDevices', // 设备信息下拉
    equipmentFindById = '/park-device/device-info/findById/', // 设备信息详情
    newEquipmentFindById ='/park-customer/parkManagement/equipment-ledger/findById/', // 新设备信息详情

    listEquipmentLedgerPage='/park-customer/parkManagement/equipment-ledger/listEquipmentLedgerPage', // 设备信息-分页查询
    createEquipmentLedger ='/park-customer/parkManagement/equipment-ledger/create', // 设备信息-新增
    exportEquipmentLedger ='/park-customer/parkManagement/equipment-ledger/export', // 设备台账-导出
    modifyEquipmentLedger ='/park-customer/parkManagement/equipment-ledger/modify', // 设备信息-修改
    removeEquipmentLedger ='/park-customer/parkManagement/equipment-ledger/remove', // 设备信息-删除
    importEquipmentLedger ='/park-customer/parkManagement/equipment-ledger/import', // 设备信息-导入
    findByIdEquipmentLedger ='/park-customer/parkManagement/equipment-ledger/findById', // 设备信息-

    createmultistage='/park-common/multistage-category/create', // 多级分类表-新增
    findByIdmultistage='park-common/multistage-category/findById', // 多级分类表-根据ID查询
    getAllSubIdmultistage='/smartPark/park-common/multistage-category/getAllSubId', // 多级分类表-根据ID查询所有子节点id
    getIdsByOrderNamesmultistage='park-common/multistage-category/getIdsByOrderNames', // 通过按序多级名字获取ID
    hasSubmultistage='/park-common/multistage-category/hasSub', // 多级分类表-是否有下级节点
    listMultistageCategoryTreemultistage='/park-common/multistage-category/listMultistageCategoryTree', // 多级分类表-获取园区下对应类型的树
    modifymultistage='/park-common/multistage-category/modify', // 多级分类表-修改
    removemultistage='/park-common/multistage-category/remove', // 多级分类表-删除
    scrapmultistage='/park-customer/parkManagement/equipment-ledger/scrap',

    // 消防设备接口
    createFireControlDevice = '/park-device/fireControlDevice/create', // -新增
    removeFireControlDevice='/park-device/fireControlDevice/remove', // -删除
    modifyFireControlDevice='/park-device/fireControlDevice/modify', // -修改
    exchangeFireControlDevice='/park-device/fireControlDevice/exportFireControlDevice', // -导出
    listFireControlDevice='/park-device/fireControlDevice/listFireControlDevicePage', // 分页查询
    findFireControlDeviceId='/park-device/fireControlDevice/findById', // -根据ID查询
    fireDeviceSync='/park-device/device-info/fireDeviceSync', // 消防设备数据同步

    // 卡片管理
    boundCard='/park-device/card/boundCard', // 卡片管理-绑定
    createCard='/park-device/card/create', // 卡片管理-新增
    exchangeCard='/park-device/card/exchangeCard', // 卡片管理-换卡
    findByIdCard='/park-device/card/findById/', // 卡片管理-
    listCardPageCard='/park-device/card/listCardPage', // 卡片管理-
    modifyCard='/park-device/card/modify', // 卡片管理-修改
    removeCard='/park-device/card/remove/', // 卡片管理-删除
    updateStatus='/park-device/card/updateStatus', // 卡片管理-修改状态
    exportCard='/park-device/card/export', // 卡片管理-

    // 会议室信息屏
    listScreenDevicePage='/park-device/device-info/listScreenDevicePage', // 会议室信息屏-分页查询
}

export default EquipmentManagement;
