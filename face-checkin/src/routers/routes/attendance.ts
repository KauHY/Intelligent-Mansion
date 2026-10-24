/*
 * @Description:考勤管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-28 16:50:06
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-01 14:05:35
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/attendance';
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'attendance',
    props: true,
    redirect: { name: 'attendanceRules' },
    meta: {
      title: '考勤管理',
    },
  },
  {
    path: `${BASEPATH}/attendance-rules`,
    name: 'attendanceAttendanceRules',
    component: () => import(/* webpackChunkName: "attendance.attendance-rules" */ '~views/attendance/attendance-rules/index.vue'),
    meta: {
      title: '考勤规则',
    },
  },
  {
    path: `${BASEPATH}/month-attendance`,
    name: 'monthAttendance',
    component: () => import(/* webpackChunkName: "attendance.attendance-record" */ '~views/attendance/attendance-record/month-attendance.vue'),
    meta: {
      title: '月度考勤',
    },
  },
  {
    path: `${BASEPATH}/day-attendance`,
    name: 'dayAttendance',
    props: true,
    component: () => import(/* webpackChunkName: "attendance.attendance-record" */ '~views/attendance/attendance-record/day-attendance.vue'),
    meta: {
      title: '每日考勤',
    },
  },
  {
    path: `${BASEPATH}/clock-record`,
    name: 'clockRecord',
    props: true,
    component: () => import(/* webpackChunkName: "attendance.attendance-record" */ '~views/attendance/attendance-record/clock-record.vue'),
    meta: {
      title: '打卡记录',
    },
  },
  {
    path: `${BASEPATH}/field-application`,
    name: 'fieldApplication',
    component: () => import(/* webpackChunkName: "attendance.attendance-record" */ '~views/attendance/attendance-record/field-application.vue'),
    meta: {
      title: '外勤申请记录',
    },
  },
];
export default routes;
