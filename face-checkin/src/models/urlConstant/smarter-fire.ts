/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 18:26:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-23 17:47:37
 */
enum Smarter {
    listMeter = '/park-device/fireWarningRecord/listFireWarningRecordPage', // -分页查询
    findListMeterById='/park-device/fireWarningRecord/findById', // -根据ID查询
    updateMeterStatus='/park-device/fireWarningRecord/handle', // -报警
}

export default Smarter;
