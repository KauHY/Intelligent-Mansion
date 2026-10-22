/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-22 16:18:25
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-02 16:30:54
 */
enum Floor {
    listBuildingDict = '/park-system/parkManagement/building/listBuildingDict/', // 根据园区id查询某园区下的所有楼栋名称
    save = '/park-system/parkManagement/building/save', // 保存楼栋
    del = '/park-system/parkManagement/building/remove/', // 删除楼栋
    findBuildingById = '/park-system/parkManagement/building/findBuildingById/', // 根据楼栋id查详情
    getBuildingTotalFloor='/park-system/parkManagement/building/getBuildingTotalFloor', // 完善园区信息
    importBuildings='/park-system/parkManagement/building/importBuildings', // 楼栋导入
    importHouses='/park-system/parkManagement/house/importHouses', // 房屋导入
    exportHouses='/park-system/parkManagement/house/exportHouses', // 房屋导出
}

export default Floor;
