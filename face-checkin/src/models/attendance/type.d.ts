/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-28 16:47:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 17:06:35
 */
interface AttendanceRulesSearchParams extends BaseSearchParams {
  // 考勤规则分页查询
}

interface AttendanceRecordSearchParams extends BaseSearchParams {
  // 考勤规则分页查询
}

interface AttendanceRules {
  applyType: 0 | 1 | null // 规则适用类型，0：全公司员工适用，1：指定员工
  companyId: string
  companyName: string
  daysOfWork: number[] // 关联工作日
  holiday: 0 | 1
  id: string
  latitude: number | null // 纬度
  location: string // 考勤地点
  longitude: number | null // 经度
  parkOrgId: string
  parkOrgName: string
  punchInTime: string // 上班打卡时间
  punchOutTime: string // 下班打卡时间
  punchRange: number | null // 考勤范围（米）
  ruleName: string // 规则名称
  employeeIds: string[]
  status?: 0 | 1 | null // 状态，0：禁用 1：启用
  workdayType: number
}
interface AttendanceInfo {
  year: string
  month: string
  date: string
  employeeName: string
  attendanceResult: number | null // 考勤结果 1：正常 2：异常
  type: number | null // 打卡方式，1：上下班打卡 2：外勤打卡
  state: number | null //
  fieldworkStartTime: string // 外勤开始时间
  fieldworkEndTime: string // 外勤结束时间
  companyId: string
  companyName: string
  id: string
  location: string // 考勤地点
  parkOrgId: string
  parkOrgName: string
  punchInTime: string // 上班打卡时间
  punchOutTime: string // 下班打卡时间
  extension?: {
    reason: string,
    picKey: string,
  }
}

 type PunchType = 'field-application'|'day-attendance'|'clock-record' // field-application 外勤打卡| day-attendance 每日打卡

 type Obj = {
   [key:string]:any
 }
interface AttendanceBaseConfig {
  api: string,
  exportApi: string,
  clockInfo?: Obj,
  columns: Obj,
  searchConf: Obj
  type?:PunchType
}
export {
  AttendanceRulesSearchParams, AttendanceRules, AttendanceInfo, AttendanceRecordSearchParams, AttendanceBaseConfig, PunchType,
};
