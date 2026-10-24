/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-22 15:35:16
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 14:22:03
 */
enum House {
    searchHouseList = '/park-system/parkManagement/house/listHousePage', // 根据园区id查询某园区下的所有楼栋名称
    save = '/park-system/parkManagement/house/save', // 保存楼栋
    del = '/park-system/parkManagement/house/remove/', // 删除楼栋
    housefindById = '/park-system/parkManagement/house/findHouseById/', // 根据id查询房屋详情
    listHouseDict = '/park-system/parkManagement/house/listHouseDict/', // 根据id查询楼栋下所有房屋的名称
    listBuildingTree = '/park-system/parkManagement/building/listBuildingTree/', // 根据园区id查询楼栋下所有楼栋房屋树

}

export default House;
