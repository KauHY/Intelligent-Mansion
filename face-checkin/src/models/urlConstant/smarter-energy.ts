/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 18:26:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-23 17:47:37
 */
enum Smarter {
    listMeter = '/park-device/meter/listMeterPage', // 仪表（1电表和2水表）-分页查询
    findListMeterById='/park-device/meter/findById', // 仪表（电表和水表）-根据ID查询
    createMeter = '/park-device/meter/create', // 仪表（1电能耗，2水能耗））-新增
    removeMeter='/park-device/meter/remove', // 仪表（电表和水表）-删除
    modifyMeter='/park-device/meter/modify', // 仪表（电表和水表）-修改
    exchangeMeter='/park-device/meter/exchangeMeter', // 仪表（电表和水表）-换表
    updateMeterStatus='/park-device/meter/updateMeterStatus', // 仪表（电表和水表）-修改状态
    listMeterReadingRecordPage='/park-device/meter-reading-record/listMeterReadingRecordPage', // 抄表记录-分页查询
    findRecordById='/park-device/meter-reading-record/findById', // 抄表记录-根据ID查询
    modifyMeterReading='/park-device/meter-reading-record/modifyMeterReading', // 修正读数-修改
    removeMeterReading='/park-device/meter-reading-record/remove', // 抄表记录-删除
    listMeterConsumptionRecordPage='/park-device/meterConsumptionRecord/listMeterConsumptionRecordPage', // 仪表（电表和水表）-修改状态
    listMeterWarningRecordPage='/park-device/meter-warning-record/listMeterWarningRecordPage', // 仪表异常告警记录-分页查询
    listMeterConsumptionAnalysis='/park-device/meterConsumptionRecord/listMeterConsumptionAnalysis', // -能耗分析查询

    selectMeterName='/park-device/meter/selectMeterName', // 仪表名称下拉框
    listMeterdownload='/park-device/meter/download', // 仪表（1电表和2水表）导出
    listMeterimportPoint='/park-device/meter/importMeters/', // 仪表（1电表和2水表）-导入
    listanalysisdownload='/park-device/meter-reading-record/analysis/download', // -能耗分析导出
    importMeterReadingRecord='/park-device/meter-reading-record/importMeterReadingRecord', // 抄表记录-导入读数
    downloadMeterReading='/park-device/meter-reading-record/readingRecordPage/download', // 抄表记录-导出
    downloadConsumptionRecord='/park-device/meterConsumptionRecord/consumptionRecord/download', // -用量记录导出

    listMeterConsumptionPage='/park-device/meterConsumption/listMeterConsumptionPage', // 企业或房屋水电用量-分页查询
    modifyShareConsumption='/park-device/meterConsumption/modifyShareConsumption', // 企业或房屋水电用量-本月分摊用量修改
    findById='/park-device/meterConsumption/findById/', // 企业或房屋水电用量-根据id查询
    downloadMeterConsumption='/park-device/meterConsumption/downloadMeterConsumption', // 企业或房屋水电用量-报表导出
}

export default Smarter;
