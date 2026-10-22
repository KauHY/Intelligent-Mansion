/*
 * @Description: 考勤规则
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-28 16:48:23
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-02-25 10:31:28
 */
enum Attendance {
    attendanceListPage = '/park-attendance/attendance-rule/listAttendanceRulePage', // 考勤规则分页
    attendanceAdd = '/park-attendance/attendance-rule/create', // 考勤规则新增
    attendanceDel = '/park-attendance/attendance-rule/remove/', // 考勤规则删除
    attendanceModify = '/park-attendance/attendance-rule/modify', // 考勤规则修改
    attendanceListDic = '/park-device/device-info/listDropDownDevices', // 考勤规则下拉
    attendanceFindById = '/park-attendance/attendance-rule/findById/', // 考勤规则详情
    modifyStatus = '/park-attendance/attendance-rule/modifyStatus', // 考勤规则启用禁用
    getAllEmployeeByRuleId = '/park-attendance/attendance-rule-relation/getAllEmployeeByRuleId/', // 通过考勤规则ID查询所有员工
    listAttendanceMonthRecordPage = '/park-attendance/attendance-month-record/listAttendanceMonthRecordPage', // 月度考勤分页
    listAttendanceRecordPage = '/park-attendance/attendance-record/listAttendanceRecordPage', // 每日考勤分页
    listPunchRecordPage = '/park-attendance/punch-record/listPunchRecordPage', // 打卡记录分页
    listFieldworkApplyRecordPage = '/park-attendance/fieldwork-apply-record/listFieldworkApplyRecordPage', // 外勤申请记录分页
    monthRecordExport = '/park-attendance/attendance-month-record/export',
    dayRecordExport = '/park-attendance/attendance-record/export',
    punchRecordExport = '/park-attendance/punch-record/export',
    applicationRecordExport = '/park-attendance/fieldwork-apply-record/export',
}
export default Attendance;
