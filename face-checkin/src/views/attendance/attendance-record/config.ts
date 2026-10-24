/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-03-01 11:26:35
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { DictionaryMap } from '~composition/useHashMapDic';
import { AttendanceInfo } from '~models/attendance';

const sameColumns = (slotsFlag = false) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '所属公司',
    dataIndex: 'companyName',
  },
  {
    title: '姓名',
    dataIndex: 'employeeName',
    slots: { customRender: slotsFlag ? 'employeeName' : '' },
  },
];

export const getMonthAttendColumns = () => [
  ...sameColumns(true),
  {
    title: '月份',
    dataIndex: 'month',
    customRender: ({ record }:{record:AttendanceInfo}) => `${record.year || ''}-${record.month || ''}`,
  },
  {
    title: '打卡异常天数',
    dataIndex: 'exceptionTimes',
  },
  {
    title: '未出勤天数',
    dataIndex: 'absenceTimes',
  },
];

export const getDayAttendColumns = (options: DictionaryMap<['ATTENDANCE_RESULT_TYPE']>) => [
  ...sameColumns(true),
  {
    title: '日期',
    dataIndex: 'date',
  },
  {
    title: '上班时间',
    dataIndex: 'punchInTime',
  },
  {
    title: '下班时间',
    dataIndex: 'punchOutTime',
  },
  {
    title: '考勤结果',
    dataIndex: 'attendanceResult',
    customRender: ({ record }:{record:AttendanceInfo}) => options.ATTENDANCE_RESULT_TYPE?.[record.attendanceResult as number]?.label,
  },
];

export const getClockInColumns = (options: DictionaryMap<['PUNCH_TYPE']>) => [
  ...sameColumns(),
  {
    title: '打卡时间',
    dataIndex: 'punchDateTime',
  },
  {
    title: '打卡地点',
    dataIndex: 'location',
  },
  {
    title: '打卡方式',
    dataIndex: 'punchType',
    // customRender: ({ record }:{record:AttendanceInfo}) => options.PUNCH_TYPE?.[record.type as number]?.label,
    slots: { customRender: 'punchType' },
  },
];

export const getApplicationColumns = (options: DictionaryMap<['FIELDWORK_APPLY_STATE']>) => [
  ...sameColumns(),
  {
    title: '申请时间',
    dataIndex: 'createTime',
  },
  {
    title: '外勤时间',
    dataIndex: 'fieldworkStartTime',
    customRender: ({ record }:{record:AttendanceInfo}) => `${record.fieldworkStartTime || ''}-${record.fieldworkEndTime || ''}`,
  },
  {
    title: '外勤地点',
    dataIndex: 'situation',
  },
  {
    title: '事由',
    dataIndex: 'reason',
  },
  {
    title: '状态',
    dataIndex: 'state',
    customRender: ({ record }:{record:AttendanceInfo}) => options.FIELDWORK_APPLY_STATE?.[record.state as number]?.label,
  },
];
