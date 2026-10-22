/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 18:26:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-02 16:33:38
 */
enum Apartment {
    createApartment = '/park-visitor/apartmentPerson/create', // 公寓人员管理-新增
    exportApartment = '/park-visitor/apartmentPerson/export', // 公寓人员管理-导出
    findByIdApartment = '/park-visitor/apartmentPerson/findById/', // 公寓人员管理-根据ID查询
    importApartment = '/park-visitor/apartmentPerson/import', // 公寓人员管理-导入
    listApartmentPersonPage = '/park-visitor/apartmentPerson/listApartmentPersonPage', // 公寓人员管理-分页查询
    modifyApartment = '/park-visitor/apartmentPerson/modify', // 公寓人员管理-修改
    removeApartment = '/park-visitor/apartmentPerson/remove/', // 公寓人员管理-删除
    updateStatusApartment = '/park-visitor/apartmentPerson/updateStatus', // 公寓人员管理-启用/禁用

    findHousePage='/park-contract/contract/vo/findHousePage', // 公寓人员管理关联房屋使用
    listDropDownDevices='/park-device/device-info/listDropDownDevices', // 设备信息-下拉列表
    fingerprint = '/park-system/parkManagement/employee/finger', // 指纹采集
}

export default Apartment;
